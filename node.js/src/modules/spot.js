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
    options
  )
}

//下单
PlaceOrder (symbol, trade_type, order_type, quantity, price, options = {}) {
  validateRequiredParameters({ symbol, trade_type, order_type, quantity, price })
  return this.SignRequest(
    'POST',
    '/open/api/v2/order/place',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
      trade_type: trade_type.toUpperCase(),
      order_type: order_type.toUpperCase(),
      quantity: quantity.toUpperCase(),
      price: price.toUpperCase()
    })
  )
}

//撤销订单
CancelOrder ( options = {}) {
  validateRequiredParameters({})
  return this.SignRequest(
    'DELETE',
    '/open/api/v2/order/cancel',
     options   
  )
}

//批量下单
MuiltPlaceOrder (symbol, trade_type, quantity, price, order_type, options = {}) {
  validateRequiredParameters({ symbol, trade_type, quantity, price, order_type })
  return this.SignRequest(
    'POST',
    '/open/api/v2/order/place_batch',
    Object.assign(options, {
      symbol: symbol.toUpperCase(),
      trade_type: trade_type.toUpperCase(),
      quantity: quantity.toUpperCase(),
      price: price.toUpperCase(),
      order_type: order_type.toUpperCase()
    })
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
withdraw (currency,	amount, address,	 options = {}) {
  validateRequiredParameters({currency,	amount, address})
  return this.SignRequest(
    'POST',
    '/open/api/v2/asset/withdraw',
     Object.assign(options, {
      currency: currency.toUpperCase(),
      amount: amount.toUpperCase(),
      address: address.toUpperCase()
     })  
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
transfer (currency,	amount, from, to, options = {}) {
  validateRequiredParameters({ currency,	amount, from, to })

  return this.SignRequest(
    'POST',
    '/open/api/v2/asset/internal/transfer',
    Object.assign(options, {
      currency: currency.toUpperCase(),
      amount: amount.toUpperCase(),
      from: from.toUpperCase(),
      to: to.toUpperCase()
    })
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