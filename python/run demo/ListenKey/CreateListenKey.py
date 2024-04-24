from python.spot import mexc_spot_v3


listenkey = mexc_spot_v3.mexc_listenkey()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
CreateListenKey = listenkey.post_listenKey()
print(CreateListenKey)