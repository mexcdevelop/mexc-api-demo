const V3Spot = require('../../../src/V3spot')

const apiKey = 'xxx'
const apiSecret = 'xxx'
const client = new V3Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.account().then(response => client.logger.log(response.data)) 