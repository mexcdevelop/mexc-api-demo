# Mexc Python Demo

Before you use the demo, you need to generate your apikey & apisecret, then enter them first.

* <https://www.mexc.com/user/openapi>

## Spot V3 Demo

Fill in the corresponding function according to the parameters mentioned in the API documentation and execute it. => `print()`


**Rest API V3 doc**   `URL = 'https://api.mexc.com'`

* <https://mexcdevelop.github.io/apidocs/spot_v3_en/#introduction>


> ### Example(Spot V3) :

Fill in your API key and Secret key in " python/spot/config.py "

```python
mexc_host = "https://api.mexc.com"
api_key = "your apikey"
secret_key = "your secretkey"
```

Select the corresponding file in the " python/run demo " folder to execute

Example " python/run demo/Market Data/ExchangeInfo.py "

```python
from python.spot import mexc_spot_v3

market = mexc_spot_v3.mexc_market()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    "symbol": "BTCUSDT"
}
ExchangeInfo = market.get_exchangeInfo(params)
print(ExchangeInfo)
```

## Spot Websocket Demo

According to the information you want to subscribe, change the content of the params according to the websocket documentation.   Execute the entire python file after adjusting the parameters.

**WebSocket doc**   `URL = 'wss://wbs-api.mexc.com/ws'`

* <https://mexcdevelop.github.io/apidocs/spot_v3_en/#websocket-market-streams>


> ### Example(Spot WebSocket) :
```python
import json
import websocket

BASE_URL = 'wss://wbs-api.mexc.com/ws'

def on_message(ws, message):
    print(message)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("Connection closed ....")

def on_open(ws):
    subscribe_message = {
            "method": "SUBSCRIPTION",
            "params": [
                "spot@public.aggre.deals.v3.api.pb@10ms@BTCUSDT",
                "spot@public.aggre.deals.v3.api.pb@10ms@ETHUSDT"
            ]
        }
    ws.send(json.dumps(subscribe_message))
    logger.info(f"Sent subscription message: {subscribe_message}")


if __name__ == "__main__":
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp(BASE_URL,
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close,
                                )
    ws.on_open = on_open
    ws.run_forever()

```
