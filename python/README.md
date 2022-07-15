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
import mexc_spot_v3
import time

hosts = "https://api.mexc.com"
mexc_key = "your apiKey"
mexc_secret = "your secretKey"

# Market Data
"""get kline"""
data = mexc_spot_v3.mexc_market(mexc_hosts=hosts)
response= data.get_kline({'symbol': 'BTCUSDT', 'interval': '5m', 'limit': 10})
print(k_line_data)
```

## Spot Websocket Demo 

According to the information you want to subscribe, change the content of the params according to the websocket documentation, ex: "op" or "symbol".   Execute the entire python file after adjusting the parameters.

**WebSocket doc**   `URL = 'wss://wbs.mexc.com/raw/ws'`

* <https://mxcdevelop.github.io/apidocs/spot_v2_cn/#websocket-api>


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
