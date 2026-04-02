/**
 * MEXC Futures REST client (public + private endpoints).
 */

const {
  request,
  DEFAULT_BASE_URL,
  DEFAULT_RECV_WINDOW
} = require('../httpClient')
const { filterParams } = require('../signer')
const E = require('./endpoints')

function _checkSymbol (symbol, name = 'symbol') {
  if (symbol != null && typeof symbol !== 'string') {
    throw new TypeError(name + ' must be string')
  }
}

function _requireSymbol (symbol, name = 'symbol') {
  if (!symbol || typeof symbol !== 'string') {
    throw new TypeError(name + ' required and must be string')
  }
}

function _checkLimit (limit, max = 100) {
  if (limit != null && (typeof limit !== 'number' || limit < 1 || limit > max)) {
    throw new TypeError('limit must be number 1-' + max)
  }
}

function _checkPage (pageNum, pageSize, pageSizeMax = 100) {
  if (pageNum != null && (typeof pageNum !== 'number' || pageNum < 1)) {
    throw new TypeError('page_num must be number >= 1')
  }
  if (pageSize != null && (typeof pageSize !== 'number' || pageSize < 1 || pageSize > pageSizeMax)) {
    throw new TypeError('page_size must be number 1-' + pageSizeMax)
  }
}

/** Filter undefined/null for optional query params. */
function _cleanQuery (obj) {
  return filterParams(obj || {})
}

class MexcFuturesRestClient {
  /**
   * @param {Object} [options]
   * @param {string} [options.apiKey]
   * @param {string} [options.apiSecret]
   * @param {string} [options.baseURL] - default https://api.mexc.com
   * @param {number} [options.timeoutMs] - request timeout ms
   * @param {number} [options.recvWindow] - signature time window ms
   * @param {boolean} [options.unwrapData=false] - if true, return only data field
   */
  constructor (options = {}) {
    this.apiKey = options.apiKey || ''
    this.apiSecret = options.apiSecret || ''
    this.baseURL = options.baseURL || DEFAULT_BASE_URL
    this.timeoutMs = options.timeoutMs
    this.recvWindow = options.recvWindow ?? DEFAULT_RECV_WINDOW
    this.unwrapData = options.unwrapData === true
  }

  _requestOpts () {
    const opts = {
      baseURL: this.baseURL,
      timeout: this.timeoutMs,
      unwrapData: this.unwrapData
    }
    if (this.apiKey && this.apiSecret) {
      opts.apiKey = this.apiKey
      opts.apiSecret = this.apiSecret
      opts.recvWindow = this.recvWindow
    }
    return opts
  }

  _public (path, params = {}) {
    return request({
      method: 'GET',
      path,
      params,
      ...this._requestOpts()
    })
  }

  _private (path, params = {}, method = 'GET', reqOpts = {}) {
    return request({
      method,
      path,
      params,
      ...this._requestOpts(),
      ...reqOpts
    })
  }

  _privatePost (path, params = {}) {
    return this._private(path, params, 'POST')
  }

  // ----- market (public, no auth) -----

  /**
   * Ping / connectivity check.
   * @endpoint GET /api/v1/contract/ping
   */
  ping () {
    return this._public(E.market.PING)
  }

  /**
   * Server time (millisecond timestamp).
   * @endpoint GET /api/v1/contract/ping
   */
  getServerTime () {
    return this._public(E.market.PING)
  }

  /**
   * Contract detail (single or all).
   * @endpoint GET /api/v1/contract/detail
   * @param {string} [symbol] - contract name e.g. BTC_USDT; omit for all
   */
  getContractDetail (symbol) {
    _checkSymbol(symbol)
    const params = _cleanQuery(symbol ? { symbol } : {})
    return this._public(E.market.CONTRACT_DETAIL, params)
  }

  /**
   * Support currencies (transferable list).
   * @endpoint GET /api/v1/contract/support_currencies
   */
  getSupportCurrencies () {
    return this._public(E.market.SUPPORT_CURRENCIES)
  }

  /**
   * Order book depth.
   * @endpoint GET /api/v1/contract/depth/{symbol}
   * @param {string} symbol - contract e.g. BTC_USDT
   * @param {number} [limit=20] - depth levels, max 100
   */
  getDepth (symbol, limit = 20) {
    _requireSymbol(symbol)
    _checkLimit(limit, 100)
    const path = `${E.market.DEPTH}/${symbol}`
    const params = _cleanQuery(limit != null ? { limit } : {})
    return this._public(path, params)
  }

  /**
   * Depth commits (recent N depth snapshots).
   * @endpoint GET /api/v1/contract/depth_commits/{symbol}/{limit}
   * @param {string} symbol - contract name
   * @param {number} limit - number of levels, required
   */
  getDepthCommits (symbol, limit) {
    _requireSymbol(symbol)
    if (limit == null || typeof limit !== 'number' || limit < 1) throw new TypeError('limit required, must be number >= 1')
    const path = `${E.market.DEPTH_COMMITS}/${symbol}/${limit}`
    return this._public(path)
  }

  /**
   * Index price.
   * @endpoint GET /api/v1/contract/index_price/{symbol}
   * @param {string} symbol - contract name
   */
  getIndexPrice (symbol) {
    _requireSymbol(symbol)
    const path = `${E.market.INDEX_PRICE}/${symbol}`
    return this._public(path)
  }

  /**
   * Fair price (mark price).
   * @endpoint GET /api/v1/contract/fair_price/{symbol}
   * @param {string} symbol - contract name
   */
  getFairPrice (symbol) {
    _requireSymbol(symbol)
    const path = `${E.market.FAIR_PRICE}/${symbol}`
    return this._public(path)
  }

  /**
   * Funding rate (current).
   * @endpoint GET /api/v1/contract/funding_rate/{symbol}
   * @param {string} symbol - contract name
   */
  getFundingRate (symbol) {
    _requireSymbol(symbol)
    const path = `${E.market.FUNDING_RATE}/${symbol}`
    return this._public(path)
  }

  /**
   * Funding rate history (paginated).
   * @endpoint GET /api/v1/contract/funding_rate/history
   * @param {Object} params - symbol (required), page_num (default 1), page_size (default 20, max 100)
   */
  getFundingRateHistory (params) {
    const p = params || {}
    _requireSymbol(p.symbol)
    _checkPage(p.page_num, p.page_size, 100)
    return this._public(E.market.FUNDING_RATE_HISTORY, _cleanQuery({ symbol: p.symbol, page_num: p.page_num, page_size: p.page_size }))
  }

  /**
   * Kline / candlestick data.
   * @endpoint GET /api/v1/contract/kline/{symbol}
   * @param {string} symbol - contract name
   * @param {Object} [params] - interval?, start?, end (start/end in seconds)
   */
  getKline (symbol, params = {}) {
    _requireSymbol(symbol)
    const path = `${E.market.KLINE}/${symbol}`
    const q = _cleanQuery({ interval: params.interval, start: params.start, end: params.end })
    return this._public(path, q)
  }

  /**
   * Index price kline.
   * @endpoint GET /api/v1/contract/kline/index_price/{symbol}
   * @param {string} symbol - contract name
   * @param {Object} [params] - interval?, start?, end
   */
  getKlineIndexPrice (symbol, params = {}) {
    _requireSymbol(symbol)
    const path = `${E.market.KLINE_INDEX_PRICE}/${symbol}`
    const q = _cleanQuery({ interval: params.interval, start: params.start, end: params.end })
    return this._public(path, q)
  }

  /**
   * Fair price kline.
   * @endpoint GET /api/v1/contract/kline/fair_price/{symbol}
   * @param {string} symbol - contract name
   * @param {Object} [params] - interval?, start?, end
   */
  getKlineFairPrice (symbol, params = {}) {
    _requireSymbol(symbol)
    const path = `${E.market.KLINE_FAIR_PRICE}/${symbol}`
    const q = _cleanQuery({ interval: params.interval, start: params.start, end: params.end })
    return this._public(path, q)
  }

  /**
   * Recent deals / trades.
   * @endpoint GET /api/v1/contract/deals/{symbol}
   * @param {string} symbol - contract name
   * @param {number} [limit=100] - count, max 100
   */
  getDeals (symbol, limit = 100) {
    _requireSymbol(symbol)
    _checkLimit(limit, 100)
    const path = `${E.market.DEALS}/${symbol}`
    const params = _cleanQuery(limit != null ? { limit } : {})
    return this._public(path, params)
  }

  /**
   * Ticker (single or all contracts).
   * @endpoint GET /api/v1/contract/ticker
   * @param {string} [symbol] - contract name; omit for all
   */
  getTicker (symbol) {
    _checkSymbol(symbol)
    const params = _cleanQuery(symbol ? { symbol } : {})
    return this._public(E.market.TICKER, params)
  }

  /**
   * Risk fund balance (risk reverse). Optional symbol; omit for all.
   * @endpoint GET /api/v1/contract/risk_reverse or /api/v1/contract/risk_reverse/{symbol}
   * @param {string} [symbol] - contract name; omit for all contracts
   */
  getRiskReverse (symbol) {
    _checkSymbol(symbol)
    if (!symbol || symbol === '') {
      return this._public(E.market.RISK_REVERSE)
    }
    const path = `${E.market.RISK_REVERSE}/${symbol}`
    return this._public(path)
  }

  /**
   * Risk fund balance history (paginated).
   * @endpoint GET /api/v1/contract/risk_reverse/history
   * @param {Object} params - symbol (required), page_num, page_size (max 100)
   */
  getRiskReverseHistory (params) {
    const p = params || {}
    _requireSymbol(p.symbol)
    _checkPage(p.page_num, p.page_size, 100)
    return this._public(E.market.RISK_REVERSE_HISTORY, _cleanQuery({ symbol: p.symbol, page_num: p.page_num, page_size: p.page_size }))
  }

  // ----- account (private) -----

  /**
   * All account assets.
   * @endpoint GET /api/v1/private/account/assets
   */
  getAssets () {
    return this._private(E.account.ASSETS)
  }

  /**
   * Single currency asset.
   * @endpoint GET /api/v1/private/account/asset/{currency}
   * @param {Object} params - currency (required)
   */
  getAsset (params = {}) {
    const currency = params.currency
    if (!currency || typeof currency !== 'string') throw new TypeError('params.currency required')
    const path = `${E.account.ASSET}/${currency}`
    return this._private(path)
  }

  /**
   * Transfer records.
   * @endpoint GET /api/v1/private/account/transfer_record
   * @param {Object} params - currency?, state?, type?, page_num (default 1), page_size (default 20)
   */
  getTransferRecords (params = {}) {
    const raw = params || {}
    const pageNum = raw.page_num != null && raw.page_num >= 1 ? raw.page_num : 1
    const pageSize = raw.page_size != null && raw.page_size >= 1 ? raw.page_size : 20
    _checkPage(pageNum, pageSize)
    const p = _cleanQuery({ ...raw, page_num: pageNum, page_size: pageSize })
    return this._private(E.account.TRANSFER_RECORD, p)
  }

  /** @deprecated Use getTransferRecords */
  getTransferRecord (params = {}) {
    return this.getTransferRecords(params)
  }

  /**
   * Profit rate.
   * @endpoint GET /api/v1/private/account/profit_rate/{type}
   * @param {Object} params - type (1: day, 2: week)
   */
  getProfitRate (params = {}) {
    const type = params.type
    if (type == null || typeof type !== 'number') throw new TypeError('params.type required (1: day, 2: week)')
    const path = `${E.account.PROFIT_RATE}/${type}`
    return this._private(path)
  }

  /**
   * Yesterday PnL.
   * @endpoint GET /api/v1/private/account/asset/analysis/yesterday_pnl
   */
  getYesterdayPnl () {
    return this._private(E.account.YESTERDAY_PNL)
  }

  /**
   * Today PnL.
   * @endpoint GET /api/v1/private/account/asset/analysis/today_pnl
   * @param {Object} [params] - reverse?, includeUnrealisedPnl?
   */
  getTodayPnl (params = {}) {
    return this._private(E.account.TODAY_PNL, _cleanQuery(params))
  }

  /**
   * Fee deduct configs.
   * @endpoint GET /api/v1/private/account/feeDeductConfigs
   */
  getFeeDeductConfigs () {
    return this._private(E.account.FEE_DEDUCT_CONFIGS)
  }

  /**
   * Risk limit.
   * @endpoint GET /api/v1/private/account/risk_limit
   * @param {Object} [params] - symbol?
   */
  getRiskLimit (params = {}) {
    return this._private(E.account.RISK_LIMIT, _cleanQuery(params))
  }

  /**
   * Contract fee rate.
   * @endpoint GET /api/v1/private/account/contract/fee_rate
   * @param {Object} [params] - symbol?
   */
  getFeeRate (params = {}) {
    return this._private(E.account.FEE_RATE, _cleanQuery(params))
  }

  /**
   * Tiered fee rate.
   * @endpoint GET /api/v1/private/account/tiered_fee_rate/v2
   * @param {Object} [params] - symbol?
   */
  getTieredFeeRate (params = {}) {
    return this._private(E.account.TIERED_FEE_RATE, _cleanQuery(params))
  }

  /**
   * User discount type.
   * @endpoint GET /api/v1/private/account/discountType
   */
  getDiscountType () {
    return this._private(E.account.DISCOUNT_TYPE)
  }

  // ----- position (private) -----

  /**
   * Open positions.
   * @endpoint GET /api/v1/private/position/open_positions
   * @param {Object} [params] - symbol, positionId
   */
  getPositions (params = {}) {
    return this._private(E.position.OPEN_POSITIONS, params)
  }

  /**
   * History positions.
   * @endpoint GET /api/v1/private/position/list/history_positions
   * @param {Object} params - symbol, type, start_time, end_time, page_num, page_size
   */
  getHistoryPositions (params = {}) {
    _checkPage(params.page_num, params.page_size)
    return this._private(E.position.HISTORY_POSITIONS, params)
  }

  /**
   * Position leverage.
   * @endpoint GET /api/v1/private/position/leverage
   * @param {Object} params - symbol (required)
   */
  getLeverage (params = {}) {
    if (!params.symbol) throw new TypeError('params.symbol required')
    return this._private(E.position.LEVERAGE, { symbol: params.symbol })
  }

  /**
   * Funding records.
   * @endpoint GET /api/v1/private/position/funding_records
   * @param {Object} params - symbol, position_id, page_num, page_size, position_type, start_time, end_time
   */
  getFundingRecords (params) {
    if (!params || params.page_num == null || params.page_size == null ||
        params.position_type == null || params.start_time == null || params.end_time == null) {
      throw new TypeError('page_num, page_size, position_type, start_time, end_time required')
    }
    return this._private(E.position.FUNDING_RECORDS, params)
  }

  // ----- order (private) -----

  /**
   * Place a single order (HTTP path: /order/create). Public method name remains submitOrder for compatibility.
   * @endpoint POST /api/v1/private/order/create
   * @param {Object} params - symbol, vol, side, type, openType, price? (required for limit), leverage? (required for open)
   */
  submitOrder (params = {}) {
    if (!params.symbol) throw new TypeError('params.symbol required')
    if (params.vol == null) throw new TypeError('params.vol required')
    if (params.side == null) throw new TypeError('params.side required')
    if (params.type == null) throw new TypeError('params.type required')
    if (params.openType == null) throw new TypeError('params.openType required')
    return this._privatePost(E.order.CREATE, _cleanQuery(params))
  }

  /**
   * Submit order batch (under maintenance, market maker only).
   * Sends raw JSON array body (same as Postman), not { batchOrder: [...] }.
   * @endpoint POST /api/v1/private/order/submit_batch
   * @param {Object|Array} params - batchOrder array or raw array of order objects
   */
  submitOrderBatch (params) {
    let list
    if (Array.isArray(params)) {
      list = params
    } else if (params && Array.isArray(params.batchOrder)) {
      list = params.batchOrder
    } else {
      throw new TypeError('batchOrder required (array) or pass array directly')
    }
    if (list.length === 0) throw new TypeError('batchOrder must not be empty')
    return this._privatePost(E.order.SUBMIT_BATCH, list)
  }

  /**
   * Cancel order(s) (under maintenance). Sends raw array body (order IDs), not { orderIds: [...] }.
   * @endpoint POST /api/v1/private/order/cancel
   * @param {Object} params - orderIds: number[] or orderId: number
   */
  cancelOrder (params) {
    if (!params) throw new TypeError('params required')
    const ids = Array.isArray(params.orderIds) ? params.orderIds : (params.orderId != null ? [params.orderId] : null)
    if (!ids || ids.length === 0) throw new TypeError('orderIds or orderId required')
    return this._privatePost(E.order.CANCEL, ids)
  }

  /**
   * Cancel all orders (under maintenance).
   * @endpoint POST /api/v1/private/order/cancel_all
   * @param {Object} [params] - symbol optional; omit to cancel all contracts
   */
  cancelAllOrders (params = {}) {
    return this._privatePost(E.order.CANCEL_ALL, params)
  }

  /**
   * Batch cancel by external order id (under maintenance).
   * @endpoint POST /api/v1/private/order/batch_cancel_with_external
   * @param {Object} params - array [{ symbol, externalOid }]
   */
  batchCancelWithExternal (params) {
    if (!params || !Array.isArray(params) || params.length === 0) throw new TypeError('params must be array of { symbol, externalOid }')
    return this._privatePost(E.order.BATCH_CANCEL_WITH_EXTERNAL, params)
  }

  /**
   * Cancel by external order id (under maintenance).
   * @endpoint POST /api/v1/private/order/cancel_with_external
   * @param {Object} params - symbol, externalOid
   */
  cancelWithExternal (params) {
    if (!params || !params.symbol || !params.externalOid) throw new TypeError('symbol and externalOid required')
    return this._privatePost(E.order.CANCEL_WITH_EXTERNAL, [params])
  }

  /**
   * Chase limit order (under maintenance): move order price to one level.
   * @endpoint POST /api/v1/private/order/chase_limit_order
   * @param {Object} params - orderId
   */
  chaseLimitOrder (params) {
    if (!params || params.orderId == null) throw new TypeError('orderId required')
    return this._privatePost(E.order.CHASE_LIMIT_ORDER, params)
  }

  /**
   * Change limit order price and volume (under maintenance).
   * @endpoint POST /api/v1/private/order/change_limit_order
   * @param {Object} params - orderId, price, vol
   */
  changeLimitOrder (params) {
    if (!params || params.orderId == null || params.price == null || params.vol == null) throw new TypeError('orderId, price, vol required')
    return this._privatePost(E.order.CHANGE_LIMIT_ORDER, params)
  }

  /**
   * Open order total count.
   * @endpoint POST /api/v1/private/order/open_order_total_count
   * @param {Object} [params] - TODO doc open_order_total_count params vs description inconsistent
   */
  getOpenOrderTotalCount (params = {}) {
    return this._privatePost(E.order.OPEN_ORDER_TOTAL_COUNT, params)
  }

  /**
   * Order detail by order id.
   * @endpoint GET /api/v1/private/order/get/{orderId}
   * @param {Object} params - orderId
   */
  getOrderDetail (params) {
    if (!params || params.orderId == null) throw new TypeError('orderId required')
    const path = `${E.order.ORDER_GET}/${params.orderId}`
    return this._private(path)
  }

  /**
   * Order by external id.
   * @endpoint GET /api/v1/private/order/external/{symbol}/{external_oid}
   * @param {Object} params - symbol, external_oid
   */
  getOrderByExternal (params) {
    if (!params || !params.symbol || !params.external_oid) throw new TypeError('symbol and external_oid required')
    const path = `${E.order.ORDER_EXTERNAL}/${params.symbol}/${params.external_oid}`
    return this._private(path)
  }

  /**
   * Batch query orders.
   * @endpoint GET /api/v1/private/order/batch_query
   * @param {Object} params - order_ids comma-separated or array, max 50
   */
  batchQueryOrders (params) {
    if (!params || (params.order_ids == null && !params.orderIds)) throw new TypeError('order_ids required')
    const p = { ...params }
    if (Array.isArray(p.order_ids)) p.order_ids = p.order_ids.join(',')
    return this._private(E.order.BATCH_QUERY, p)
  }

  /**
   * Batch query by external order id.
   * @endpoint POST /api/v1/private/order/batch_query_with_external
   * @param {Object} params - array [{ symbol, externalOid }]
   */
  batchQueryWithExternal (params) {
    if (!params || !Array.isArray(params) || params.length === 0) throw new TypeError('params must be array of { symbol, externalOid }')
    return this._privatePost(E.order.BATCH_QUERY_WITH_EXTERNAL, params)
  }

  /**
   * Open orders (current orders).
   * @endpoint GET /api/v1/private/order/list/open_orders
   * @param {Object} [params] - page_num, page_size (default 1, 20)
   */
  getOpenOrders (params = {}) {
    const p = { page_num: params.page_num ?? 1, page_size: params.page_size ?? 20 }
    _checkPage(p.page_num, p.page_size)
    return this._private(E.order.LIST_OPEN_ORDERS, p)
  }

  /**
   * Close orders (closed / liquidated etc).
   * @endpoint GET /api/v1/private/order/list/close_orders
   * @param {Object} params - symbol (required), start_time?, end_time?, page_num?, page_size?
   */
  getCloseOrders (params = {}) {
    if (!params.symbol) throw new TypeError('params.symbol required')
    const p = _cleanQuery({ ...params, page_num: params.page_num ?? 1, page_size: params.page_size ?? 20 })
    _checkPage(p.page_num, p.page_size, 1000)
    return this._private(E.order.LIST_CLOSE_ORDERS, p)
  }

  /**
   * History orders.
   * @endpoint GET /api/v1/private/order/list/history_orders
   * @param {Object} params - symbol?, states?, category?, startTime?, endTime?, page_num, page_size, orderId?
   */
  getHistoryOrders (params) {
    const p = params || {}
    _checkPage(p.page_num, p.page_size)
    return this._private(E.order.LIST_HISTORY_ORDERS, p)
  }

  /**
   * Order list (alias for getHistoryOrders).
   * @endpoint GET /api/v1/private/order/list/history_orders
   * @param {Object} params - symbol?, states?, page_num, page_size, etc.
   */
  getOrderList (params = {}) {
    _checkPage(params.page_num, params.page_size)
    const p = { ...params }
    if (p.state != null && p.states == null) p.states = String(p.state)
    return this._private(E.order.LIST_HISTORY_ORDERS, p)
  }

  /**
   * Order deal list (history).
   * @endpoint GET /api/v1/private/order/list/order_deals/v3
   * @param {Object} params - symbol, start_time?, end_time?, page_num, page_size
   */
  getOrderDealList (params) {
    if (!params || !params.symbol) throw new TypeError('symbol required')
    _checkPage(params.page_num, params.page_size)
    return this._private(E.order.LIST_ORDER_DEALS_V3, params)
  }

  /**
   * Single order deal details.
   * @endpoint GET /api/v1/private/order/deal_details/{orderId}
   * @param {Object} params - symbol, order_id or orderId
   */
  getOrderDealDetails (params) {
    if (!params || !params.symbol) throw new TypeError('symbol required')
    const oid = params.order_id ?? params.orderId
    if (oid == null) throw new TypeError('order_id or orderId required')
    const path = `${E.order.DEAL_DETAILS}/${oid}`
    return this._private(path, { symbol: params.symbol })
  }

  /**
   * Order fee details.
   * @endpoint GET /api/v1/private/order/fee_details
   * @param {Object} params - symbol, ids?, start_time?, end_time?, page_num?, page_size?
   */
  getOrderFeeDetails (params) {
    if (!params || !params.symbol) throw new TypeError('symbol required')
    return this._private(E.order.FEE_DETAILS, params)
  }

  // ----- position extension (under maintenance) -----

  /**
   * Change position margin (under maintenance).
   * @endpoint POST /api/v1/private/position/change_margin
   * @param {Object} params - positionId, amount, type (ADD|SUB)
   */
  changeMargin (params) {
    if (!params || params.positionId == null || params.amount == null || !params.type) throw new TypeError('positionId, amount, type required')
    return this._privatePost(E.position.CHANGE_MARGIN, params)
  }

  /**
   * Enable/disable auto add margin (under maintenance).
   * @endpoint POST /api/v1/private/position/change_auto_add_im
   * @param {Object} params - positionId, isEnabled
   */
  changeAutoAddIm (params) {
    if (!params || params.positionId == null || params.isEnabled == null) throw new TypeError('positionId, isEnabled required')
    return this._privatePost(E.position.CHANGE_AUTO_ADD_IM, params)
  }

  /**
   * Change leverage (under maintenance).
   * @endpoint POST /api/v1/private/position/change_leverage
   * @param {Object} params - leverage, positionId?, openType?, symbol?, positionType?, leverageMode?, marginSelected?, leverageSelected?
   */
  changeLeverage (params) {
    if (!params || params.leverage == null) throw new TypeError('leverage required')
    return this._privatePost(E.position.CHANGE_LEVERAGE, params)
  }

  /**
   * Position mode.
   * @endpoint GET /api/v1/private/position/position_mode
   */
  getPositionMode () {
    return this._private(E.position.POSITION_MODE)
  }

  /**
   * Change position mode (under maintenance).
   * @endpoint POST /api/v1/private/position/change_position_mode
   * @param {Object} params - positionMode (1: hedge, 2: one-way)
   */
  changePositionMode (params) {
    if (!params || params.positionMode == null) throw new TypeError('positionMode required')
    return this._privatePost(E.position.CHANGE_POSITION_MODE, params)
  }

  /**
   * Reverse position (under maintenance).
   * @endpoint POST /api/v1/private/position/reverse
   * @param {Object} params - symbol, positionId, vol
   */
  reversePosition (params) {
    if (!params || !params.symbol || params.positionId == null || params.vol == null) throw new TypeError('symbol, positionId, vol required')
    return this._privatePost(E.position.REVERSE, params)
  }

  /**
   * Close all positions (under maintenance).
   * @endpoint POST /api/v1/private/position/close_all
   * @param {Object} [params]
   */
  closeAllPositions (params = {}) {
    return this._privatePost(E.position.CLOSE_ALL, params)
  }

  // ----- account extension -----

  /**
   * Asset analysis.
   * @endpoint GET /api/v1/private/account/asset/analysis/{type}
   * @param {Object} params - currency, type (1: this week, 2: this month, 3: all, 4: custom), startTime?, endTime?
   */
  getAssetAnalysis (params) {
    if (!params || !params.currency || params.type == null) throw new TypeError('currency, type required')
    const path = `${E.account.ASSET_ANALYSIS}/${params.type}`
    return this._private(path, { currency: params.currency, startTime: params.startTime, endTime: params.endTime })
  }

  /**
   * Asset analysis v3.
   * @endpoint POST /api/v1/private/account/asset/analysis/v3
   * @param {Object} params - startTime, endTime, reverse?, includeUnrealisedPnl?, symbol?
   */
  getAssetAnalysisV3 (params) {
    if (!params || params.startTime == null || params.endTime == null) throw new TypeError('startTime, endTime required')
    return this._privatePost(E.account.ASSET_ANALYSIS_V3, params)
  }

  /**
   * Asset analysis calendar daily.
   * @endpoint POST /api/v1/private/account/asset/analysis/calendar/daily/v3
   * @param {Object} params - startTime, endTime, reverse?, includeUnrealisedPnl?
   */
  getAssetAnalysisCalendarDaily (params) {
    if (!params || params.startTime == null || params.endTime == null) throw new TypeError('startTime, endTime required')
    return this._privatePost(E.account.ASSET_ANALYSIS_CALENDAR_DAILY_V3, params)
  }

  /**
   * Asset analysis calendar monthly.
   * @endpoint POST /api/v1/private/account/asset/analysis/calendar/monthly/v3
   * @param {Object} [params] - reverse?, includeUnrealisedPnl?
   */
  getAssetAnalysisCalendarMonthly (params = {}) {
    return this._privatePost(E.account.ASSET_ANALYSIS_CALENDAR_MONTHLY_V3, params)
  }

  /**
   * Asset analysis recent.
   * @endpoint POST /api/v1/private/account/asset/analysis/recent/v3
   * @param {Object} [params] - reverse?, includeUnrealisedPnl?, symbol?
   */
  getAssetAnalysisRecent (params = {}) {
    return this._privatePost(E.account.ASSET_ANALYSIS_RECENT_V3, params)
  }

  /**
   * Asset analysis export. Pass timezone-login in Header.
   * @endpoint GET /api/v1/private/account/asset/analysis/export
   * @param {Object} params - startTime, endTime, reverse?, includeUnrealisedPnl?, symbol?, fileType?, language?
   * @param {Object} [options] - headers: { 'timezone-login': 'UTC+08:00' }
   */
  getAssetAnalysisExport (params = {}, options = {}) {
    if (params.startTime == null || params.endTime == null) throw new TypeError('params.startTime, params.endTime required')
    const reqOpts = options.headers ? { headers: options.headers } : {}
    return this._private(E.account.ASSET_ANALYSIS_EXPORT, _cleanQuery(params), 'GET', reqOpts)
  }

  /**
   * Contract fee discount config.
   * @endpoint GET /api/v1/private/account/config/contractFeeDiscountConfig
   */
  getContractFeeDiscountConfig () {
    return this._private(E.account.CONTRACT_FEE_DISCOUNT_CONFIG)
  }

  /**
   * Order deal fee total (last 30 days).
   * @endpoint GET /api/v1/private/account/asset_book/order_deal_fee/total
   */
  getOrderDealFeeTotal () {
    return this._private(E.account.ORDER_DEAL_FEE_TOTAL)
  }

  /**
   * Zero fee rate pairs.
   * @endpoint GET /api/v1/private/account/contract/zero_fee_rate
   * @param {Object} [params] - symbol?
   */
  getZeroFeeRate (params = {}) {
    return this._private(E.account.ZERO_FEE_RATE, params)
  }

  /**
   * Change risk level (under maintenance, disabled).
   * @endpoint POST /api/v1/private/account/change_risk_level
   * @param {Object} params
   */
  changeRiskLevel (params) {
    return this._privatePost(E.account.CHANGE_RISK_LEVEL, params || {})
  }

  // ----- plan / stop / track -----

  /**
   * Plan order list.
   * @endpoint GET /api/v1/private/planorder/list/orders
   * @param {Object} params - start_time, end_time, page_num, page_size, symbol?, states?, side?
   */
  getPlanOrders (params = {}) {
    const p = params || {}
    if (p.start_time == null || p.end_time == null) throw new TypeError('params.start_time, params.end_time required')
    _checkPage(p.page_num ?? 1, p.page_size ?? 20)
    return this._private(E.plan.LIST_ORDERS, _cleanQuery({ ...p, page_num: p.page_num ?? 1, page_size: p.page_size ?? 20 }))
  }

  /**
   * Place plan order.
   * @endpoint POST /api/v1/private/planorder/place/v2
   * @param {Object} params
   */
  placePlanOrder (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.plan.PLACE_V2, params)
  }

  /**
   * Change plan order price.
   * @endpoint POST /api/v1/private/planorder/change_price
   * @param {Object} params
   */
  changePlanOrderPrice (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.plan.CHANGE_PRICE, params)
  }

  /**
   * Cancel plan order (under maintenance).
   * @endpoint POST /api/v1/private/planorder/cancel
   * @param {Object} params - orders: [{ symbol, orderId }] or array
   */
  cancelPlanOrder (params) {
    const list = Array.isArray(params) ? params : (params?.orders || [])
    if (!list.length) throw new TypeError('params.orders or array of { symbol, orderId } required')
    return this._privatePost(E.plan.CANCEL, list)
  }

  /**
   * Cancel all plan orders.
   * @endpoint POST /api/v1/private/planorder/cancel_all
   * @param {Object} [params]
   */
  cancelAllPlanOrders (params = {}) {
    return this._privatePost(E.plan.CANCEL_ALL, params)
  }

  /**
   * Change plan stop order (TP/SL).
   * @endpoint POST /api/v1/private/planorder/change_stop_order
   * @param {Object} params
   */
  changePlanStopOrder (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.plan.CHANGE_STOP_ORDER, params)
  }

  /**
   * Place stop order (TP/SL).
   * @endpoint POST /api/v1/private/stoporder/place
   * @param {Object} params
   */
  placeStopOrder (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.stop.PLACE, params)
  }

  /**
   * Cancel stop order (under maintenance).
   * @endpoint POST /api/v1/private/stoporder/cancel
   * @param {Object} params - orders: [{ stopPlanOrderId }] or array
   */
  cancelStopOrder (params) {
    const list = Array.isArray(params) ? params : (params?.orders || [])
    if (!list.length) throw new TypeError('params.orders or array of { stopPlanOrderId } required')
    return this._privatePost(E.stop.CANCEL, list)
  }

  /**
   * Cancel all stop orders.
   * @endpoint POST /api/v1/private/stoporder/cancel_all
   * @param {Object} [params]
   */
  cancelAllStopOrders (params = {}) {
    return this._privatePost(E.stop.CANCEL_ALL, params)
  }

  /**
   * Change stop order price.
   * @endpoint POST /api/v1/private/stoporder/change_price
   * @param {Object} params
   */
  changeStopOrderPrice (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.stop.CHANGE_PRICE, params)
  }

  /**
   * Change stop plan price.
   * @endpoint POST /api/v1/private/stoporder/change_plan_price
   * @param {Object} params
   */
  changeStopPlanPrice (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.stop.CHANGE_PLAN_PRICE, params)
  }

  /**
   * Stop order list.
   * @endpoint GET /api/v1/private/stoporder/list/orders
   * @param {Object} params - page_num, page_size, symbol?, is_finished?, state?, type?, start_time?, end_time?
   */
  getStopOrders (params = {}) {
    const p = { page_num: params.page_num ?? 1, page_size: params.page_size ?? 20 }
    _checkPage(p.page_num, p.page_size)
    return this._private(E.stop.LIST_ORDERS, _cleanQuery({ ...params, ...p }))
  }

  /**
   * Stop open orders (current).
   * @endpoint GET /api/v1/private/stoporder/open_orders
   * @param {Object} [params] - symbol?
   */
  getStopOpenOrders (params = {}) {
    return this._private(E.stop.OPEN_ORDERS, _cleanQuery(params || {}))
  }

  /**
   * Place track order.
   * @endpoint POST /api/v1/private/trackorder/place
   * @param {Object} params
   */
  placeTrackOrder (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.track.PLACE, params)
  }

  /**
   * Cancel track order.
   * @endpoint POST /api/v1/private/trackorder/cancel
   * @param {Object} params
   */
  cancelTrackOrder (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.track.CANCEL, params)
  }

  /**
   * Change track order.
   * @endpoint POST /api/v1/private/trackorder/change_order
   * @param {Object} params
   */
  changeTrackOrder (params) {
    if (!params) throw new TypeError('params required')
    return this._privatePost(E.track.CHANGE_ORDER, params)
  }

  /**
   * Track order list.
   * @endpoint GET /api/v1/private/trackorder/list/orders
   * @param {Object} [params]
   */
  getTrackOrders (params = {}) {
    return this._private(E.track.LIST_ORDERS, params)
  }

  // ----- stp (self-trade prevention, market maker only) -----

  /**
   * Query STP groups and group members.
   * @endpoint GET /api/v1/private/market_maker/self_trade/blacklist
   * @param {Object} [params] - configName (optional, blacklist group id)
   */
  getStpGroups (params = {}) {
    return this._private(E.stp.BLACKLIST, _cleanQuery(params))
  }

  /**
   * Get current user STP group.
   * @endpoint GET /api/v1/private/market_maker/self_trade/blacklist/search
   */
  getCurrentUserStpGroup () {
    return this._private(E.stp.BLACKLIST_SEARCH)
  }

  /**
   * Create STP group.
   * @endpoint POST /api/v1/private/market_maker/self_trade/blacklist/create
   * @param {Object} params - configName (required), blacklist (required, sub-account UID array)
   */
  createStpGroup (params) {
    if (!params || !params.configName) throw new TypeError('params.configName required')
    if (!params.blacklist || !Array.isArray(params.blacklist)) throw new TypeError('params.blacklist required and must be array')
    return this._privatePost(E.stp.BLACKLIST_CREATE, { configName: params.configName, blacklist: params.blacklist })
  }

  /**
   * Update STP group.
   * @endpoint POST /api/v1/private/market_maker/self_trade/blacklist/update
   * @param {Object} params - configName (required), blacklist (required, sub-account UID array)
   */
  updateStpGroup (params) {
    if (!params || !params.configName) throw new TypeError('params.configName required')
    if (!params.blacklist || !Array.isArray(params.blacklist)) throw new TypeError('params.blacklist required and must be array')
    return this._privatePost(E.stp.BLACKLIST_UPDATE, { configName: params.configName, blacklist: params.blacklist })
  }

  /**
   * Delete STP group.
   * @endpoint POST /api/v1/private/market_maker/self_trade/blacklist/delete
   * @param {Object} params - configName (required)
   */
  deleteStpGroup (params) {
    if (!params || !params.configName) throw new TypeError('params.configName required')
    return this._privatePost(E.stp.BLACKLIST_DELETE, { configName: params.configName })
  }
}

module.exports = { MexcFuturesRestClient }
