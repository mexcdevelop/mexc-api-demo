/**
 * MEXC Futures API path constants.
 * @see https://www.mexc.com/api-docs/futures/market-endpoints
 * @see https://www.mexc.com/api-docs/futures/account-and-trading-endpoints
 */

module.exports = {
  // ----- market (public) -----
  market: {
    PING: '/api/v1/contract/ping',
    CONTRACT_DETAIL: '/api/v1/contract/detail',
    SUPPORT_CURRENCIES: '/api/v1/contract/support_currencies',
    DEPTH: '/api/v1/contract/depth',
    DEPTH_COMMITS: '/api/v1/contract/depth_commits',
    INDEX_PRICE: '/api/v1/contract/index_price',
    FAIR_PRICE: '/api/v1/contract/fair_price',
    FUNDING_RATE: '/api/v1/contract/funding_rate',
    FUNDING_RATE_HISTORY: '/api/v1/contract/funding_rate/history',
    KLINE: '/api/v1/contract/kline',
    KLINE_INDEX_PRICE: '/api/v1/contract/kline/index_price',
    KLINE_FAIR_PRICE: '/api/v1/contract/kline/fair_price',
    DEALS: '/api/v1/contract/deals',
    TICKER: '/api/v1/contract/ticker',
    RISK_REVERSE: '/api/v1/contract/risk_reverse',
    RISK_REVERSE_HISTORY: '/api/v1/contract/risk_reverse/history'
  },

  // ----- account (private) -----
  account: {
    ASSETS: '/api/v1/private/account/assets',
    ASSET: '/api/v1/private/account/asset',
    TRANSFER_RECORD: '/api/v1/private/account/transfer_record',
    PROFIT_RATE: '/api/v1/private/account/profit_rate',
    ASSET_ANALYSIS: '/api/v1/private/account/asset/analysis',
    ASSET_ANALYSIS_V3: '/api/v1/private/account/asset/analysis/v3',
    ASSET_ANALYSIS_CALENDAR_DAILY_V3: '/api/v1/private/account/asset/analysis/calendar/daily/v3',
    ASSET_ANALYSIS_CALENDAR_MONTHLY_V3: '/api/v1/private/account/asset/analysis/calendar/monthly/v3',
    ASSET_ANALYSIS_RECENT_V3: '/api/v1/private/account/asset/analysis/recent/v3',
    YESTERDAY_PNL: '/api/v1/private/account/asset/analysis/yesterday_pnl',
    TODAY_PNL: '/api/v1/private/account/asset/analysis/today_pnl',
    ASSET_ANALYSIS_EXPORT: '/api/v1/private/account/asset/analysis/export',
    FEE_DEDUCT_CONFIGS: '/api/v1/private/account/feeDeductConfigs',
    RISK_LIMIT: '/api/v1/private/account/risk_limit',
    CHANGE_RISK_LEVEL: '/api/v1/private/account/change_risk_level',
    CONTRACT_FEE_RATE: '/api/v1/private/account/contract/fee_rate',
    ZERO_FEE_RATE: '/api/v1/private/account/contract/zero_fee_rate',
    DISCOUNT_TYPE: '/api/v1/private/account/discountType',
    CONTRACT_FEE_DISCOUNT_CONFIG: '/api/v1/private/account/config/contractFeeDiscountConfig',
    ORDER_DEAL_FEE_TOTAL: '/api/v1/private/account/asset_book/order_deal_fee/total',
    FEE_RATE: '/api/v1/private/account/contract/fee_rate',
    TIERED_FEE_RATE: '/api/v1/private/account/tiered_fee_rate/v2'
  },

  // ----- order (private) -----
  order: {
    CREATE: '/api/v1/private/order/create',
    SUBMIT_BATCH: '/api/v1/private/order/submit_batch',
    CANCEL: '/api/v1/private/order/cancel',
    CANCEL_ALL: '/api/v1/private/order/cancel_all',
    BATCH_CANCEL_WITH_EXTERNAL: '/api/v1/private/order/batch_cancel_with_external',
    CANCEL_WITH_EXTERNAL: '/api/v1/private/order/cancel_with_external',
    CHASE_LIMIT_ORDER: '/api/v1/private/order/chase_limit_order',
    CHANGE_LIMIT_ORDER: '/api/v1/private/order/change_limit_order',
    OPEN_ORDER_TOTAL_COUNT: '/api/v1/private/order/open_order_total_count',
    ORDER_GET: '/api/v1/private/order/get',
    ORDER_EXTERNAL: '/api/v1/private/order/external',
    BATCH_QUERY: '/api/v1/private/order/batch_query',
    BATCH_QUERY_WITH_EXTERNAL: '/api/v1/private/order/batch_query_with_external',
    LIST_OPEN_ORDERS: '/api/v1/private/order/list/open_orders',
    LIST_HISTORY_ORDERS: '/api/v1/private/order/list/history_orders',
    LIST_CLOSE_ORDERS: '/api/v1/private/order/list/close_orders',
    LIST_ORDER_DEALS_V3: '/api/v1/private/order/list/order_deals/v3',
    DEAL_DETAILS: '/api/v1/private/order/deal_details',
    FEE_DETAILS: '/api/v1/private/order/fee_details'
  },

  // ----- position (private) -----
  position: {
    OPEN_POSITIONS: '/api/v1/private/position/open_positions',
    HISTORY_POSITIONS: '/api/v1/private/position/list/history_positions',
    LEVERAGE: '/api/v1/private/position/leverage',
    FUNDING_RECORDS: '/api/v1/private/position/funding_records',
    POSITION_MODE: '/api/v1/private/position/position_mode',
    CHANGE_MARGIN: '/api/v1/private/position/change_margin',
    CHANGE_AUTO_ADD_IM: '/api/v1/private/position/change_auto_add_im',
    CHANGE_LEVERAGE: '/api/v1/private/position/change_leverage',
    CHANGE_POSITION_MODE: '/api/v1/private/position/change_position_mode',
    REVERSE: '/api/v1/private/position/reverse',
    CLOSE_ALL: '/api/v1/private/position/close_all'
  },

  plan: {
    LIST_ORDERS: '/api/v1/private/planorder/list/orders',
    PLACE_V2: '/api/v1/private/planorder/place/v2',
    CHANGE_PRICE: '/api/v1/private/planorder/change_price',
    CANCEL: '/api/v1/private/planorder/cancel',
    CANCEL_ALL: '/api/v1/private/planorder/cancel_all',
    CHANGE_STOP_ORDER: '/api/v1/private/planorder/change_stop_order'
  },

  stop: {
    PLACE: '/api/v1/private/stoporder/place',
    CANCEL: '/api/v1/private/stoporder/cancel',
    CANCEL_ALL: '/api/v1/private/stoporder/cancel_all',
    CHANGE_PRICE: '/api/v1/private/stoporder/change_price',
    CHANGE_PLAN_PRICE: '/api/v1/private/stoporder/change_plan_price',
    LIST_ORDERS: '/api/v1/private/stoporder/list/orders',
    OPEN_ORDERS: '/api/v1/private/stoporder/open_orders'
  },

  track: {
    PLACE: '/api/v1/private/trackorder/place',
    CANCEL: '/api/v1/private/trackorder/cancel',
    CHANGE_ORDER: '/api/v1/private/trackorder/change_order',
    LIST_ORDERS: '/api/v1/private/trackorder/list/orders'
  },

  // ----- stp (private, market maker) -----
  stp: {
    BLACKLIST: '/api/v1/private/market_maker/self_trade/blacklist',
    BLACKLIST_SEARCH: '/api/v1/private/market_maker/self_trade/blacklist/search',
    BLACKLIST_CREATE: '/api/v1/private/market_maker/self_trade/blacklist/create',
    BLACKLIST_UPDATE: '/api/v1/private/market_maker/self_trade/blacklist/update',
    BLACKLIST_DELETE: '/api/v1/private/market_maker/self_trade/blacklist/delete'
  },

  // Backward-compat aliases
  trade: {
    ORDER_CREATE: '/api/v1/private/order/create',
    ORDER_SUBMIT_BATCH: '/api/v1/private/order/submit_batch',
    ORDER_CANCEL: '/api/v1/private/order/cancel',
    ORDER_CANCEL_ALL: '/api/v1/private/order/cancel_all'
  }
}
