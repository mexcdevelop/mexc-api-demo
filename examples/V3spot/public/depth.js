
const V3Spot = require('../../../src/V3spot')

const client = new V3Spot()

client.depth('btcusdt', { limit: 5 }).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))