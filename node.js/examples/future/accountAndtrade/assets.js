const Future = require('../../../src/future')

const apiKey = 'mx0ArMnVRMwyLgXbZS'
const apiSecret = 'fb74993960a848969dcf270942b8d4f4'
const client = new Future(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })

client.Assets().then(response => client.logger.log(response.data)) 
  