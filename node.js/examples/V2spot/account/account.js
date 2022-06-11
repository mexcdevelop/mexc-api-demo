const Spot = require('../../../src/spot')

const ApiKey = ''
const apiSecret = ''
const client = new Spot(ApiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.account().then(response => client.logger.log(response.data)) 
