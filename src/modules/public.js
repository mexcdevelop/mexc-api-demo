const { validateRequiredParameters } = require('../helpers/validation')
const Public = superclass => class extends superclass {
 //所有交易对信息
 symbols () {
    return this.publicRequest('GET', '/open/api/v2/market/symbols')
  }
 
  //当前系统时间
servertime () {
    return this.publicRequest('GET', '/open/api/v2/common/timestamp')
  }
 
  //ping
  ping () {
    return this.publicRequest('GET', '/open/api/v2/common/ping')
  }
 
  //获取平台支持接口的交易对
  api_default_symbols (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/open/api/v2/market/ticker',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //Ticker行情
  Ticker (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/open/api/v2/market/ticker',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  
  //深度信息
  Depth (symbol, depth, options = {}) {
    validateRequiredParameters({ symbol, depth })
    return this.publicRequest(
      'GET',
      '/open/api/v2/market/depth',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        depth: depth.toUpperCase()
      })
    )
  }
  
  //成交记录
  Deals (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/open/api/v2/market/deals',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  
  //K线数据
  Kline (symbol, interval, options = {}) {
    validateRequiredParameters({ symbol, interval})
    return this.publicRequest(
      'GET',
      '/open/api/v2/market/kline',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        interval: interval.toUpperCase()
      })
    )
  }

  //获取币种信息
  CoinList () {
    return this.publicRequest('GET', '/open/api/v2/market/coin/list')
  }

}

module.exports = Public