const Future = require('../../src/future')

const apiKey = ''
const apiSecret = ''
const client = new Future(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })

client.Assets().then(response => client.logger.log(response.data)) 
  