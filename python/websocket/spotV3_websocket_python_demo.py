import json
import hashlib
from urllib.parse import urlencode
import websocket
from python.spot.v3 import mexc_spot_v3

BASE_URL = 'wss://wbs.mexc.com/ws'
hosts = 'https://api.mexc.com'
mexc_key = "your apiKey"
mexc_secret = "your secretKey"

listenKey = mexc_spot_v3.mexc_listenkey(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)

""" 建立 ListenKey """
""" generate ListenKey """
ListenKey = listenKey.post_listenKey()['listenKey']

""" 延長 ListenKey 有效期 """
""" Extend ListenKey validity """
# params = {'listenKey': ListenKey}
# print(listenKey.put_listenKey(params))

""" 刪除 ListneKey """
""" Delete ListenKey """
# params = {'listenKey': ListenKey}
# print(listenKey.delete_listenKey(params))

BASE_URL = 'wss://wbs.mexc.com/ws' + '?listenKey=' + ListenKey

def on_message(ws, message):
    print(message)


def on_error(ws, error):
    print('Connection error ....')
    print(error)


def on_close(ws):
    print("Connection closed ....")


def on_ping(ws, message):
    params = {"method": "PING"}
    ws.send(json.dumps(params))
    print(message)


def on_pong(ws, message):
    print(message)


def on_open(ws):
    """
    根據想訂閱信息, 依照websocket文檔更改"method"及"params"中的內容
    According to the information you want to subscribe,
    change the content of the "method" and "params" according to the websocket documentation,
    """
    params = {
        "method": "SUBSCRIPTION",
        "params":[
            #"spot@public.deals.v3.api@ADAUSDT",
            "spot@public.kline.v3.api@ADAUSDT@Min5",
            #"spot@public.increase.depth.v3.api@ADAUSDT"
            "spot@private.orders.v3.api",
            "spot@private.deals.v3.api"
        ]
    }
    params_sign = urlencode(params)
    print(params_sign)
    print(json.dumps(params))
    ws.send(json.dumps(params))
    print('Opened ....')


if __name__ == "__main__":
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp(BASE_URL,
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close,
                                on_ping=on_ping,
                                on_pong=on_pong)
    ws.on_open = on_open
    ws.run_forever(ping_interval=30,
                    ping_timeout=10)
