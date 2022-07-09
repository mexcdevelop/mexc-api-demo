const Future = require('../../src/future')

const client = new Future()


client.FundingRateHistory('BTC_USDT',{
    page_num:1, 
    page_size:1
}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))