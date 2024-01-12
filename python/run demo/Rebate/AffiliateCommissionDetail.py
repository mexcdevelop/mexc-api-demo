from python.spot import mexc_spot_v3


rebate = mexc_spot_v3.mexc_rebate()

# Enter parameters in JSON format in the "params", for example: {"symbol":"BTCUSDT", "limit":"200"}
# If there are no parameters, no need to send params
params = {
    # "startTime": "xxx",
    # "endTime": "xxx",
    # "inviteCode": "xxx",
    # "page": "xxx",
    # "pageSize": "xxx",
    # "type": "1"
}
AffiliateCommissionDetail = rebate.get_affiliate_commission_detail(params)
print(AffiliateCommissionDetail)