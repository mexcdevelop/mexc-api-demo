const Spot = require('../../../src/spot')

const ApiKey = ''
const apiSecret = ''
const client = new Spot(ApiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.PlaceOrder({order_type: 'LIMIT_ORDER',price:'0.1',quantity: '55',symbol: 'MX_USDT',trade_type: 'BID'})
.then(response => client.logger.log(response.data)) 
