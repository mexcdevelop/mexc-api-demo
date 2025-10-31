const Spot = require('../../src/spot')
const client = new Spot('', '', { baseURL: 'https://api.mexc.com' })

// 深度数据需要symbol参数
client.Depth({symbol: 'BTCUSDT'}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))
