import time
import json
import requests
import hmac
import hashlib
from urllib.parse import urlencode

# ServerTime、Signature
class TOOL(object):

    def _get_server_time(self):
        return int(time.time()*1000)

    def _sign_v3(self, req_time, sign_params=None):
        if sign_params:
            sign_params = urlencode(sign_params)
            to_sign = "{}&timestamp={}".format(sign_params, req_time)
        else:
            to_sign = "timestamp={}".format(req_time)
        sign = hmac.new(self.mexc_secret.encode('utf-8'), to_sign.encode('utf-8'), hashlib.sha256).hexdigest()
        return sign

    def public_request(self, method, url, params=None):
        url = '{}{}'.format(self.hosts, url)
        return requests.request(method, url, params=params)

    def sign_request(self, method, url, params=None):
        url = '{}{}'.format(self.hosts, url)
        req_time = self._get_server_time()
        if params:
            params['signature'] = self._sign_v3(req_time=req_time, sign_params=params)
        else:
            params={}
            params['signature'] = self._sign_v3(req_time=req_time)
        params['timestamp'] = req_time
        headers = {
            'x-mexc-apikey': self.mexc_key,
            'Content-Type': 'application/json',
        }
        return requests.request(method, url, params=params, headers=headers)

# Market Data
class mexc_market(TOOL):

    def __init__(self, mexc_hosts):
        self.api = '/api/v3'
        self.hosts = mexc_hosts
        self.method = 'GET'

    def get_ping(self):
        """test connectivity"""
        url = '{}{}'.format(self.api, '/ping')
        response = self.public_request(self.method, url)
        return response.json()

    def get_timestamp(self):
        """get sever time"""
        url = '{}{}'.format(self.api, '/time')
        response = self.public_request(self.method, url)
        return response.json()

    def get_exchangeInfo(self, params=None):
        """get exchangeInfo"""
        url = '{}{}'.format(self.api, '/exchangeInfo')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_depth(self, params):
        """get symbol depth"""
        url = '{}{}'.format(self.api, '/depth')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_deals(self, params):
        """get current trade deals list"""
        url = '{}{}'.format(self.api, '/trades')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_aggtrades(self, params):
        """get aggregate trades list"""
        url = '{}{}'.format(self.api, '/aggTrades')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_kline(self, params):
        """get k-line data"""
        url = '{}{}'.format(self.api, '/klines')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_avgprice(self, params):
        """get current average prcie(default : 5m)"""
        url = '{}{}'.format(self.api, '/avgPrice')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_24hr_ticker(self, params):
        """get 24hr prcie ticker change statistics"""
        url = '{}{}'.format(self.api, '/ticker/24hr')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_price(self, params):
        """get symbol price ticker"""
        url = '{}{}'.format(self.api, '/ticker/price')
        response = self.mexc_request(self.method, url, params=params)
        return response.json()

    def get_bookticker(self, params=None):
        """get symbol order book ticker"""
        url = '{}{}'.format(self.api, '/ticker/bookTicker')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_ETF_info(self, params=None):
        """get ETF information"""
        url = '{}{}'.format(self.api, '/etf/info')
        response = self.public_request(self.method, url, params=params)
        return response.json()

# Spot Trade
class mexc_trade(TOOL):

    def __init__(self, mexc_hosts, mexc_key, mexc_secret):
        self.api = '/api/v3'
        self.hosts = mexc_hosts
        self.mexc_key = mexc_key
        self.mexc_secret = mexc_secret

    def post_order_test(self, params):
        """test new order"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/order/test')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_order(self, params):
        """place order"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/order')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_batchorders(self, params):
        """place batch orders"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/batchOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_order(self, params):
        """
        cancel order
        'origClientOrderId' or 'orderId' must be sent
        """
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/order')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_openorders(self, params):
        """
        cancel all order for a single symbol
        """
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/openOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_order(self, params):
        """
        get order
        'origClientOrderId' or 'orderId' must be sent
        """
        method = 'GET'
        url = '{}{}'.format(self.api, '/order')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_openorders(self, params):
        """get current pending order """
        method = 'GET'
        url = '{}{}'.format(self.api, '/openOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_allorders(self, params):
        """
        get current all order
        startTime and endTime need to use at the same time
        """
        method = 'GET'
        url = '{}{}'.format(self.api, '/allOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_mytrades(self, params):
        """
        get current all order
        orderId need to use with symbol at the same time
        """
        method = 'GET'
        url = '{}{}'.format(self.api, '/myTrades')
        response = self.sign_request(method, url, params=params)
        return response.json()

# Spot Account
class mexc_account(TOOL):

    def __init__(self, mexc_hosts, mexc_key, mexc_secret):
        self.api = '/api/v3'
        self.hosts = mexc_hosts
        self.mexc_key = mexc_key
        self.mexc_secret = mexc_secret

    def get_account_info(self):
        """get account information"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/account')
        response = self.sign_request(method, url)
        return response.json()

    def get_coinlist(self):
        """get coin list"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/capital/config/getall')
        response = self.sign_request(method, url)
        return response.json()

# Sub-Account
class mexc_subaccount(TOOL):

    def __init__(self, mexc_hosts, mexc_key, mexc_secret):
        self.api = '/api/v3/sub-account'
        self.hosts = mexc_hosts
        self.mexc_key = mexc_key
        self.mexc_secret = mexc_secret

    def post_virtualSubAccount(self, params):
        """create a sub-account"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/virtualSubAccount')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_SubAccountList(self, params=None):
        """get sub-account list"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/list')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_virtualApiKey(self, params):
        """create sub-account's apikey"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/apiKey')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_virtualApiKey(self, params):
        """get sub-account's apikey"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/apiKey')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_virtualApiKey(self, params):
        """delete sub-account's apikey"""
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/apiKey')
        response = self.sign_request(method, url, params=params)
        return response.json()

# Margin Account & Trade
class mexc_margin(TOOL):

    def __init__(self, mexc_hosts, mexc_key, mexc_secret):
        self.api = '/api/v3/margin'
        self.hosts = mexc_hosts
        self.mexc_key = mexc_key
        self.mexc_secret = mexc_secret

    def post_tradeMode(self, params):
        """switch tradeMode"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/tradeMode')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_margin_Order(self, params):
        """place margin order"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/order')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_loan(self, params):
        """loan"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/loan')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_repay(self, params):
        """repay loan"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/repay')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_openOrders(self, params):
        """cancel margin openOrders(All orders for a single symbol)"""
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/openOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_Order(self, params):
        """cancel margin order"""
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/order')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_loan_history(self, params):
        """get loan history"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/loan')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_allOrders(self, params):
        """get historical commission records"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/allOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_myTrades(self, params):
        """get historical deals"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/myTrades')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_openOrders(self, params):
        """get current pending order record"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/openOrders')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_maxTransferable(self, params):
        """get the maximum transferable amount"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/maxTransferable')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_priceIndex(self, params):
        """get margin price index"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/priceIndex')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_order(self, params):
        """get margin account order details"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/order')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_isolated_account(self, params):
        """get isolated margin account information"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/isolated/account')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_trigerOrder(self):
        """get stop profit or stop loss order"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/trigerOrder')
        response = self.sign_request(method, url)
        return response.json()

    def get_maxBorrowable(self, params):
        """get account's maximum loan amount"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/maxBorrowable')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_repay_history(self, params):
        """get repay history"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/repay')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_isolated_pair(self, params):
        """get isolated margin symbol"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/isolated/pair')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_forceLiquidationRec(self, params):
        """get account forced liquidation record"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/forceLiquidationRec')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_isolatedMarginData(self, params):
        """get isolated margin rate and limit"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/isolatedMarginData')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_isolatedMarginTier(self, params):
        """get isolated tier"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/isolatedMarginTier')
        response = self.sign_request(method, url, params=params)
        return response.json()
