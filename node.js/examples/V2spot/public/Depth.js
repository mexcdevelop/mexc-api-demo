const Spot = require('../../../src/spot')

const client = new Spot()

client.Depth({"depth":'5',"symbol":"BTC_USDT"}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))