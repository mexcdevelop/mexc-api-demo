const { validateRequiredParameters } = require('../helpers/validation')
const V3Public = superclass => class extends superclass {
 //测试服务的连通性
 TestConnectivity () {
    return this.publicRequest('GET', '/api/v3/ping')
  }
 
  //获取服务器时间
 servertime () {
    return this.publicRequest('GET', '/api/v3/time')
  }
 
  //交易规范信息
  ExchangeInformation () {
    return this.publicRequest('GET', '/api/v3/exchangeInfo')
  }
 
  //深度信息
  depth (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/api/v3/depth',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //近期成交列表
  RecentTradesList (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/api/v3/trades',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  
  //历史成交列表
  OldTradeLookup (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/api/v3/historicalTrades',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  
  //近期成交（归集）
  CompressedTradesList (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/api/v3/aggTrades',
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
      '/api/v3/klines',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        interval: interval.toUpperCase()
      })
    )
  }

  //当前平均价格
  CurrentAveragePrice (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      '/api/v3/avgPrice',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //24小时价格滚动情况
  TickerPriceChange () {
    return this.publicRequest('GET', '/api/v3/ticker/24hr')
  }  

  //最新价格
  SymbolPriceTicker () {
    return this.publicRequest('GET', '/api/v3/ticker/price')
  }

  //当前最优挂单
  SymbolOrderBook () {
    return this.publicRequest('GET', '/api/v3/ticker/bookTicker')
  }
}

module.exports = V3Public