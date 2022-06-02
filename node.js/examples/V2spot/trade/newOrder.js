const Spot = require('../../../src/spot')

const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.newOrder('BNBUSDT', 'BUY', 'LIMIT', {
  price: '10',
  quantity: 1 
}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))