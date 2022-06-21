
const V3Spot = require('../../../src/v3spot')

const client = new V3Spot()

client.depth('btcusdt').then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))