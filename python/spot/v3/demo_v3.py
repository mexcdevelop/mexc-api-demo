import mexc_spot_v3

hosts = "https://api.mexc.com"
mexc_key = "your apiKey"
mexc_secret = "your secretKey"

# Market Data
"""get kline"""
data = mexc_spot_v3.mexc_market(mexc_hosts=hosts)
params = {
    'symbol': 'BTCUSDT',
    'interval': '5m',
    'limit': 10
}
response = data.get_kline(params)
print(response)


# Spot Trade
"""place an order"""
trade = mexc_spot_v3.mexc_trade(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)
params = {
    "symbol": "BTCUSDT",
    "side": "BUY",
    "type": "LIMIT",
    "quantity": 0.005,
    "price": "10000"
}
response = trade.post_order(params)
print(response)


# Spot Account
"""get spot account information"""
account = mexc_spot_v3.mexc_account(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)
response = account.get_account_info()
print(response)


# Capital
"""get currency information"""
capital = mexc_spot_v3.mexc_capital(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)
response = capital.get_coinlist()
print(response)


# Sub-Account
"""get sub account information"""
sub_account = mexc_spot_v3.mexc_subaccount(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)
response = sub_account.get_SubAccountList()
print(response)


# Margin Account & Trade
"""get loan history"""
margin = mexc_spot_v3.mexc_margin(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)
params = {
    "asset": "USDT",
    "symbol": "BTCUSDT",
}
response = margin.get_loan_history(params)
print(response)


# Rebate
"""get rebate record details"""
margin = mexc_spot_v3.mexc_rebate(mexc_key=mexc_key, mexc_secret=mexc_secret, mexc_hosts=hosts)

response = margin.get_rebate_detail()
print(response)