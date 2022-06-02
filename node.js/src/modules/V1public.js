const { validateRequiredParameters } = require('../helpers/validation')
const V1Public = superclass => class extends superclass {

  //获取服务器时间
servertime () {
    return this.publicRequest('GET', 'api/v1/contract/ping')
  }
 
  //获取合约信息
  ContractDetail () {
    return this.publicRequest('GET', 'api/v1/contract/detail')
  }
 
  //获取可划转币种
  SupporCurrencies () {
    return this.publicRequest('GET', 'api/v1/contract/support_currencies')
  }

  //获取合约深度信息
  DepthBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      'api/v1/contract/depth/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  
  //获取合约最近N条深度信息快照
  DepthcommitsBySymbol (symbol, limit, options = {}) {
    validateRequiredParameters({ symbol, limit })
    return this.publicRequest(
      'GET',
      'api/v1/contract/depth_commits/{symbol}/{limit}',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        limit: limit.toUpperCase()
      })
    )
  }
  
  //获取合约指数价格
  IndexPriceBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol })
    return this.publicRequest(
      'GET',
      'api/v1/contract/index_price/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  
  //获取合约合理价格
  FairPriceBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol})
    return this.publicRequest(
      'GET',
      'api/v1/contract/fair_price/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //获取合约资金费率
  FundingRateBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol})
    return this.publicRequest(
      'GET',
      'api/v1/contract/funding_rate/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //获取蜡烛图数据
  KlineBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol})
    return this.publicRequest(
      'GET',
      'api/v1/contract/kline/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //获取指数价格蜡烛图数据
  IndexPriceKlineBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol})
    return this.publicRequest(
      'GET',
      'api/v1/contract/kline/index_price/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }
  //获取合理价格蜡烛图数据
  FairPriceKlineBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol})
    return this.publicRequest(
      'GET',
      'api/v1/contract/kline/fair_price/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //获取成交数据
  DealsBySymbol (symbol, options = {}) {
    validateRequiredParameters({ symbol})
    return this.publicRequest(
      'GET',
      'api/v1/contract/deals/{symbol}',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })
    )
  }

  //获取合约行情数据
  Ticker () {
    return this.publicRequest('GET', 'api/v1/contract/ticker')
  }

  //获取所有合约风险基金余额
  RiskReverse () {
    return this.publicRequest('GET', 'api/v1/contract/risk_reverse')
  }

  //获取合约风险基金余额历史
  RiskReverseHistory (symbol, page_num, page_size = {}) {
    validateRequiredParameters({ symbol, page_num, page_size })
    return this.publicRequest(
      'GET',
      'api/v1/contract/risk_reverse/history',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        page_num: page_num.toUpperCase(),
        page_size: page_size.toUpperCase()
      })
    )
  }

  //获取合约资金费率历史
  FundingRateHistory (symbol, page_num, page_size = {}) {
    validateRequiredParameters({ symbol, page_num, page_size })
    return this.publicRequest(
      'GET',
      'api/v1/contract/funding_rate/history',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        page_num: page_num.toUpperCase(),
        page_size: page_size.toUpperCase()
      })
    )
  }         
}

module.exports = V1Public