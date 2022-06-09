const V3Spot = require('../../../src/v3spot')

const apiKey = ''
const apiSecret = ''
const client = new V3Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.AccountInformation('timestamp').then(response => client.logger.log(response.data)) 