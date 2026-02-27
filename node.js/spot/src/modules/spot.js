const { validateRequiredParameters } = require('../helpers/validation')
const Spot = superclass => class extends superclass {

  /**
   * @V3
   */
  //*行情接口*//
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

  //API交易对
  ApiDefault() {
    return this.publicRequest('GET', '/api/v3/defaultSymbols')
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
    return this.signRequest(
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

  //*现货账户和交易接口*//
  //查询账户KYC状态
  KycStatus(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/kyc/status',
      options
    )
  }

  //用户API交易对
  SelfSymbol(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/selfSymbols',
      options
    )
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
  AccountTradeList(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/myTrades',
      options
    )
  }

  //开启MX抵扣
  MxDeduct(options = {}) {
    return this.signRequest(
      'POST',
      'api/v3/mxDeduct/enable',
      options
    )
  }

  //查看MX抵扣状态
  MxDeducth(options = {}) {
    return this.signRequest(
      'GET',
      'api/v3/mxDeduct/enableh',
      options
    )
  }

  //查看手续费率
  TradeFee(options = {}) {
    return this.signRequest(
      'GET',
      'api/v3/tradeFee',
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
  DelApikey(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/sub-account/apiKey',
      options
    )
  }

  //母子用户万向划转
  UniversalTransfer(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/sub-account/universalTransfer',
      options
    )
  }

  //查询母子万向划转历史
  TransferHistory(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/sub-account/universalTransfer',
      options
    )
  }

  //查询子账户的APIkey
  GetAsset(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/sub-account/asset',
      options
    )
  }

  //*钱包接口*//
  //查询币种信息
  CoinList(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/config/getall',
      options
    )
  }

  //提币
  WithDraw(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/withdraw',
      options
    )
  }

  //取消提币
  CancelWithdraw(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/capital/withdraw',
      options
    )
  }
  //获取充值历史
  DepositHisrec(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/deposit/hisrec',
      options
    )
  }

  //获取提币历史
  WithdrawHistory(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/withdraw/history',
      options
    )
  }

  //生成充值地址
  GenerateDepositAddress(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/deposit/address',
      options
    )
  }

  //获取充值地址
  DepositAddress(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/deposit/address',
      options
    )
  }

  //获取提币地址
  WithdrawAddress(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/withdraw/address',
      options
    )
  }
  
  //用户万向划转
  Transfer(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/transfer',
      options
    )
  }

  //查询用户万向划转历史
  TransferHistory(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/transfer',
      options
    )
  }

  //查询用户万向划转历史(ID)
  TransferHistoryId(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/transfer/tranId',
      options
    )
  }

  //获取小额资产可兑换列表
  Capital(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/convert/list',
      options
    )
  }

  //小额资产兑换
  CapitalConvert(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/convert',
      options
    )
  } 

  //查询小额资产兑换历史
  CapitalHistory(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/convert',
      options
    )
  }
  
  //用户站内转账接口
  TransferInternal(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/capital/transfer/internal',
      options
    )
  } 

  //查询用户内部转账历史接口
  TransferInternalHistory(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/capital/transfer/internal',
      options
    )
  } 

  //*邀请返佣接口*//
  //  获取邀请返佣记录
  TaxQuery(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/taxQuery',
      options
    )
  }

  // 获取返佣记录明细 （奖励记录）
  RebateDetail(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/detail',
      options
    )
  }

  // 获取自返记录明细 （奖励记录）
  KickBack(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/detail/kickback',
      options
    )
  }

  // 获取邀请人
  ReferCode(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/referCode',
      options
    )
  }

  // 获取代理邀请返佣记录 （代理账户）
  Affiliate(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/affiliate/commission',
      options
    )
  }

  // 获取代理提现记录 （代理账户）
  AffiliateWithdraw(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/affiliate/withdraw',
      options
    )
  }

  // 获取代理返佣明细 （代理账户）
  AffiliateDetail(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/affiliate/commission/detail',
      options
    )
  }

  // 查询直客页面数据（代理账户）
  AffiliateReferral(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/affiliate/referral',
      options
    )
  }

  // 查询子代理页面数据（代理账户）
  AffiliateSubaffiliates(options = {}) {
    return this.signRequest(
      'GET',
      '/api/v3/rebate/affiliate/subaffiliates',
      options
    )
  }

  //*Websocket*//
  //创建listenkey
  CreateListenKey(options = {}) {
    return this.signRequest(
      'POST',
      '/api/v3/userDataStream',
      options
    )
  }

  //延长listenkey
  KeepListenKey(options = {}) {
    return this.signRequest(
      'PUT',
      '/api/v3/userDataStream',
      options
    )
  } 

  //关闭listenkey
  CloseListenKey(options = {}) {
    return this.signRequest(
      'DELETE',
      '/api/v3/userDataStream',
      options
    )
  } 
}

module.exports = Spot
