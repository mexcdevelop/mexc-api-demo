const { validateRequiredParameters } = require('../helpers/validation')
const Spot = superclass => class extends superclass {
  
/**
 * @V2
 */
   //所有交易对信息
 symbols () {
  return this.publicRequest('GET', 'https://www.mexc.com/open/api/v2/market/symbols')
}

//当前系统时间
servertime () {
  return this.publicRequest('GET', 'https://www.mexc.com/open/api/v2/common/timestamp')
}

//ping
ping () {
  return this.publicRequest('GET', 'https://www.mexc.com/open/api/v2/common/ping')
}

//获取平台支持接口的交易对
api_default_symbols (symbol, options = {}) {
  validateRequiredParameters({ symbol })
  return this.publicRequest(
    'GET',
    'https://www.mexc.com/open/api/v2/market/ticker',
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
    'https://www.mexc.com/open/api/v2/market/ticker',
    Object.assign(options, {
      symbol: symbol.toUpperCase()
    })
  )
}

//深度信息
Depth (symbol, depth, options = {}) {
  validateRequiredParameters({ symbol,depth})
  return this.publicRequest(
    'GET',
    'https://www.mexc.com/open/api/v2/market/depth',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
      depth: depth.toUpperCase(),
    })
  )
}


//成交记录
Deals (symbol, options = {}) {
  validateRequiredParameters({ symbol })
  return this.publicRequest(
    'GET',
    'https://www.mexc.com/open/api/v2/market/deals',
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
    'https://www.mexc.com/open/api/v2/market/kline',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
      interval: interval.toUpperCase()
    })
  )
}

//获取币种信息
CoinList () {
  return this.publicRequest('GET', 'https://www.mexc.com/open/api/v2/market/coin/list')
}

  //账户余额
  account (options = {}) {
    return this.SignRequest(
      'GET',
      '/open/api/v2/account/info',
      options
    )
  }

 //获取账户可接口交易的交易对
  apiAccount (options = {}) {
  return this.SignRequest(
    'GET',
    '/open/api/v2/market/api_symbols',
    options,
  )
}

//下单
PlaceOrder (options = {}) {
  return this.SignRequest(
    'POST',
    '/open/api/v2/order/place',
    options,
    
  )
}

//撤销订单
CancelOrder ( options = {}) {
  return this.SignRequest(
    'DELETE',
    '/open/api/v2/order/cancel',
     options   
  )
}

//批量下单
MuiltPlaceOrder (options = {}) {
  return this.SignRequest(
    'POST',
    '/open/api/v2/order/place_batch',
    options
  )
}

//当前挂单
GetOpenOrder (symbol, options = {}) {
  validateRequiredParameters({ symbol})
  return this.SignRequest(
    'GET',
    '/open/api/v2/order/open_orders',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
    })
  )
}

//所有订单
GetAllOrder (symbol, trade_type	, states, options = {}) {
  validateRequiredParameters({ symbol, trade_type	, states })
  return this.SignRequest(
    'GET',
    '/open/api/v2/order/list',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
      trade_type: trade_type.toUpperCase(),
      states: states.toUpperCase(),
    })
  )
}

//查询订单
QueryOrderById (order_ids	, options = {}) {
  validateRequiredParameters({ order_ids	 })
  return this.SignRequest(
    'GET',
    '/open/api/v2/order/query',
    Object.assign(options, {
      order_ids	: order_ids.toUpperCase(),
    })
  )
}

//个人成交记录
GetOrderDeal (order_id, options = {}) {
  validateRequiredParameters({ order_id })
  return this.SignRequest(
    'GET',
    '/open/api/v2/order/deals',
    Object.assign(options, {
      order_id: order_id.toUpperCase(),
    })
  )
}

//成交明细
QueryOrderDealById (order_id, options = {}) {
  validateRequiredParameters({ order_id })
  return this.SignRequest(
    'GET',
    '/open/api/v2/order/deal_detail',
    Object.assign(options, {
      order_id: order_id.toUpperCase(),
    })
  )
}

//按交易对撤销订单
CancelBySymbol (symbol, options = {}) {
  validateRequiredParameters({ symbol })
  return this.SignRequest(
    'DELETE',
    '/open/api/v2/order/cancel_by_symbol',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
    })
  )
}





//获取充币地址
GetDepositList ( currency,options = {}) {
  validateRequiredParameters({currency})
  return this.SignRequest(
    'GET',
    '/open/api/v2/asset/deposit/address/list',
     Object.assign(options,{
    currency: currency.toUpperCase() 
     })   
  )
}

//充值记录查询
GetDepositRecord ( options = {}) {
  validateRequiredParameters({})
  return this.SignRequest(
    'GET',
    '/open/api/v2/asset/deposit/list',
     options   
  )
}

//提币地址列表查询
GetWithdrawList ( options = {}) {
  validateRequiredParameters({})
  return this.SignRequest(
    'GET',
    '/open/api/v2/asset/withdraw/list',
     options   
  )
}

//提币
withdraw ( options = {}) {
  return this.SignRequest(
    'POST',
    '/open/api/v2/asset/withdraw',
     options
  )
}

//取消提币
CancelWithdraw (withdraw_id, options = {}) {
  validateRequiredParameters({ withdraw_id })
  return this.SignRequest(
    'DELETE',
    '/open/api/v2/asset/withdraw',
    Object.assign(options, {
      withdraw_id: withdraw_id.toUpperCase()
    })
  )
}

//内部资金划转
transfer ( options = {}) {
  return this.SignRequest(
    'POST',
    '/open/api/v2/asset/internal/transfer',
    options
  )
}

//内部资金转账记录
GetTransferRecord (options = {}) {
  validateRequiredParameters({})
  return this.SignRequest(
    'GET',
    '/open/api/v2/asset/internal/transfer/record',
     options 
  )
}

//获取可划转资金
GetAvlTransfer (currency, options = {}) {
  validateRequiredParameters({ currency })
  return this.SignRequest(
    'GET',
    '/open/api/v2/account/balance',
    Object.assign(options, {
      currency: currency.toUpperCase(),
    })
  )
}

//内部资金划转订单查询
QueryTransferRecordById (transact_id, options = {}) {
  validateRequiredParameters({ transact_id })
  return this.SignRequest(
    'GET',
    '/open/api/v2/asset/internal/transfer/info',
    Object.assign(options, {
      transact_id: transact_id.toUpperCase(),
    })
  )
}

/**
 * @V3
 */
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

//获取ETF
Etfinfo () {
return this.publicRequest('GET', 'api/v3/etf/info')
}

  //测试下单
  TestConnectivity (options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/order/test',
      options
    )
  }

 //下单
 Order (symbol, side, type, options = {}) {
    validateRequiredParameters({ symbol, side, type })
    return this.signRequest(
      'POST',
      '/api/v3/order',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        side: side.toUpperCase(),
        type: type.toUpperCase()
      })
    )
  }

//撤销订单
CancelOrder (symbol,  options = {}) {
    validateRequiredParameters({symbol})
    return this.signRequest(
      'DELETE',
      '/api/v3/order',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
        
      })   
    )
  }

//撤销单一交易对所有订单
CancelallOpenOrders (symbol,  options = {}) {
    validateRequiredParameters({symbol })
    return this.signRequest(
      'DELETE',
      '/api/v3/openOrders',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
      
      })   
    )
  }

//查询订单
QueryOrderr (symbol,  options = {}) {
    validateRequiredParameters({symbol})
    return this.signRequest(
      'GET',
      '/api/v3/order',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),

      })   
    )
  }

//当前挂单
CurrentOpenOrders (symbol,  options = {}) {
    validateRequiredParameters({symbol})
    return this.signRequest(
      'GET',
      '/api/v3/openOrders',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
 
      })   
    )
  }

//查询所有订单
AllOrders (symbol,  options = {}) {
    validateRequiredParameters({symbol })
    return this.signRequest(
      'GET',
      '/api/v3/allOrders',
      Object.assign(options, {
        symbol: symbol.toUpperCase()
      })   
    )
  }

//账户信息
AccountInformation ( options = {}) {
  return this.signRequest(
    'GET',
    '/api/v3/account',
    options
    
  )
}

//账户成交历史
AccountTradeList (symbol, options = {}) {
    validateRequiredParameters({symbol})
    return this.signRequest(
      'GET',
      '/api/v3/myTrades',
      Object.assign(options, {
        symbol: symbol.toUpperCase()

      })   
    )
  }

  //*母子账户接口*//

  //创建子账户
  virtualSubAccount (subAccount, note	, options = {}) {
    validateRequiredParameters({subAccount, note	})
    return this.signRequest(
      'POST',
      '/api/v3/sub-account/virtualSubAccount',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        note: note.toUpperCase()

      })   
    )
  }

  //查看子账户列表
  SubAccountList ( options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/list',
      options  
    )
  }

  //创建子账户的APIkey
  virtualApikey (subAccount, note, permissions	, options = {}) {
    validateRequiredParameters({subAccount, note, permissions	})
    return this.signRequest(
      'POST',
      '/api/v3/sub-account/apiKey',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        note: note.toUpperCase(),
        permissions:permissions.toUpperCase()
      })   
    )
  }

  //查询子账户的APIkey
  GetApiKey (subAccount	, options = {}) {
    validateRequiredParameters({subAccount})
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/apiKey',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase()
      })   
    )
  }

  //删除子账户的APIkey
  DelAccount (subAccount, apiKey,	  options = {}) {
    validateRequiredParameters({subAccount, apiKey})
    return this.signRequest(
      'DELETE',
      '/api/v3/sub-account/apiKey',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        apikey:apikey.toUpperCase()
      })   
    )
  }


//切换杠杆模式
TradeMode (tradeMode , symbol, options= {}) {
  validateRequiredParameters({tradeMode , symbol})
  return this.signRequest(
    'POST',
    '/api/v3/margin/tradeMode',
    Object.assign(options, {
      tradeMode: tradeMode.toUpperCase(),
      symbol:symbol.toUpperCase()
    })   
  )
}

//下单
Marginorder (tradeMode , symbol, options= {}) {
  validateRequiredParameters({tradeMode , symbol})
  return this.signRequest(
    'POST',
    '/api/v3/margin/order',
    Object.assign(options, {
      tradeMode: tradeMode.toUpperCase(),
      symbol:symbol.toUpperCase()
    })   
  )
}

//借贷
loan (asset , amount, options= {}) {
  validateRequiredParameters({asset , amount})
  return this.signRequest(
    'POST',
    '/api/v3/margin/loan',
    Object.assign(options, {
      asset: asset.toUpperCase(),
      amount:amount.toUpperCase()
    })   
  )
}

//归还借贷
repay (asset , symbol, borrowId, recvWindow, options= {}) {
  validateRequiredParameters({asset , symbol, borrowId, recvWindow})
  return this.signRequest(
    'POST',
    '/api/v3/margin/repay',
    Object.assign(options, {
      asset: asset.toUpperCase(),
      symbol:symbol.toUpperCase(),
      borrowId:borrowId.toUpperCase(),
      recvWindow:recvWindow.toUpperCase()

    })   
  )
}

//撤销单一交易对的所有挂单
cancelAllMargin (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'DELETE',
    '/api/v3/margin/openOrders',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//撤销订单
cancelMargin (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'DELETE',
    '/api/v3/margin/order',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//查询借贷记录
loanRecord (asset , recvWindow, options= {}) {
  validateRequiredParameters({asset , recvWindow})
  return this.signRequest(
    'GET',
    '/api/v3/margin/loan',
    Object.assign(options, {
      asset: asset.toUpperCase(),
      recvWindow:recvWindow.toUpperCase()
    })   
  )
}

//查询历史委托记录
allOrdersRecord (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/allOrders',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//查询历史成交记录
MyTrades (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/myTrades',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//查询当前挂单记录
marginOpenOrders (options= {}) {
  return this.signRequest(
    'GET',
    '/api/v3/margin/openOrders',
    options
    )  
}

//查询最大可转出额
maxTransferableh (asset , symbol, options= {}) {
  validateRequiredParameters({asset , symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/maxTransferableh',
    Object.assign(options, {
      asset: asset.toUpperCase(),
      symbol:symbol.toUpperCase()
    })   
  )
}

//查询杠杆价格指数
priceIndex (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/priceIndex',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//查询杠杆账户订单详情
marginOrder (options= {}) {
  return this.signRequest(
    'GET',
    '/api/v3/margin/order',
    options
  )
}

//查询杠杆逐仓账户信息
isolatedAccount (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/isolated/account',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//查询止盈止损订单
trigerOrder (options= {}) {
  return this.signRequest(
    'GET',
    '/api/v3/margin/trigerOrder',
    options
  )
}

//查询账户最大可借贷额度
maxBorrowable (asset ,options= {}) {
  validateRequiredParameters({asset})
  return this.signRequest(
    'GET',
    '/api/v3/margin/maxBorrowable',
    Object.assign(options, {
      asset: asset.toUpperCase()
    })   
  )
}

//查询还贷记录
repayRecord (asset , tranId, options= {}) {
  validateRequiredParameters({asset , tranId})
  return this.signRequest(
    'GET',
    '/api/v3/margin/repay',
    Object.assign(options, {
      asset: asset.toUpperCase(),
      tranId:tranId.toUpperCase()
    })   
  )
}

//查询逐仓杠杆交易对
isolatedPair (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/isolated/pair',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}

//获取账户强制平仓记录
forceLiquidationRec (options= {}) {
  return this.signRequest(
    'GET',
    '/api/v3/margin/forceLiquidationRec',
    options
  )
}

//获取逐仓杠杆利率及限额
isolatedMarginData (options= {}) {
  return this.signRequest(
    'GET',
    '/api/v3/margin/isolatedMarginData',
    options  
  )
}

//获取逐仓档位信息
isolatedMarginTier (symbol, options= {}) {
  validateRequiredParameters({symbol})
  return this.signRequest(
    'GET',
    '/api/v3/margin/isolatedMarginTier',
    Object.assign(options, {
      symbol:symbol.toUpperCase()
    })   
  )
}





  
}

module.exports = Spot