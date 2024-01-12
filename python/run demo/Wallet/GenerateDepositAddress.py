from python.spot import mexc_spot_v3


wallet = mexc_spot_v3.mexc_wallet()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    "coin": "USDT",
    "network": "Tron(TRC20)"
}
GenerateDepositAddress = wallet.post_deposit_address(params)
print(GenerateDepositAddress)