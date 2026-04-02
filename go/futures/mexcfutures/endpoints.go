package mexcfutures

// REST path constants for MEXC Futures API.
// See https://www.mexc.com/api-docs/futures/market-endpoints
// See https://www.mexc.com/api-docs/futures/account-and-trading-endpoints

// Market (public) paths.
const (
	PathPing               = "/api/v1/contract/ping"
	PathContractDetail     = "/api/v1/contract/detail"
	PathSupportCurrencies  = "/api/v1/contract/support_currencies"
	PathDepth              = "/api/v1/contract/depth"
	PathDepthCommits       = "/api/v1/contract/depth_commits"
	PathIndexPrice         = "/api/v1/contract/index_price"
	PathFairPrice          = "/api/v1/contract/fair_price"
	PathFundingRate        = "/api/v1/contract/funding_rate"
	PathFundingRateHistory = "/api/v1/contract/funding_rate/history"
	PathKline              = "/api/v1/contract/kline"
	PathKlineIndexPrice    = "/api/v1/contract/kline/index_price"
	PathKlineFairPrice     = "/api/v1/contract/kline/fair_price"
	PathDeals              = "/api/v1/contract/deals"
	PathTicker             = "/api/v1/contract/ticker"
	PathRiskReverse        = "/api/v1/contract/risk_reverse"
	PathRiskReverseHistory = "/api/v1/contract/risk_reverse/history"
)

// Account (private) paths.
const (
	PathAccountAssets                  = "/api/v1/private/account/assets"
	PathAccountAsset                   = "/api/v1/private/account/asset"
	PathTransferRecord                 = "/api/v1/private/account/transfer_record"
	PathProfitRate                     = "/api/v1/private/account/profit_rate"
	PathAssetAnalysis                  = "/api/v1/private/account/asset/analysis"
	PathAssetAnalysisV3                = "/api/v1/private/account/asset/analysis/v3"
	PathAssetAnalysisCalendarDailyV3   = "/api/v1/private/account/asset/analysis/calendar/daily/v3"
	PathAssetAnalysisCalendarMonthlyV3 = "/api/v1/private/account/asset/analysis/calendar/monthly/v3"
	PathAssetAnalysisRecentV3          = "/api/v1/private/account/asset/analysis/recent/v3"
	PathYesterdayPnl                   = "/api/v1/private/account/asset/analysis/yesterday_pnl"
	PathTodayPnl                       = "/api/v1/private/account/asset/analysis/today_pnl"
	PathAssetAnalysisExport            = "/api/v1/private/account/asset/analysis/export"
	PathFeeDeductConfigs               = "/api/v1/private/account/feeDeductConfigs"
	PathRiskLimit                      = "/api/v1/private/account/risk_limit"
	PathChangeRiskLevel                = "/api/v1/private/account/change_risk_level"
	PathContractFeeRate                = "/api/v1/private/account/contract/fee_rate"
	PathZeroFeeRate                    = "/api/v1/private/account/contract/zero_fee_rate"
	PathDiscountType                   = "/api/v1/private/account/discountType"
	PathContractFeeDiscountConfig      = "/api/v1/private/account/config/contractFeeDiscountConfig"
	PathOrderDealFeeTotal              = "/api/v1/private/account/asset_book/order_deal_fee/total"
	PathTieredFeeRate                  = "/api/v1/private/account/tiered_fee_rate/v2"
)

// Order (private) paths.
const (
	PathOrderCreate                  = "/api/v1/private/order/create"
	PathOrderSubmitBatch             = "/api/v1/private/order/submit_batch"
	PathOrderCancel                  = "/api/v1/private/order/cancel"
	PathOrderCancelAll               = "/api/v1/private/order/cancel_all"
	PathOrderBatchCancelWithExternal = "/api/v1/private/order/batch_cancel_with_external"
	PathOrderCancelWithExternal      = "/api/v1/private/order/cancel_with_external"
	PathOrderChaseLimitOrder         = "/api/v1/private/order/chase_limit_order"
	PathOrderChangeLimitOrder        = "/api/v1/private/order/change_limit_order"
	PathOrderOpenOrderTotalCount     = "/api/v1/private/order/open_order_total_count"
	PathOrderGet                     = "/api/v1/private/order/get"
	PathOrderExternal                = "/api/v1/private/order/external"
	PathOrderBatchQuery              = "/api/v1/private/order/batch_query"
	PathOrderBatchQueryWithExternal  = "/api/v1/private/order/batch_query_with_external"
	PathOrderListOpenOrders          = "/api/v1/private/order/list/open_orders"
	PathOrderListHistoryOrders       = "/api/v1/private/order/list/history_orders"
	PathOrderListCloseOrders         = "/api/v1/private/order/list/close_orders"
	PathOrderListOrderDealsV3        = "/api/v1/private/order/list/order_deals/v3"
	PathOrderDealDetails             = "/api/v1/private/order/deal_details"
	PathOrderFeeDetails              = "/api/v1/private/order/fee_details"
)

// Position (private) paths.
const (
	PathPositionOpenPositions      = "/api/v1/private/position/open_positions"
	PathPositionHistoryPositions   = "/api/v1/private/position/list/history_positions"
	PathPositionLeverage           = "/api/v1/private/position/leverage"
	PathPositionFundingRecords     = "/api/v1/private/position/funding_records"
	PathPositionMode               = "/api/v1/private/position/position_mode"
	PathPositionChangeMargin       = "/api/v1/private/position/change_margin"
	PathPositionChangeAutoAddIm    = "/api/v1/private/position/change_auto_add_im"
	PathPositionChangeLeverage     = "/api/v1/private/position/change_leverage"
	PathPositionChangePositionMode = "/api/v1/private/position/change_position_mode"
	PathPositionReverse            = "/api/v1/private/position/reverse"
	PathPositionCloseAll           = "/api/v1/private/position/close_all"
)

// Plan order (private) paths.
const (
	PathPlanListOrders      = "/api/v1/private/planorder/list/orders"
	PathPlanPlaceV2         = "/api/v1/private/planorder/place/v2"
	PathPlanChangePrice     = "/api/v1/private/planorder/change_price"
	PathPlanCancel          = "/api/v1/private/planorder/cancel"
	PathPlanCancelAll       = "/api/v1/private/planorder/cancel_all"
	PathPlanChangeStopOrder = "/api/v1/private/planorder/change_stop_order"
)

// Stop order (private) paths.
const (
	PathStopPlace           = "/api/v1/private/stoporder/place"
	PathStopCancel          = "/api/v1/private/stoporder/cancel"
	PathStopCancelAll       = "/api/v1/private/stoporder/cancel_all"
	PathStopChangePrice     = "/api/v1/private/stoporder/change_price"
	PathStopChangePlanPrice = "/api/v1/private/stoporder/change_plan_price"
	PathStopListOrders      = "/api/v1/private/stoporder/list/orders"
	PathStopOpenOrders      = "/api/v1/private/stoporder/open_orders"
)

// Track order (private) paths.
const (
	PathTrackPlace       = "/api/v1/private/trackorder/place"
	PathTrackCancel      = "/api/v1/private/trackorder/cancel"
	PathTrackChangeOrder = "/api/v1/private/trackorder/change_order"
	PathTrackListOrders  = "/api/v1/private/trackorder/list/orders"
)

// STP (self-trade prevention, market maker, private) paths.
const (
	PathStpBlacklist       = "/api/v1/private/market_maker/self_trade/blacklist"
	PathStpBlacklistSearch = "/api/v1/private/market_maker/self_trade/blacklist/search"
	PathStpBlacklistCreate = "/api/v1/private/market_maker/self_trade/blacklist/create"
	PathStpBlacklistUpdate = "/api/v1/private/market_maker/self_trade/blacklist/update"
	PathStpBlacklistDelete = "/api/v1/private/market_maker/self_trade/blacklist/delete"
)
