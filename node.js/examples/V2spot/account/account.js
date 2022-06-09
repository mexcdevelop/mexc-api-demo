const Spot = require('../../../src/spot')

const ApiKey = 'mx0CCNV0uAJaNGPZOI'
const apiSecret = '1fc3817aa2df461a91a9782e325d863f'
const client = new Spot(ApiKey, apiSecret, { baseURL: 'https://www.mexc.com' })

client.account().then(response => client.logger.log(response.data)) 
