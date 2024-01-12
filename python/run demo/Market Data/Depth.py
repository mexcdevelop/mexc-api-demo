from python.spot import mexc_spot_v3


market = mexc_spot_v3.mexc_market()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"10"}
# If there are no parameters, input: ""
params = {
    "symbol": "BTCUSDT",
    "limit": "10"
}
Depth = market.get_depth(params)
print(Depth)