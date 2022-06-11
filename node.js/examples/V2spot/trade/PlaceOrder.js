const Spot = require('../../../src/spot')

const ApiKey = 'mx01QndahNw5sZUvbR'
const apiSecret = 'e96c46331b764613b4f2465508222cbe'
const client = new Spot(ApiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.PlaceOrder({"order_type": "LIMIT_ORDER","price": "0.1","quantity": "100","symbol": "MX_USDT","trade_type": "BID"})
.then(response => client.logger.log(response.data)) 
