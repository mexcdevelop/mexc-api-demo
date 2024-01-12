from python.spot import mexc_spot_v3


wallet = mexc_spot_v3.mexc_wallet()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    "coin": "USDT",
    "network": "Tron(TRC20)",
    "address": "xxx",
    "amount": "1000",
    # "memo": "xxx",
    # "withdrawOrderId": "xxx",
    # "remark": "xxx",
}
Withdraw = wallet.post_withdraw(params)
print(Withdraw)