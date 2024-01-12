from python.spot import mexc_spot_v3


market = mexc_spot_v3.mexc_market()

servertime = market.get_timestamp()
print(servertime)