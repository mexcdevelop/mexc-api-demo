import time
import json
import requests
import hmac
import hashlib
from urllib.parse import urlencode


BASE_URL = 'https://api.mexc.com'
API_KEY = 'your access key'
SECRET_KEY = 'your serect key'


def _get_server_time():
    return int(time.time()*1000)


def _sign_v3(sign_params={}):
    sign_params = urlencode(sign_params)
    if sign_params:
        to_sign = "{}&timestamp={}".format(sign_params, _get_server_time())
    else:
        to_sign = "timestamp={}".format(_get_server_time())
    sign = hmac.new(SECRET_KEY.encode('utf-8'), to_sign.encode('utf-8'), hashlib.sha256).hexdigest()
    return sign


def get_ping():
    """test connectivity"""
    method = 'GET'
    path = '/api/v3/ping'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_timestamp():
    """get sever time"""
    method = 'GET'
    path = '/api/v3/time'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_exchangeInfo():
    """get exchangeInfo"""
    method = 'GET'
    path = '/api/v3/exchangeInfo'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_depth(symbol, limit=None):
    """get order book"""
    method = 'GET'
    path = '/api/v3/depth'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    if limit:
        data.update({'limit': limit})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_trades(symbol, limit=None):
    """get current trades list"""
    method = 'GET'
    path = '/api/v3/trades'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    if limit:
        data.update({'limit': limit})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_historicalTrades(symbol, limit=None):
    """get old trades lookup"""
    method = 'GET'
    path = '/api/v3/trades'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    if limit:
        data.update({'limit': limit})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_aggtrades(symbol, start_time=None, end_time=None, limit=None):
    """get aggregate trades list"""
    method = 'GET'
    path = '/api/v3/aggTrades'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    if start_time:
        data.update({'start_time': start_time})
    if end_time:
        data.update({'end_time': end_time})
    if limit:
        data.update({'limit': limit})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_kline(symbol, interval, start_time=None, end_time=None, limit=None):
    """get k-line data"""
    method = 'GET'
    path = '/api/v3/klines'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
        'interval': interval,
    }
    if start_time:
        data.update({'start_time': start_time})
    if end_time:
        data.update({'end_time': end_time})
    if limit:
        data.update({'limit': limit})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_avgprice(symbol):
    """get current average prcie(default : 5m)"""
    method = 'GET'
    path = '/api/v3/avgPrice'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    response = requests.request(method, url, params=data)
    print(response.json())


def get_24hr_ticker(symbol):
    """get 24hr prcie ticker change statistics"""
    method = 'GET'
    path = '/api/v3/ticker/24hr'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    response = requests.request(method, url, params=data)
    print(response.json())


def get_price(symbol):
    """get symbol price ticker"""
    method = 'GET'
    path = '/api/v3/ticker/price'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    response = requests.request(method, url, params=data)
    print(response.json())


def get_bookticker(symbol=None):
    """get symbol order book ticker"""
    method = 'GET'
    path = '/api/v3/ticker/bookTicker'
    url = '{}{}'.format(BASE_URL, path)
    data = {}
    if symbol:
        data.update({'symbol': symbol})
    response = requests.request(method, url, params=data)
    print(response.json())


def post_order_test(symbol, side, order_type, quantity=None, quoteOrderQty=None, price=None, NewClient_order_id=None, recvWindow=None):
    """test new order"""
    method = 'POST'
    path = '/api/v3/order/test'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
        'side': side,
        'type': order_type,
    }
    if quantity:
        data.update({'quantity': quantity})
    if quoteOrderQty:
        data.update({'quoteOrderQty': quoteOrderQty})
    if price:
        data.update({'price': price})
    if NewClient_order_id:
        data.update({'client_order_id': NewClient_order_id})
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def post_order(symbol, side, order_type, quantity=None, quoteOrderQty=None, price=None, NewClient_order_id=None, recvWindow=None):
    """new order"""
    method = 'POST'
    path = '/api/v3/order'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
        'side': side,
        'type': order_type,
    }
    if quantity:
        data.update({'quantity': quantity})
    if quoteOrderQty:
        data.update({'quoteOrderQty': quoteOrderQty})
    if price:
        data.update({'price': price})
    if NewClient_order_id:
        data.update({'client_order_id': NewClient_order_id})
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def delete_order(symbol, orderId=None, origClientOrderId=None, newClientOrderId=None, recvWindow=None):
    """
    cancel order
    'origClientOrderId' or 'orderId' must be sent
    """
    method = 'DELETE'
    path = '/api/v3/order'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    if orderId:
        data.update({'orderId': orderId})
    if origClientOrderId:
        data.update({'origClientOrderId': origClientOrderId})
    if newClientOrderId:
        data.update({'newClientOrderId': newClientOrderId})
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def delete_openorders(symbol, recvWindow=None):
    """
    cancel all order for a single symbol
    'origClientOrderId' or 'orderId' must be sent
    """
    method = 'DELETE'
    path = '/api/v3/openOrders'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def get_order(symbol, orderId=None, origClientOrderId=None, recvWindow=None):
    """
    get order
    'origClientOrderId' or 'orderId' must be sent
    """
    method = 'GET'
    path = '/api/v3/order'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol
    }
    if orderId:
        data.update({'orderId': orderId})
    if origClientOrderId:
        data.update({'origClientOrderId': origClientOrderId})
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def get_openorders(symbol, recvWindow=None):
    """get current pending order """
    method = 'GET'
    path = '/api/v3/openOrders'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol
    }
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def get_allorders(symbol, orderId=None, startTime=None, endTime=None, limit=None, recvWindow=None):
    """
    get current all order
    startTime and endTime need to use at the same time
    """
    method = 'GET'
    path = '/api/v3/allOrders'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol
    }
    if orderId:
        data.update({'orderId': orderId})
    if startTime:
        data.update({'startTime': startTime})
    if endTime:
        data.update({'endTime': endTime})
    if limit:
        data.update({'limit': limit})
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def get_account_info():
    """get account information"""
    method = 'GET'
    path = '/api/v3/account'
    url = '{}{}'.format(BASE_URL, path)
    sign = _sign_v3()
    data = {
        'timestamp': _get_server_time(),
        'signature': sign,
    }
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())


def get_mytrades(symbol, orderId=None, startTime=None, endTime=None, limit=None, recvWindow=None):
    """
    get current all order
    orderId need to use with symbol at the same time
    """
    method = 'GET'
    path = '/api/v3/myTrades'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol
    }
    if orderId:
        data.update({'orderId': orderId})
    if startTime:
        data.update({'startTime': startTime})
    if endTime:
        data.update({'endTime': endTime})
    if limit:
        data.update({'limit': limit})
    if recvWindow:
        data.update({'recvWindow': recvWindow})
    sign = _sign_v3(sign_params=data)
    data['timestamp'] = _get_server_time()
    data['signature'] = sign
    headers = {
        'x-mexc-apikey': API_KEY,
        'Content-Type': 'application/json',
    }
    response = requests.request(method, url, params=data, headers=headers)
    print(response.json())
