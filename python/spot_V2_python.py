import time
import json
import requests
import hmac
import hashlib

BASE_URL = 'https://www.mexc.com'
API_KEY = 'your access key'
SECRET_KEY = 'your serect key'


def _get_server_time():
    return int(time.time()*1000)


def _sign_v2(sign_params=None):
    params = (API_KEY, _get_server_time())
    if sign_params:
        params = "%s%s%s" % (API_KEY, _get_server_time(), sign_params)
    else:
        params = "%s%s" % (API_KEY, _get_server_time())
    params = hmac.new(SECRET_KEY.encode(), params.encode(),
                      hashlib.sha256).hexdigest()
    return params


def get_symbols():
    """get market data"""
    method = 'GET'
    path = '/open/api/v2/market/symbols'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_timestamp():
    """get current timestamp"""
    method = 'GET'
    path = '/open/api/v2/common/timestamp'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_ping():
    """get ping"""
    method = 'GET'
    path = '/open/api/v2/common/ping'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_api_default_symbols():
    """get api default symbols"""
    method = 'GET'
    path = '/open/api/v2/market/api_default_symbols'
    url = '{}{}'.format(BASE_URL, path)
    response = requests.request(method, url)
    print(response.json())


def get_ticker(symbol):
    """get symbol ticker information"""
    method = 'GET'
    path = '/open/api/v2/market/ticker'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
    }
    response = requests.request(method, url, params=data)
    print(response.json())


def get_depth(symbol, depth):
    """get symbol depth"""
    method = 'GET'
    path = '/open/api/v2/market/depth'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
        'depth': depth,
    }
    response = requests.request(method, url, params=data)
    print(response.json())


def get_deals(symbol, limit=None):
    """get deals records"""
    method = 'GET'
    path = '/open/api/v2/market/deals'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
        'limit': limit,
    }
    response = requests.request(method, url, params=data)
    print(response.json())


def get_kline(symbol, interval, start_time=None, limit=None):
    """get k-line data"""
    method = 'GET'
    path = '/open/api/v2/market/kline'
    url = '{}{}'.format(BASE_URL, path)
    data = {
        'symbol': symbol,
        'interval': interval,
    }
    if start_time:
        data.update({'start_time': start_time})
    if limit:
        data.update({'limit': limit})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_currency_info(currency=None):
    """get currency information"""
    method = 'GET'
    path = '/open/api/v2/market/coin/list'
    url = '{}{}'.format(BASE_URL, path)
    data = {}
    if currency:
        data.update({'currency': currency})
    response = requests.request(method, url, params=data)
    print(response.json())


def get_account_info():
    """get account information"""
    method = 'GET'
    path = '/open/api/v2/account/info'
    url = '{}{}'.format(BASE_URL, path)
    params = _sign_v2()
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_account_symbols():
    """get symbols which account can trade"""
    method = 'GET'
    path = '/open/api/v2/market/api_symbols'
    url = '{}{}'.format(BASE_URL, path)
    params = _sign_v2()
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(method, url, headers=headers)
    print(response.json())


def post_place(symbol, price, quantity, trade_type, order_type, client_order_id=None):
    """place order"""
    method = 'POST'
    path = '/open/api/v2/order/place'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        'symbol': symbol,
        'price': price,
        'quantity': quantity,
        'trade_type': trade_type,
        'order_type': order_type,
    }
    if client_order_id:
        data_orignal.update({"client_order_id": client_order_id})
    data = json.dumps(data_orignal)
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(
        method, url, data=data, headers=headers)
    print(response.json())


def delete_cancel(order_ids=None, client_order_ids=None):
    """cancel order"""
    method = 'DELETE'
    path = '/open/api/v2/order/cancel'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {}
    if order_ids:
        data_orignal.update({'order_ids':order_ids})
    if client_order_ids:
        data_orignal.update({'client_order_ids':client_order_ids})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def post_place_batch(order_data):
    """batch place oder"""
    method = 'POST'
    path = '/open/api/v2/order/place_batch'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = order_data
    data = json.dumps(data_orignal)
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(
        method, url, data=data, headers=headers)
    print(response.json())


def get_open_orders(symbol, trade_type=None, start_time=None, limit=None):
    """get current order"""
    method = 'GET'
    path = '/open/api/v2/order/open_orders'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "symbol": symbol,
    }
    if start_time:
        data_orignal.update({"start_time": start_time})
    if trade_type:
        data_orignal.update({"trade_type": trade_type})
    if limit:
        data_orignal.update({"limit": limit})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_order_list(symbol, trade_type, states, start_time=None, limit=None):
    """get order list"""
    method = 'GET'
    path = '/open/api/v2/order/list'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "symbol": symbol,
        "trade_type": trade_type,
        "states": states,
    }
    if start_time:
        data_orignal.update({"start_time": start_time})
    if limit:
        data_orignal.update({"limit": limit})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_query_order(order_ids):
    """get order query"""
    method = 'GET'
    path = '/open/api/v2/order/query'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "order_ids": order_ids,
    }

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_deals(symbol, limit=None, start_time=None):
    """get order deals"""
    method = 'GET'
    path = '/open/api/v2/order/deals'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "symbol": symbol,
    }
    if start_time:
        data_orignal.update({"start_time": start_time})
    if limit:
        data_orignal.update({"limit": limit})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_deal_detail(order_id):
    """get order deal detail"""
    method = 'GET'
    path = '/open/api/v2/order/deal_detail'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "order_id": order_id,
    }

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def delete_cancel_symbol(symbol):
    """cancel order"""
    method = 'DELETE'
    path = '/open/api/v2/order/cancel_by_symbol'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "symbol": symbol,
    }

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_deposit_address(currency):
    """deposit address list"""
    method = 'GET'
    path = '/open/api/v2/asset/deposit/address/list'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "currency": currency,
    }
    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(
        method, url, headers=headers)
    print(response.json())


def get_deposit_list(currency=None, startTime=None, endTime=None, pageSize=None, pagenum=None, state=None):
    """deposit history list"""
    method = 'GET'
    path = '/open/api/v2/asset/deposit/list'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {}
    if currency:
        data_orignal.update({"currency": currency})
    if pageSize:
        data_orignal.update({"page_size": pageSize})
    if pagenum:
        data_orignal.update({"page_unm": pagenum})
    if startTime:
        data_orignal.update({"start_time": startTime})
    if endTime:
        data_orignal.update({"end_time": endTime})
    if state:
        data_orignal.update({"state": state})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(
        method, url, headers=headers)
    print(response.json())


def get_address_list(currency=None, pageSize=None, pagenum=None):
    """withdraw address list"""
    method = 'GET'
    path = '/open/api/v2/asset/address/list'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {}
    if currency:
        data_orignal.update({"currency": currency})
    if pageSize:
        data_orignal.update({"page_size": pageSize})
    if pagenum:
        data_orignal.update({"page_unm": pagenum})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(
        method, url, headers=headers)
    print(response.json())


def post_withdraw(currency, amount, address, chain=None, remark=None):
    """withdraw"""
    method = 'POST'
    path = '/open/api/v2/asset/withdraw'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "currency": currency,
        "amount": amount,
        "address": address,
    }
    if chain:
        data_orignal.update({"chain": chain})
    if remark:
        data_orignal.update({"remark": remark})
    data = json.dumps(data_orignal)
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(
        method, url, data=data, headers=headers)
    print(response.json())


def delete_cancel(withdraw_id):
    """cancel order"""
    method = 'DELETE'
    path = '/open/api/v2/asset/withdraw'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "withdraw_id": withdraw_id,
    }

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(method, url, headers=headers)
    print(response.json())


def get_withdraw_list(currency=None, withdraw_id=None, startTime=None, endTime=None, pageSize=None, pagenum=None, state=None):
    """withdraw history list"""
    method = 'GET'
    path = '/open/api/v2/asset/withdraw/list'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {}
    if currency:
        data_orignal.update({"currency": currency})
    if withdraw_id:
        data_orignal.update({"withdraw_id": withdraw_id})
    if pageSize:
        data_orignal.update({"page_size": pageSize})
    if pagenum:
        data_orignal.update({"page_unm": pagenum})
    if startTime:
        data_orignal.update({"start_time": startTime})
    if endTime:
        data_orignal.update({"end_time": endTime})
    if state:
        data_orignal.update({"state": state})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(
        method, url, headers=headers)
    print(response.json())


def post_transfer(currency, amount, from_where, to_where):
    """internal transfer"""
    method = 'POST'
    path = '/open/api/v2/asset/internal/transfer'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "currency": currency,
        "amount": amount,
        "from": from_where,
        "to": to_where,
    }
    data = json.dumps(data_orignal)
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(
        method, url, data=data, headers=headers)
    print(response.json())


def get_transfer_list():
    """transfer history list"""
    method = 'GET'
    path = '/open/api/v2/asset/internal/transfer/record'
    url = '{}{}'.format(BASE_URL, path)
    params = _sign_v2()
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    response = requests.request(
        method, url, headers=headers)
    print(response.json())


def get_account_blance(currency, account_type=None, sub_uid=None):
    """account balance can be transfer"""
    method = 'GET'
    path = '/open/api/v2/account/balance'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "currency": currency,
    }
    if account_type:
        data_orignal.update({"account_type": account_type})
    if sub_uid:
        data_orignal.update({"sub_uid": sub_uid})

    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(
        method, url, headers=headers)
    print(response.json())


def get_transact_id(transact_id):
    """get internal transfer order"""
    method = 'GET'
    path = '/open/api/v2/asset/internal/transfer/info'
    url = '{}{}'.format(BASE_URL, path)
    data_orignal = {
        "transact_id": transact_id,
    }
    data = '&'.join('{}={}'.format(
        i, data_orignal[i]) for i in sorted(data_orignal))
    params = _sign_v2(sign_params=data)
    headers = {
        "ApiKey": API_KEY,
        "Request-Time": str(_get_server_time()),
        "Signature": params,
        "Content-Type": "application/json"
    }
    url = "%s%s%s" % (url, "?", data)
    response = requests.request(
        method, url, headers=headers)
    print(response.json())
