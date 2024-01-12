from python.spot import mexc_spot_v3


trade = mexc_spot_v3.mexc_trade()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = [{
    "symbol": "MXUSDT",
    "side": "BUY",
    "type": "LIMIT_MAKER",
    "quantity": 10,
    "price": "0.5"
    }, {
    "symbol": "MXUSDT",
    "side": "BUY",
    "type": "LIMIT_MAKER",
    "quantity": 10,
    "price": "0.6"
    }]
BatchOrder = trade.post_batchorders(params)
print(BatchOrder)