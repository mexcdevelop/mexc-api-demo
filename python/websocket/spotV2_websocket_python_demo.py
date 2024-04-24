import time
import json
import hashlib
from urllib.parse import urlencode
import websocket


BASE_URL = 'wss://wbs.mexc.com/raw/ws'
API_KEY = 'your access key'
SECRET_KEY = 'your secret key'


def on_message(ws, message):
    print(message)


def on_error(ws, error):
    print(error)


def on_close(ws):
    print("Connection closed ....")


def on_ping(ws, message):
    ws.send("ping")
    print(message)


def on_pong(ws, message):
    print(message)


def on_open(ws):
    """
    根據想訂閱信息，依照websocket文檔更改params中的內容，例如: "op"及"symbol"
    According to the information you want to subscribe,
    change the content of the params according to the websocket documentation,
    ex: "op" and "symbol"
    """
    params = {
        "api_key": API_KEY,
        "op": "sub.personal",
        #"symbol": "ETH_USDT",
        #"interval": "Min30",
        "req_time": int(time.time() * 1000),
        "api_secret": SECRET_KEY
    }
    params_sign = urlencode(params)
    print(params_sign)
    sign_data = hashlib.md5(params_sign.encode('utf-8')).hexdigest()
    del params['api_secret']
    params["sign"] = sign_data
    print(json.dumps(params))
    ws.send(json.dumps(params))


if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp(BASE_URL,
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close,
                                on_ping=on_ping,
                                on_pong=on_pong)
    ws.on_open = on_open
    ws.run_forever(ping_timeout=10)
