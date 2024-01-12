from python.spot import mexc_spot_v3


subaccount = mexc_spot_v3.mexc_subaccount()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    "subAccount": "xxx",
    "note": "xxx"
}
CreateSubAccount = subaccount.post_virtualSubAccount(params)
print(CreateSubAccount)