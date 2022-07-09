
const Future = require('../../src/future')

const apiKey = ''
const apiSecret = ''
const client = new Future(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })

client.PlaceNewOrder('IMX_USDT',{
    price: 0.1,
    vol: 10,
    side:1,
    type:1,
    openType:2
  }).then(response => client.logger.log(response.data)) 
  