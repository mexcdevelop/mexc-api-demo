const Spot = require('../../../src/spot')

const ApiKey = 'mx01QndahNw5sZUvbR'
const apiSecret = 'fb74993960a848969dcf270942b8d4f4'
const client = new Spot(ApiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.PlaceOrder({order_type: 'LIMIT_ORDER',price: 0.2,quantity: 100,symbol: 'MX_USDT',trade_type: 'BID'})
.then(response => client.logger.log(response.data)) 
