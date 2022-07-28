const Spot = require('../../src/spot')
const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

//Account
client.AccountInformation().then(response => client.logger.log(response.data)) 

//Depth
client.Depth({symbol:'BTCUSDT',depth:5}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

//VirtualApikey
client.VirtualApikey({subAccount:'subAccount123',note:'123',permissions:'SPOT_ACCOUNT_READ,SPOT_TRANSFER_READ'})
.then(response => client.logger.log(response.data)) 

////Place an order
client.PlaceOrder({order_type: "LIMIT_ORDER",price: 0.2, quantity: 25, symbol: "MX_USDT",trade_type: "BID"})
.then(response => client.logger.log(response.data)) 