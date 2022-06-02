const Future = require('../../../src/future')

const client = new Future()

client.depth('btcusdt', { limit: 5 }).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))