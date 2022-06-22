const { validateRequiredParameters } = require('../helpers/validation')
const V3Trade = superclass => class extends superclass {
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



}

module.exports = V3Trade