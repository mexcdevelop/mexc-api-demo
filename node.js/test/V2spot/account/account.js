const Spot = require('../../../src/spot')

const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.account().then(response => client.logger.log(response.data)) 
