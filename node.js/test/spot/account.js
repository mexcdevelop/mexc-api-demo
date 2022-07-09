const Spot = require('../../src/spot')

const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.AccountInformation().then(response => client.logger.log(response.data)) 