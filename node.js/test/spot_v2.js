const Spot = require('../../src/spot')
const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

//API trading support
client.defaultSymbols().then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

//Place an order
client.placeOrder({"order_type": "LIMIT_ORDER","price": "0.2","quantity": "25","symbol": "MX_USDT","trade_type": "BID"})
.then(response => client.logger.log(response.data)) 