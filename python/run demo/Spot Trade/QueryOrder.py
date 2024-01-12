from python.spot import mexc_spot_v3


trade = mexc_spot_v3.mexc_trade()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    "symbol": "MXUSDT",
    # "origClientOrderId": "xxx",
    "orderId": "C01__xxx",
}
QueryOrder = trade.get_order(params)
print(QueryOrder)