# Mexc Python Demo

Before you use the demo, you need to generate your apikey & apisecret, then enter them first.

* <https://www.mexc.com/user/openapi>

## Spot V2、V3 Demo 

Fill in the corresponding function according to the parameters mentioned in the API documentation and execute it. => `print()`

**Rest API V2 doc**   `URL = 'https://www.mexc.com'`

* <https://mxcdevelop.github.io/apidocs/spot_v2_cn/#5ea2e0cde2>

**Rest API V3 doc**   `URL = 'https://api.mexc.com'`

* <https://mxcdevelop.github.io/apidocs/spot_v3_cn/#45fa4e00db>


> ### Example(Spot V3) :

```python
import json
import requests

BASE_URL = 'https://api.mexc.com'

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

print(get_kline('ETHUSDT', interval='5m'))
```

## Spot Websocket Demo 

According to the information you want to subscribe, change the content of the params according to the websocket documentation, ex: "op" or "symbol".   Execute the entire python file after adjusting the parameters.

**WebSocket doc**   `URL = 'wss://wbs.mexc.com/raw/ws'`

* <https://github.com/mxcdevelop/APIDoc/blob/master/websocket/spot/websocket.2022_2_28.md>


> ### Example(Spot WebSocket) :
```python
import json
import websocket

BASE_URL = 'wss://wbs.mexc.com/raw/ws'

def on_message(ws, message):
    print(message)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("Connection closed ....")

def on_open(ws):
    params = {        
        "op": "sub.symbol",
        "symbol": "ETH_USDT",       
    }    
    print(json.dumps(params))    
    ws.send(json.dumps(params))


if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp(BASE_URL,
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close,
                                )
    ws.on_open = on_open
    ws.run_forever(ping_timeout=10)

```
