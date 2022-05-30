const Spot = require('../../../src/spot')

const apiKey = 'xxx'
const apiSecret = 'xxx'
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.account().then(response => client.logger.log(response.data)) 