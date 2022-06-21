const V3Spot = require('../../../src/V3spot')

const apiKey = ''
const apiSecret = ''
const client = new V3Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.newOrder('BNBUSDT', 'BUY', 'LIMIT', {
  price: '10',
  quantity: 1 
}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))