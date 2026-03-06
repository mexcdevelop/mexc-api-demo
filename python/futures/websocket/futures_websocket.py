import time
import json
import hashlib
import hmac
from urllib.parse import urlencode
import threading
import websocket

BASE_URL = 'wss://contract.mexc.co/edge'
API_KEY = 'your apikey'
SECRET_KEY = 'your secretkey'


def on_message(ws, message):
    print(message)


def on_error(ws, error):
    print('connection error ....')
    print(error)


def on_close(ws, close_status_code, close_msg):
    print(f"Connection closed with status code: {close_status_code}, message: {close_msg}")


def on_open(ws):
    """
    根據想訂閱信息，依照websocket文檔更改params中的內容，例如: "method"及"param"
    According to the information you want to subscribe,
    change the content of the params according to the websocket documentation,
    ex: "method" and "param"
    """
    params_sign = API_KEY + str(int(time.time()*1000))
    sign = hmac.new(SECRET_KEY.encode('utf-8'), params_sign.encode('utf-8'),
                      hashlib.sha256).hexdigest()
    params = {
        "method": "sub.ticker",
        "param":{
           "symbol":"BTC_USDT"
        }
    }
    print(json.dumps(params))
    ws.send(json.dumps(params))

    # # params = {
    # # "method":"personal.filter",
    # # "param":{
    # #     "filters":[
    # #         {
    # #             "filter":"asset"
    # #         }
    # #     ]
    # #     }
    # # }
    # print(json.dumps(params))
    # ws.send(json.dumps(params))

def send_ping_periodically(ws):
    while True:
        time.sleep(10)
        ping_message = {"method": "ping"}
        ws.send(json.dumps(ping_message))

if __name__ == "__main__":
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp(BASE_URL,
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close
                                )
    ws.on_open = on_open
    timer_thread = threading.Thread(target=send_ping_periodically, args=(ws,))
    timer_thread.daemon = True
    timer_thread.start()
    ws.run_forever()