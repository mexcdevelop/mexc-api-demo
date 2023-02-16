const Spot = require('../src/spot')
const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://www.mexc.com' })



//Place an order
client.placeOrder({ "order_type": "LIMIT_ORDER", "price": "XX", "quantity": "XX", "symbol": "MX_USDT", "trade_type": "BID" })
  .then(response => client.logger.log(response.data)) 