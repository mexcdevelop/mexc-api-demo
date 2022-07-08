
const Spot = require('../../src/spot')

const client = new Spot()

client.depth('btcusdt').then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))