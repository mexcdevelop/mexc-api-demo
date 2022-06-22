const V3Spot = require('../../../src/V3spot')

const apiKey = ''
const apiSecret = ''
const client = new V3Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.Order('MXUSDT', 'BUY', 'LIMIT', {
  price: '0.1',
  quantity: 100,
  timeInForce: 'GTC'
}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))
  