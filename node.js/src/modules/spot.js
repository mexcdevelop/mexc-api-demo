      
const { validateRequiredParameters } = require('../helpers/validation')
const Spot = superclass => class extends superclass {
  /**
   * @V3
   */
  //测试服务的连通性
  TestConnectivity() {
    return this.publicRequest('GET', '/api/v3/ping')
  }

  //获取服务器时间
  Servertime() {
    return this.publicRequest('GET', '/api/v3/time')
  }

  //交易规范信息
  ExchangeInformation() {
    return this.publicRequest('GET', '/api/v3/exchangeInfo')
  }

  //深度信息
  Depth(options = {}) {
    return this.publicRequest(
      'GET',
      '/api/v3/depth',
      options
    )
  }

  //近期成交列表
  RecentTradesList(options = {}) {
    return this.publicRequest(
      'GET',
      '/api/v3/trades',
      options
    )
  }

  //历史成交列表
  OldTradeLookup(ptions = {}) {
    return this.publicRequest(
      'GET',
      '/api/v3/historicalTrades',
      options
    )
  }

  //近期成交（归集）
  CompressedTradesList(options = {}) {
    return this.publicRequest(
      'GET',
      '/api/v3/aggTrades',
      options
    )
  }

  //K线数据
  Kline(options = {}) {
    return this.publicRequest(
      'GET',
      '/api/v3/klines',
      options
    )
  }

  //当前平均价格
  CurrentAveragePrice(options = {}) {
    return this.publicRequest(
      'GET',
      '/api/v3/avgPrice',
      options
    )
  }

  //24小时价格滚动情况
  TickerPriceChange() {
    return this.publicRequest('GET', '/api/v3/ticker/24hr')
  }

  //最新价格
  SymbolPriceTicker() {
    return this.publicRequest('GET', '/api/v3/ticker/price')
  }

  //当前最优挂单
  SymbolOrderBook() {
    return this.publicRequest('GET', '/api/v3/ticker/bookTicker')
  }

  //获取ETF
  Etfinfo() {
    return this.publicRequest('GET', 'api/v3/etf/info')
  }

  //测试下单
  TestConnectivity(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/order/test',
      options
    )
  }

  //下单
  Order(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/order',
      options
    )
  }

  //批量下单
  BatchOrders(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/batchOrders',
      options
    )
  }

  //撤销订单
  CancelOrder(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/order',
      options
    )
  }

  //撤销单一交易对所有订单
  CancelallOpenOrders(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/openOrders',
      options
    )
  }

  //查询订单
  QueryOrder(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/order',
      options
    )
  }

  //当前挂单
  CurrentOpenOrders(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/openOrders',
      options
    )
  }

  //查询所有订单
  AllOrders(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/allOrders',
      options
    )
  }

  //账户信息
  AccountInformation(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/account',
      options

    )
  }

  //账户成交历史
  AccountTradeList(ptions = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/myTrades',
      options
    )
  }

  //*母子账户接口*//

  //创建子账户
  VirtualSubAccount(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/sub-account/virtualSubAccount',
      options
    )
  }

  //查看子账户列表
  SubAccountList(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/list',
      options
    )
  }

  //创建子账户的APIkey
  VirtualApikey(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/sub-account/apiKey',
      options
    )
  }

  //查询子账户的APIkey
  GetApiKey(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/apiKey',
      options
    )
  }
 
  //删除子账户的APIkey
  DelAccount(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/sub-account/apiKey',
      options
    )
  }

    //删除子账户的APIkey
    TransferAccount(options = {}) {
      return this.signRequest(
        'POST',
        '/api/v3/capital/sub-account/universalTransfer',
        options
      )
    }


  //切换杠杆模式
  TradeMode(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/margin/tradeMode',
      options
    )
  }

  //下单
  Marginorder(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/margin/order',
      options
    )
  }

  //借贷
  Loan(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/margin/loan',
      options
    )
  }

  //归还借贷
  Repay(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/margin/repay',
      options
    )
  }

  //撤销单一交易对的所有挂单
  CancelAllMargin(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/margin/openOrders',
      options
    )
  }

  //撤销订单
  CancelMargin(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/margin/order',
      options
    )
  }

  //查询借贷记录
  LoanRecord(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/loan',
      options
    )
  }

  //查询历史委托记录
  AllOrdersRecord(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/allOrders',
      options
    )
  }

  //查询历史成交记录
  MyTrades(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/myTrades',
      options
    )
  }

  //查询当前挂单记录
  MarginOpenOrders(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/openOrders',
      options
    )
  }

  //查询最大可转出额
  MaxTransferableh(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/maxTransferableh',
      options
    )
  }

  //查询杠杆价格指数
  PriceIndex(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/priceIndex',
      options
    )
  }

  //查询杠杆账户订单详情
  MarginOrder(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/order',
      options
    )
  }

  //查询杠杆逐仓账户信息
  IsolatedAccount(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/isolated/account',
      options
    )
  }

  //查询止盈止损订单
  TrigerOrder(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/trigerOrder',
      options
    )
  }

  //查询账户最大可借贷额度
  MaxBorrowable(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/maxBorrowable',
      options
    )
  }

  //查询还贷记录
  RepayRecord(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/repay',
      options
    )
  }

  //查询逐仓杠杆交易对
  IsolatedPair(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/isolated/pair',
      options
    )
  }

  //获取账户强制平仓记录
  ForceLiquidationRec(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/forceLiquidationRec',
      options
    )
  }

  //获取逐仓杠杆利率及限额
  IsolatedMarginData(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/isolatedMarginData',
      options
    )
  }

  //获取逐仓档位信息
  IsolatedMarginTier(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/margin/isolatedMarginTier',
      options
    )
  }


  WithDraw(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/withdraw/apply',
      options
    )
  }

  //创建broker子账户
  CreateBroker(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/broker/sub-account/virtualSubAccount',
      options
    )
  }

}

module.exports = Spot

    