from python.spot import mexc_spot_v3


wallet = mexc_spot_v3.mexc_wallet()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    # "startTime": "xxx",
    # "endTime": "xxx",
    # "page": "xxx",
    # "limit": "xxx",
    # "tranId": "xxx"
}
InternalTransferHistory = wallet.get_transfer_internal_list(params)
print(InternalTransferHistory)