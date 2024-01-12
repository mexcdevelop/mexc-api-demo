from python.spot import mexc_spot_v3


market = mexc_spot_v3.mexc_market()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    "symbols": "ETHUSDT,BTCUSDT"
}
ExchangeInfo = market.get_exchangeInfo(params)
print(ExchangeInfo)