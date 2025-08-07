from python.spot import mexc_spot_v3


rebate = mexc_spot_v3.mexc_rebate()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    # "startTime": "xxx",
    # "endTime": "xxx",
    # "page": "xxx"
}
Affiliate_Campaign = rebate.get_affiliate_campaign(params)
print(Affiliate_Campaign)