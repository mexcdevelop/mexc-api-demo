from python.spot import mexc_spot_v3


market = mexc_spot_v3.mexc_market()

ApiSymbol = market.get_defaultSymbols()
print(ApiSymbol)