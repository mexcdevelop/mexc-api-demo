const Spot = require('../../src/spot')
const client = new Spot( '', '', { baseURL: 'https://api.mexc.com' })

// 平均价格API需要symbol参数
client.CurrentAveragePrice({symbol: 'BTCUSDT'}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))
