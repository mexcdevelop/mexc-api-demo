import requests
import hmac
import hashlib
from urllib.parse import urlencode, quote
import config

# ServerTime、Signature
class TOOL(object):

    def _get_server_time(self):
        return requests.request('get', 'https://api.mexc.com/api/v3/time').json()['serverTime']

    def _sign_v3(self, req_time, sign_params=None):
        if sign_params:
            sign_params = urlencode(sign_params, quote_via=quote)
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
            params = {}
            params['signature'] = self._sign_v3(req_time=req_time)
        params['timestamp'] = req_time
        headers = {
            'x-mexc-apikey': self.mexc_key,
            'Content-Type': 'application/json',
        }
        return requests.request(method, url, params=params, headers=headers)


# Market Data
class mexc_market(TOOL):

    def __init__(self):
        self.api = '/api/v3'
        self.hosts = config.mexc_host
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

    def get_defaultSymbols(self):
        """get defaultSymbols"""
        url = '{}{}'.format(self.api, '/defaultSymbols')
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

    def get_24hr_ticker(self, params=None):
        """get 24hr prcie ticker change statistics"""
        url = '{}{}'.format(self.api, '/ticker/24hr')
        response = self.public_request(self.method, url, params=params)
        return response.json()

    def get_price(self, params=None):
        """get symbol price ticker"""
        url = '{}{}'.format(self.api, '/ticker/price')
        response = self.public_request(self.method, url, params=params)
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

    def __init__(self):
        self.api = '/api/v3'
        self.hosts = config.mexc_host
        self.mexc_key = config.api_key
        self.mexc_secret = config.secret_key

    def get_selfSymbols(self):
        """get currency information"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/selfSymbols')
        response = self.sign_request(method, url)
        return response.json()

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
        """place batch orders(same symbol)"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/batchOrders')
        params = {"batchOrders": str(params)}
        response = self.sign_request(method, url, params=params)
        print(response.url)
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

    def post_mxDeDuct(self, params):
        """Enable MX DeDuct"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/mxDeduct/enable')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_mxDeDuct(self):
        """MX DeDuct status"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/mxDeduct/enable')
        response = self.sign_request(method, url)
        return response.json()

    def get_account_info(self):
        """get account information"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/account')
        response = self.sign_request(method, url)
        return response.json()


# Wallet
class mexc_wallet(TOOL):

    def __init__(self):
        self.api = '/api/v3/capital'
        self.hosts = config.mexc_host
        self.mexc_key = config.api_key
        self.mexc_secret = config.secret_key

    def get_coinlist(self):
        """get currency information"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/config/getall')
        response = self.sign_request(method, url)
        return response.json()

    def post_withdraw(self, params):
        """withdraw"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/withdraw/apply')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def cancel_withdraw(self, params):
        """withdraw"""
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/withdraw')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_deposit_list(self, params):
        """deposit history list"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/deposit/hisrec')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_withdraw_list(self, params):
        """withdraw history list"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/withdraw/history')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_deposit_address(self, params):
        """generate deposit address"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/deposit/address')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_deposit_address(self, params):
        """get deposit address"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/deposit/address')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_withdraw_address(self, params):
        """get deposit address"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/withdraw/address')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_transfer(self, params):
        """universal transfer"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/transfer')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_transfer_list(self, params):
        """universal transfer history"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/transfer')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_transfer_list_byId(self, params):
        """universal transfer history (by tranId)"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/transfer/tranId')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_transfer_internal(self, params):
        """universal transfer"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/transfer/internal')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_transfer_internal_list(self, params=None):
        """universal transfer"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/transfer/internal')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_smallAssets_list(self):
        """small Assets convertible list"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/convert/list')
        response = self.sign_request(method, url)
        return response.json()

    def post_smallAssets_convert(self, params):
        """small Assets convert"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/convert')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_smallAssets_history(self, params=None):
        """small Assets convertible history"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/convert')
        response = self.sign_request(method, url, params=params)
        return response.json()


# Sub-Account
class mexc_subaccount(TOOL):

    def __init__(self):
        self.api = '/api/v3'
        self.hosts = config.mexc_host
        self.mexc_key = config.api_key
        self.mexc_secret = config.secret_key

    def post_virtualSubAccount(self, params):
        """create a sub-account"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/sub-account/virtualSubAccount')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_SubAccountList(self, params=None):
        """get sub-account list"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/sub-account/list')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_virtualApiKey(self, params):
        """create sub-account's apikey"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/sub-account/apiKey')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_virtualApiKey(self, params):
        """get sub-account's apikey"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/sub-account/apiKey')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_virtualApiKey(self, params):
        """delete sub-account's apikey"""
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/sub-account/apiKey')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def post_universalTransfer(self, params):
        """universal transfer between accounts"""
        method = 'POST'
        url = '{}{}'.format(self.api, '/capital/sub-account/universalTransfer')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_universalTransfer(self, params):
        """universal transfer history between accounts"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/capital/sub-account/universalTransfer')
        response = self.sign_request(method, url, params=params)
        return response.json()


# Rebate
class mexc_rebate(TOOL):

    def __init__(self):
        self.api = '/api/v3/rebate'
        self.hosts = config.mexc_host
        self.mexc_key = config.api_key
        self.mexc_secret = config.secret_key

    def get_taxQuery(self, params=None):
        """get the rebate commission record"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/taxQuery')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_rebate_detail(self, params=None):
        """get rebate record details"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/detail')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_kickback_detail(self, params=None):
        """get self-return record details"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/detail/kickback')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_inviter(self, params=None):
        """get self-return record details"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/referCode')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_affiliate_commission(self, params=None):
        """get affiliate commission history"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/affiliate/commission')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_affiliate_withdraw(self, params=None):
        """get affiliate withdraw history"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/affiliate/withdraw')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_affiliate_commission_detail(self, params=None):
        """get affiliate commission details"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/affiliate/commission/detail')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_affiliate_referral(self, params=None):
        """get affiliate referral"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/affiliate/referral')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def get_affiliate_subaffiliates(self, params=None):
        """get affiliate subaffiliates"""
        method = 'GET'
        url = '{}{}'.format(self.api, '/affiliate/subaffiliates')
        response = self.sign_request(method, url, params=params)
        return response.json()


# WebSocket ListenKey
class mexc_listenkey(TOOL):

    def __init__(self):
        self.api = '/api/v3'
        self.hosts = config.mexc_host
        self.mexc_key = config.api_key
        self.mexc_secret = config.secret_key

    def post_listenKey(self):
        """ generate ListenKey """
        method = 'POST'
        url = '{}{}'.format(self.api, '/userDataStream')
        response = self.sign_request(method, url)
        return response.json()

    def get_listenKey(self):
        """ get valid ListenKey """
        method = 'GET'
        url = '{}{}'.format(self.api, '/userDataStream')
        response = self.sign_request(method, url)
        return response.json()

    def put_listenKey(self, params):
        """ extend ListenKey validity """
        method = 'PUT'
        url = '{}{}'.format(self.api, '/userDataStream')
        response = self.sign_request(method, url, params=params)
        return response.json()

    def delete_listenKey(self, params):
        """ delete ListenKey """
        method = 'DELETE'
        url = '{}{}'.format(self.api, '/userDataStream')
        response = self.sign_request(method, url, params=params)
        return response.json()
