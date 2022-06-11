const { validateRequiredParameters } = require('../helpers/validation')
const Future = superclass => class extends superclass {
  //获取用户所有资产信息
  Assets (options = {}) {
    return this.SignRequest(
      'GET',
      '/api/v1/private/account/assets',
      options
    )
  }

 //获取用户单个币种资产信息
 AssetByCurrency (currency, options = {}) {
      validateRequiredParameters({currency})
  return this.SignRequest(
    'GET',
    '/api/v1/private/account/asset/{currency}',
    Object.assign(options, {
        currency:currency.toUpperCase()
    })
  )
}

//获取用户资产划转记录
TransferRecord (page_num, page_size, options = {}) {
  validateRequiredParameters({ page_num, page_size })
  return this.SignRequest(
    'GET',
    '/api/v1/private/account/transfer_record',
    Object.assign(options, {
        page_num: page_num.toUpperCase(),
        page_size: page_size.toUpperCase(),
    })
  )
}

//获取用户历史持仓信息
HistoryPositions (page_num, page_size, options = {}) {
    validateRequiredParameters({ page_num, page_size })
    return this.SignRequest(
      'GET',
      '/api/v1/private/position/list/history_positions',
      Object.assign(options, {
          page_num: page_num.toUpperCase(),
          page_size: page_size.toUpperCase(),
      })
    )
  }

//获取用户当前持仓
OpenPositions (options = {}) {
    return this.SignRequest(
      'GET',
      '/api/v1/private/position/open_positions',
      options
    )
  }

//获取用户资金费用明细
FundingRecords (page_num, page_size, options = {}) {
    validateRequiredParameters({ page_num, page_size })
    return this.SignRequest(
      'GET',
      '/api/v1/private/position/funding_records',
      Object.assign(options, {
          page_num: page_num.toUpperCase(),
          page_size: page_size.toUpperCase(),
      })
    )
  }

//获取用户当前未结束订单
OpenOrders (page_num, page_size, options = {}) {
    validateRequiredParameters({ page_num, page_size })
    return this.SignRequest(
      'GET',
      '/api/v1/private/order/list/open_orders/{symbol}',
      Object.assign(options, {
          page_num: page_num.toUpperCase(),
          page_size: page_size.toUpperCase(),
      })
    )
  }

//获取用户所有历史订单
HistoryOrders (page_num, page_size, options = {}) {
    validateRequiredParameters({ page_num, page_size })
    return this.SignRequest(
      'GET',
      '/api/v1/private/order/list/history_orders',
      Object.assign(options, {
          page_num: page_num.toUpperCase(),
          page_size: page_size.toUpperCase(),
      })
    )
  }

//根据外部号查询订单
ExternalByExternalOid (symbol, external_oid, options = {}) {
    validateRequiredParameters({ symbol, external_oid })
    return this.SignRequest(
      'GET',
      '/api/v1/private/order/external/{symbol}/{external_oid}',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        external_oid: external_oid.toUpperCase(),
      })
    )
  }

//根据订单号查询订单
QueryOrderById (order_id, options = {}) {
  validateRequiredParameters({ order_id })
  return this.SignRequest(
    'GET',
    '/api/v1/private/order/get/{order_id}',
    Object.assign(options, {
      order_id: order_id.toUpperCase(),
    })
  )
}

//根据订单号批量查询订单
BatchQueryById (order_ids, options = {}) {
  validateRequiredParameters({ order_ids })
  return this.SignRequest(
    'GET',
    '/api/v1/private/order/batch_query',
    Object.assign(options, {
        order_ids: order_ids.toUpperCase(),
    })
  )
}



//根据订单号获取订单成交明细
DealDetails ( order_id	,options = {}) {
  validateRequiredParameters({order_id})
  return this.SignRequest(
    'GET',
    '/api/v1/private/order/deal_details/{order_id}',
     Object.assign(options,{
        order_id: order_id.toUpperCase() 
     })   
  )
}

//获取用户所有订单成交明细
OrderDeals ( symbol, page_num, page_size, options = {}) {
    validateRequiredParameters({symbol, page_num, page_size})
    return this.SignRequest(
      'GET',
      '/api/v1/private/order/list/order_deals',
       Object.assign(options,{
        symbol: symbol.toUpperCase(),
        page_num: page_num.toUpperCase(),
        page_size: page_size.toUpperCase()  
       })   
    )
  }

//获取计划委托订单列表
Planorder ( page_num, page_size, options = {}) {
    validateRequiredParameters({page_num, page_size})
    return this.SignRequest(
      'GET',
      '/api/v1/private/planorder/list/orders',
       Object.assign(options,{
        page_num: page_num.toUpperCase(),
        page_size: page_size.toUpperCase()  
       })   
    )
  }


//获取止盈止损订单列表
Stoporder ( page_num, page_size, options = {}) {
    validateRequiredParameters({page_num, page_size})
    return this.SignRequest(
      'GET',
      '/api/v1/private/stoporder/list/orders',
       Object.assign(options,{
        page_num: page_num.toUpperCase(),
        page_size: page_size.toUpperCase()  
       })   
    )
  }

//获取风险限额
RiskLimit (options = {}) {
  validateRequiredParameters({ })
  return this.SignRequest(
    'GET',
    '/api/v1/private/account/risk_limit',
     options
  )
}

//获取用户当前手续费率
TieredFeeRate ( symbol, options = {}) {
  validateRequiredParameters({ symbol })
  return this.SignRequest(
    'GET',
    '/api/v1/private/account/tiered_fee_rate',
    Object.assign(options, {
        symbol: symbol.toUpperCase()
    })
  )
}
//增加或减少仓位保证金
ChangeMargin (positionId, amount, type,  options = {}) {
    validateRequiredParameters({ positionId, amount, type })
    return this.SignRequest(
      'POST',
      '/api/v1/private/position/change_margin',
      Object.assign(options, {
        positionId: positionId.toUpperCase(),
        amount: amount.toUpperCase(),
        type: type.toUpperCase()
      })
    )
  }

//获取持仓杠杆倍数
Leverage (symbol, options = {}) {
  validateRequiredParameters({ symbol })
  return this.SignRequest(
    'GET',
    '/api/v1/private/position/leverage',
    Object.assign(options, {
        symbol: symbol.toUpperCase(),
    })
  )
}

//修改杠杆倍数
ChangeLeverage (leverage, options = {}) {
  validateRequiredParameters({ leverage })
  return this.SignRequest(
    'POST',
    'api/v1/private/position/change_leverage',
    Object.assign(options, {
        leverage: leverage.toUpperCase(),
    })
  )
}

//获取用户仓位模式
PositionMode (transact_id, options = {}) {
    validateRequiredParameters({ transact_id })
    return this.SignRequest(
      'GET',
      '/open/api/v2/asset/internal/transfer/info',
      Object.assign(options, {
        transact_id: transact_id.toUpperCase(),
      })
    )
  }
  //修改用户仓位模式
  ChangePositionMode (options = {}) {
    validateRequiredParameters({ })
    return this.SignRequest(
      'POST',
      'api/v1/private/position/position_mode',
       options

    )
  }
  //下单
  PlaceNewOrder (symbol, price, vol, side, type, openType, options = {}) {
    validateRequiredParameters({ symbol, price, vol, side, type, openType })
    return this.SignRequest(
      'POST',
      '/api/v1/private/order/submit',
      Object.assign(options, {
        symbol:symbol.toUpperCase(),
        price: price.toUpperCase(),
        vol: vol.toUpperCase(),
        side: side.toUpperCase(),
        type: type.toUpperCase(),
        openType: openType.toUpperCase()
      })
    )
  }

  //批量下单
  PlaceNewOrderBatch (symbol, price, vol, side, type, openType, options = {}) {
    validateRequiredParameters({ symbol, price, vol, side, type, openType })
    return this.SignRequest(
      'POST',
      '/api/v1/private/order/submit_batch',
      Object.assign(options, {
        symbol:symbol.toUpperCase(),
        price: price.toUpperCase(),
        vol: vol.toUpperCase(),
        side: side.toUpperCase(),
        type: type.toUpperCase(),
        openType: openType.toUpperCase()
      })
    )
  }
  //取消订单#
  CancelOrderById (orderId, options = {}) {
    validateRequiredParameters({ orderId })
    return this.SignRequest(
      'POST',
      '/api/v1/private/order/cancel',
      Object.assign(options, {
        orderId: orderId.toUpperCase(),
      })
    )
  }

  //根据外部订单号取消订单
  CancelWithExternal (symbol, externalOid, options = {}) {
    validateRequiredParameters({ symbol, externalOid })
    return this.SignRequest(
      'POST',
      '/api/v1/private/order/cancel_with_external',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        externalOid: externalOid.toUpperCase(),
      })
    )
  }
  //取消某合约下所有订单
  CancelAll (options = {}) {
    validateRequiredParameters({})
    return this.SignRequest(
      'POST',
      '/api/v1/private/order/cancel_all',
      options
    )
  }

  //修改风险等级（已禁用）

    //计划委托下单
    PlacePlanOrder (symbol, vol, side,  openType, triggerPrice, triggerType, executeCycle,
        orderType, trend, options = {}) {
        validateRequiredParameters({ symbol, vol, side,  openType, triggerPrice, triggerType, executeCycle,
            orderType, trend })
        return this.SignRequest(
          'POST',
          '/api/v1/private/order/submit_batch',
          Object.assign(options, {
            symbol:symbol.toUpperCase(),
            openType: openType.toUpperCase(),
            vol: vol.toUpperCase(),
            side: side.toUpperCase(),
            triggerPrice: triggerPrice.toUpperCase(),
            triggerType: triggerType.toUpperCase(),
            executeCycle: executeCycle.toUpperCase(),
            orderType: orderType.toUpperCase(),
            trend: trend.toUpperCase()
          })
        )
      }
        //取消计划委托订单
        CancelPlanOrder (symbol, orderId, options = {}) {
    validateRequiredParameters({ symbol, orderId })
    return this.SignRequest(
      'POST',
      '/api/v1/private/planorder/cancel',
      Object.assign(options, {
        symbol: symbol.toUpperCase(),
        orderId: orderId.toUpperCase()
      })
    )
  }
    //取消所有计划委托订单
    CancelAllPlanOrder ( options = {}) {
        validateRequiredParameters({ })
        return this.SignRequest(
          'POST',
          '/api/v1/private/planorder/cancel_all',
          options
        )
      }
        //取消止盈止损委托单
        CancelStopOrder (stopPlanOrderId, options = {}) {
    validateRequiredParameters({ stopPlanOrderId })
    return this.SignRequest(
      'POST',
      '/api/v1/private/stoporder/cancel',
      Object.assign(options, {
        stopPlanOrderId: stopPlanOrderId.toUpperCase(),
      })
    )
  }
    //取消所有止盈止损委托单
    CancelAllStopOrder ( options = {}) {
        validateRequiredParameters({})
        return this.SignRequest(
          'POST',
          '/api/v1/private/stoporder/cancel_all',
          options
        )
      }
          //修改限价单止盈止损价格
          StoporderChangePrice (orderId, options = {}) {
        validateRequiredParameters({ orderId })
        return this.SignRequest(
          'POST',
          '/api/v1/private/stoporder/change_price',
          Object.assign(options, {
            orderId: orderId.toUpperCase(),
          })
        )
      }


          //修改止盈止损委托单止盈止损价格
          StopOrderChangePlanPrice (stopPlanOrderId, options = {}) {
        validateRequiredParameters({ stopPlanOrderId })
        return this.SignRequest(
          'POST',
          '/api/v1/private/stoporder/change_plan_price',
          Object.assign(options, {
            stopPlanOrderId: stopPlanOrderId.toUpperCase(),
          })
        )
      }





  
}

module.exports = Future