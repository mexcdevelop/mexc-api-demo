const { validateRequiredParameters } = require('../helpers/validation')
const Trade = superclass => class extends superclass {
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






  
}

module.exports = Trade