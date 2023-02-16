const Future = require('../../src/future')
const apiKey = ''
const apiSecret = ''
const client = new Future(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })



//Asset
client.Assets().then(response => client.logger.log(response.data))


//Contract Information
client.ContractDetail().then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

//Depth
client.DepthBySymbol().then(response => client.logger.log(JSON.stringify(response.data)))
  .catch(error => client.logger.error(error))

//Contractual funding rates
client.FundingRateHistory({
  symbol: 'BTC_USDT',
  page_num: 1,
  page_size: 1
}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

//Place an order
client.PlaceNewOrder({
  symbol: 'IMX_USDT',
  price: 0.1,
  vol: 10,
  side: 1,
  type: 1,
  openType: 2
}).then(response => client.logger.log(response.data))

//Batch orders
client.PlaceNewOrderBatch([{
  symbol: 'IMX_USDT',
  price: 0.1,
  vol: 10,
  side: 1,
  type: 1,
  openType: 2
},
{
  symbol: 'IMX_USDT',
  price: 0.2,
  vol: 10,
  side: 1,
  type: 1,
  openType: 2
}]).then(response => client.logger.log(response.data))

//Ticker
client.Ticker({ symbol: 'BTC_USDT' }).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

