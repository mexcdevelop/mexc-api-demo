const { validateRequiredParameters } = require('../helpers/validation')
const Trade = superclass => class extends superclass {
  //测试下单
  TestConnectivity (options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/order/test',
      options
    )
  }

 //下单
 PlaceOrder (symbol, side, type, timestamp,options = {}) {
    validateRequiredParameters({ symbol, side, type, timestamp })
    return this.signRequest(
      'POST',
      '/api/v3/order',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        side: side.toUpperCase(),
        type: type.toUpperCase(),
        timestamp: timestamp.toUpperCase(),
      })
    )
  }

//撤销订单
CancelOrder (symbol, timestamp, options = {}) {
    validateRequiredParameters({symbol, timestamp})
    return this.signRequest(
      'DELETE',
      '/api/v3/order',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

//撤销单一交易对所有订单
CancelallOpenOrders (symbol, timestamp, options = {}) {
    validateRequiredParameters({symbol, timestamp})
    return this.signRequest(
      'DELETE',
      '/api/v3/openOrders',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

//查询订单
QueryOrderr (symbol, timestamp, options = {}) {
    validateRequiredParameters({symbol, timestamp})
    return this.signRequest(
      'GET',
      '/api/v3/order',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

//当前挂单
CurrentOpenOrders (symbol, timestamp, options = {}) {
    validateRequiredParameters({symbol, timestamp})
    return this.signRequest(
      'GET',
      '/api/v3/openOrders',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

//查询所有订单
AllOrders (symbol, timestamp, options = {}) {
    validateRequiredParameters({symbol, timestamp})
    return this.signRequest(
      'GET',
      '/api/v3/allOrders',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

//账户信息
AccountInformation (timestamp	, options = {}) {
  validateRequiredParameters({ timestamp	 })
  return this.signRequest(
    'GET',
    '/api/v3/account',
    Object.assign(options, {
        timestamp: timestamp.toUpperCase(),
    })
  )
}

//账户成交历史
AccountTradeList (symbol, timestamp, options = {}) {
    validateRequiredParameters({symbol, timestamp})
    return this.signRequest(
      'GET',
      '/api/v3/myTrades',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

  //*母子账户接口*//

  //创建子账户
  virtualSubAccount (subAccount, note	, timestamp, options = {}) {
    validateRequiredParameters({subAccount, note	, timestamp})
    return this.signRequest(
      'POST',
      '/api/v3/sub-account/virtualSubAccount',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        note: note.toUpperCase(),
        timestamp:timestamp.toUpperCase()
      })   
    )
  }

  //查看子账户列表
  SubAccountList ( timestamp, options = {}) {
    validateRequiredParameters({timestamp})
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/list',
      Object.assign(options, {
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

  //创建子账户的APIkey
  virtualApikey (subAccount, note, permissions	, timestamp, options = {}) {
    validateRequiredParameters({subAccount, note, permissions	, timestamp})
    return this.signRequest(
      'POST',
      '/api/v3/sub-account/apiKey',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        note: note.toUpperCase(),
        timestamp:timestamp.toUpperCase(),
        permissions:permissions.toUpperCase()
      })   
    )
  }

  //查询子账户的APIkey
  GetApiKey (subAccount	, timestamp, options = {}) {
    validateRequiredParameters({subAccount	, timestamp})
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/apiKey',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }

  //删除子账户的APIkey
  DelAccount (subAccount, apiKey,	 timestamp, options = {}) {
    validateRequiredParameters({subAccount, apiKey,	 timestamp})
    return this.signRequest(
      'DELETE',
      '/api/v3/sub-account/apiKey',
      Object.assign(options, {
        subAccount: subAccount.toUpperCase(),
        apikey:apikey.toUpperCase(),
        timestamp: timestamp.toUpperCase()
      })   
    )
  }



}

module.exports = Trade