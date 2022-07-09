const Future = require('../../src/future')

const client = new Future()


client.Ticker('BTC_USDT').then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))