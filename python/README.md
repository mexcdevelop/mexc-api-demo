# Mexc Python Demo

Before you use the demo, you need to generate your apikey & apisecret, then enter them first.

* <https://www.mexc.com/user/openapi>

## Prerequisites

This project requires `uv` for Python environment management. Install it first:

```bash
# Install uv (if not already installed)
curl -LsSf https://astral.sh/uv/install.sh | sh
# or
pip install uv
```

## Setup

1. **Create virtual environment and install dependencies:**

```bash
cd python
uv venv
source .venv/bin/activate  # On Windows: .venv\Scripts\activate
uv pip install requests
```

2. **Configure API credentials:**

Fill in your API key and Secret key in `python/spot/config.py`

```python
mexc_host = "https://api.mexc.com"  # or test environment URL
api_key = "your apikey"
secret_key = "your secretkey"
```

## Spot V3 Demo

Fill in the corresponding function according to the parameters mentioned in the API documentation and execute it. => `print()`

**Rest API V3 doc**   `URL = 'https://api.mexc.com'`

* <https://mexcdevelop.github.io/apidocs/spot_v3_en/#introduction>

## Running Examples

### Method 1: Using uv run (Recommended)

```bash
cd python
PYTHONPATH=.. uv run "run demo/Market Data/ExchangeInfo.py"
```

### Method 2: Using uv with activated environment

```bash
cd python
source .venv/bin/activate
PYTHONPATH=.. python "run demo/Market Data/ExchangeInfo.py"
```

### Method 3: From project root

```bash
# From project root directory
PYTHONPATH=. python3 "python/run demo/Market Data/ExchangeInfo.py"
```

> ### Example(Spot V3) :

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

## Notes

- Some APIs (like `get_avgprice`) require signature authentication even in test environments
- The virtual environment (.venv) and Python cache files (__pycache__, *.pyc) are ignored by git
- Make sure to activate the virtual environment before running any scripts

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
