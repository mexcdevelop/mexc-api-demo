const Spot = require('../../../src/spot')

const client = new Spot()

client.Depth("BTC_USDT",'5').then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))