const Spot = require('../../../src/spot')
//const V1Public = require('../../../src/modules/V1public')
const client = new Spot()

client.depth('btcusdt', { limit: 5 }).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))