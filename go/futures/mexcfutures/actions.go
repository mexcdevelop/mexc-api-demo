package mexcfutures

import (
	"context"
	"fmt"
)

// registerAllActions registers all action handlers into m (called from action_runner.initActionHandlers).
func registerAllActions(m map[string]actionHandler) {
	// ----- public -----
	m["ping"] = handlePing
	m["ticker"] = handleTicker
	m["depth"] = handleDepth
	m["contract_detail"] = handleContractDetail
	m["support_currencies"] = handleSupportCurrencies
	m["depth_commits"] = handleDepthCommits
	m["index_price"] = handleIndexPrice
	m["fair_price"] = handleFairPrice
	m["funding_rate"] = handleFundingRate
	m["funding_rate_history"] = handleFundingRateHistory
	m["kline"] = handleKline
	m["kline_index"] = handleKlineIndex
	m["kline_fair"] = handleKlineFair
	m["deals"] = handleDeals
	m["risk_reverse"] = handleRiskReverse
	m["risk_reverse_history"] = handleRiskReverseHistory
	// ----- private account -----
	m["assets"] = handleAssets
	m["asset"] = handleAsset
	m["transfer_records"] = handleTransferRecords
	m["profit_rate"] = handleProfitRate
	m["yesterday_pnl"] = handleYesterdayPnl
	m["today_pnl"] = handleTodayPnl
	m["fee_deduct_configs"] = handleFeeDeductConfigs
	m["risk_limit"] = handleRiskLimit
	m["fee_rate"] = handleFeeRate
	m["tiered_fee_rate"] = handleTieredFeeRate
	m["discount_type"] = handleDiscountType
	m["asset_analysis"] = handleAssetAnalysis
	m["asset_analysis_v3"] = handleAssetAnalysisV3
	m["asset_analysis_calendar_daily"] = handleAssetAnalysisCalendarDaily
	m["asset_analysis_calendar_monthly"] = handleAssetAnalysisCalendarMonthly
	m["asset_analysis_recent"] = handleAssetAnalysisRecent
	m["asset_analysis_export"] = handleAssetAnalysisExport
	m["contract_fee_discount_config"] = handleContractFeeDiscountConfig
	m["order_deal_fee_total"] = handleOrderDealFeeTotal
	m["zero_fee_rate"] = handleZeroFeeRate
	m["change_risk_level"] = handleChangeRiskLevel
	// ----- private position -----
	m["positions"] = handlePositions
	m["history_positions"] = handleHistoryPositions
	m["leverage"] = handleLeverage
	m["funding_records"] = handleFundingRecords
	m["change_margin"] = handleChangeMargin
	m["change_auto_add_im"] = handleChangeAutoAddIm
	m["change_leverage"] = handleChangeLeverage
	m["position_mode"] = handlePositionMode
	m["change_position_mode"] = handleChangePositionMode
	m["reverse_position"] = handleReversePosition
	m["close_all_positions"] = handleCloseAllPositions
	// ----- private order -----
	m["submit_order"] = handleSubmitOrder
	m["submit_order_batch"] = handleSubmitOrderBatch
	m["cancel_order"] = handleCancelOrder
	m["cancel_all_orders"] = handleCancelAllOrders
	m["order_detail"] = handleOrderDetail
	m["open_orders"] = handleOpenOrders
	m["history_orders"] = handleHistoryOrders
	m["order_deal_list"] = handleOrderDealList
	m["batch_cancel_with_external"] = handleBatchCancelWithExternal
	m["cancel_with_external"] = handleCancelWithExternal
	m["chase_limit_order"] = handleChaseLimitOrder
	m["change_limit_order"] = handleChangeLimitOrder
	m["open_order_total_count"] = handleOpenOrderTotalCount
	m["order_by_external"] = handleOrderByExternal
	m["batch_query_orders"] = handleBatchQueryOrders
	m["batch_query_with_external"] = handleBatchQueryWithExternal
	m["close_orders"] = handleCloseOrders
	m["order_deal_details"] = handleOrderDealDetails
	m["order_fee_details"] = handleOrderFeeDetails
	// ----- private plan order -----
	m["plan_orders"] = handlePlanOrders
	m["place_plan_order"] = handlePlacePlanOrder
	m["change_plan_order_price"] = handleChangePlanOrderPrice
	m["cancel_plan_order"] = handleCancelPlanOrder
	m["cancel_all_plan_orders"] = handleCancelAllPlanOrders
	m["change_plan_stop_order"] = handleChangePlanStopOrder
	// ----- private stop order -----
	m["place_stop_order"] = handlePlaceStopOrder
	m["cancel_stop_order"] = handleCancelStopOrder
	m["cancel_all_stop_orders"] = handleCancelAllStopOrders
	m["change_stop_order_price"] = handleChangeStopOrderPrice
	m["change_stop_plan_price"] = handleChangeStopPlanPrice
	m["stop_orders"] = handleStopOrders
	m["stop_open_orders"] = handleStopOpenOrders
	// ----- private track order -----
	m["place_track_order"] = handlePlaceTrackOrder
	m["cancel_track_order"] = handleCancelTrackOrder
	m["change_track_order"] = handleChangeTrackOrder
	m["track_orders"] = handleTrackOrders
	// ----- private stp -----
	m["stp_list"] = handleStpList
	m["create_stp"] = handleCreateStp
	m["update_stp"] = handleUpdateStp
	m["delete_stp"] = handleDeleteStp
}

// ----- public -----

func handlePing(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.Ping(ctx)
}

func handleTicker(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", defaultSymbol)
	return c.GetTicker(ctx, symbol)
}

func handleDepth(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", defaultSymbol)
	limit := paramInt(params, "limit", defaultDepthLimit)
	return c.GetDepth(ctx, symbol, limit)
}

func handleContractDetail(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	return c.GetContractDetail(ctx, symbol)
}

func handleSupportCurrencies(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetSupportCurrencies(ctx)
}

func handleDepthCommits(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for depth_commits")
	}
	limit := paramInt(params, "limit", defaultDepthLimit)
	return c.GetDepthCommits(ctx, symbol, limit)
}

func handleIndexPrice(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", defaultSymbol)
	return c.GetIndexPrice(ctx, symbol)
}

func handleFairPrice(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", defaultSymbol)
	return c.GetFairPrice(ctx, symbol)
}

func handleFundingRate(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", defaultSymbol)
	return c.GetFundingRate(ctx, symbol)
}

func handleFundingRateHistory(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for funding_rate_history")
	}
	pn := paramInt(params, "page_num", 1)
	ps := paramInt(params, "page_size", 20)
	p := FundingRateHistoryParams{Symbol: symbol, PageNum: pn, PageSize: ps}
	return c.GetFundingRateHistory(ctx, p)
}

func handleKline(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for kline")
	}
	return c.GetKline(ctx, symbol, klineParamsFromMap(params))
}

func handleKlineIndex(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for kline_index")
	}
	return c.GetKlineIndexPrice(ctx, symbol, klineParamsFromMap(params))
}

func handleKlineFair(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for kline_fair")
	}
	return c.GetKlineFairPrice(ctx, symbol, klineParamsFromMap(params))
}

func handleDeals(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for deals")
	}
	limit := paramInt(params, "limit", 100)
	return c.GetDeals(ctx, symbol, limit)
}

func handleRiskReverse(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	return c.GetRiskReverse(ctx, symbol)
}

func handleRiskReverseHistory(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for risk_reverse_history")
	}
	pn := paramInt(params, "page_num", 1)
	ps := paramInt(params, "page_size", 20)
	p := RiskReverseHistoryParams{Symbol: symbol, PageNum: pn, PageSize: ps}
	return c.GetRiskReverseHistory(ctx, p)
}

// ----- private account -----

func handleAssets(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssets(ctx)
}

func handleAsset(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	currency := paramStr(params, "currency", "")
	if currency == "" {
		return nil, fmt.Errorf("params.currency required for asset")
	}
	return c.GetAsset(ctx, map[string]any{"currency": currency})
}

func handleTransferRecords(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetTransferRecords(ctx, params)
}

func handleProfitRate(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	typ := paramInt(params, "type", 0)
	if typ == 0 {
		return nil, fmt.Errorf("params.type required for profit_rate (1: day, 2: week)")
	}
	return c.GetProfitRate(ctx, map[string]any{"type": typ})
}

func handleYesterdayPnl(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetYesterdayPnl(ctx)
}

func handleTodayPnl(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetTodayPnl(ctx, params)
}

func handleFeeDeductConfigs(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetFeeDeductConfigs(ctx)
}

func handleRiskLimit(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetRiskLimit(ctx, params)
}

func handleFeeRate(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetFeeRate(ctx, params)
}

func handleTieredFeeRate(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetTieredFeeRate(ctx, params)
}

func handleDiscountType(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetDiscountType(ctx)
}

func handleAssetAnalysis(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssetAnalysis(ctx, params)
}

func handleAssetAnalysisV3(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssetAnalysisV3(ctx, params)
}

func handleAssetAnalysisCalendarDaily(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssetAnalysisCalendarDaily(ctx, params)
}

func handleAssetAnalysisCalendarMonthly(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssetAnalysisCalendarMonthly(ctx, params)
}

func handleAssetAnalysisRecent(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssetAnalysisRecent(ctx, params)
}

func handleAssetAnalysisExport(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetAssetAnalysisExport(ctx, params, nil)
}

func handleContractFeeDiscountConfig(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetContractFeeDiscountConfig(ctx)
}

func handleOrderDealFeeTotal(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetOrderDealFeeTotal(ctx)
}

func handleZeroFeeRate(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetZeroFeeRate(ctx, params)
}

func handleChangeRiskLevel(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeRiskLevel(ctx, params)
}

// ----- private position -----

func handlePositions(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	p := map[string]any{}
	if params != nil {
		if s := paramStr(params, "symbol", ""); s != "" {
			p["symbol"] = s
		}
	}
	return c.GetPositions(ctx, p)
}

func handleHistoryPositions(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetHistoryPositions(ctx, params)
}

func handleLeverage(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for leverage")
	}
	return c.GetLeverage(ctx, params)
}

func handleFundingRecords(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetFundingRecords(ctx, params)
}

func handleChangeMargin(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeMargin(ctx, params)
}

func handleChangeAutoAddIm(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeAutoAddIm(ctx, params)
}

func handleChangeLeverage(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeLeverage(ctx, params)
}

func handlePositionMode(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetPositionMode(ctx)
}

func handleChangePositionMode(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangePositionMode(ctx, params)
}

func handleReversePosition(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ReversePosition(ctx, params)
}

func handleCloseAllPositions(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CloseAllPositions(ctx, params)
}

// ----- private order -----

func handleSubmitOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("submit_order requires params.symbol, params.vol, params.side, params.type, params.openType")
	}
	if !hasParam(params, "symbol") || paramStrOrFormat(params, "symbol") == "" {
		return nil, fmt.Errorf("submit_order requires params.symbol")
	}
	if !hasParam(params, "vol") {
		return nil, fmt.Errorf("submit_order requires params.vol")
	}
	if !hasParam(params, "side") {
		return nil, fmt.Errorf("submit_order requires params.side")
	}
	if !hasParam(params, "type") {
		return nil, fmt.Errorf("submit_order requires params.type")
	}
	if !hasParam(params, "openType") {
		return nil, fmt.Errorf("submit_order requires params.openType")
	}
	vol, _, err := parseSubmitOrderNumber(params, "vol")
	if err != nil {
		return nil, err
	}
	out := make(map[string]any)
	for k, v := range params {
		out[k] = v
	}
	out["symbol"] = paramStrOrFormat(params, "symbol")
	out["side"] = paramStrOrFormat(params, "side")
	out["type"] = paramStrOrFormat(params, "type")
	out["openType"] = paramStrOrFormat(params, "openType")
	out["vol"] = vol
	if price, ok, err := parseSubmitOrderNumber(params, "price"); err != nil {
		return nil, err
	} else if ok {
		out["price"] = price
	}
	if leverage, ok, err := parseSubmitOrderNumber(params, "leverage"); err != nil {
		return nil, err
	} else if ok {
		out["leverage"] = leverage
	}
	return c.SubmitOrder(ctx, out)
}

func handleSubmitOrderBatch(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("submit_order_batch requires params.batchOrder (array) or pass array as params")
	}
	var body interface{}
	if bo, ok := params["batchOrder"]; ok && bo != nil {
		sl, ok := toSlice(bo)
		if !ok || len(sl) == 0 {
			return nil, fmt.Errorf("submit_order_batch params.batchOrder must be non-empty array")
		}
		body = sl
	} else {
		return nil, fmt.Errorf("submit_order_batch requires params.batchOrder (array)")
	}
	return c.SubmitOrderBatch(ctx, body)
}

func handleCancelOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	ids, err := cancelOrderIDsFromParams(params)
	if err != nil {
		return nil, err
	}
	var p map[string]any
	if len(ids) == 1 {
		p = map[string]any{"orderId": ids[0]}
	} else {
		sl := make([]interface{}, len(ids))
		for i := range ids {
			sl[i] = ids[i]
		}
		p = map[string]any{"orderIds": sl}
	}
	return c.CancelOrder(ctx, p)
}

func handleCancelAllOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CancelAllOrders(ctx, params)
}

func handleOrderDetail(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	orderId := paramInt64(params, "orderId", 0)
	if orderId == 0 {
		orderId = paramInt64(params, "order_id", 0)
	}
	if orderId == 0 {
		return nil, fmt.Errorf("params.orderId or order_id required for order_detail")
	}
	return c.GetOrderDetail(ctx, map[string]any{"orderId": orderId})
}

func handleOpenOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetOpenOrders(ctx, params)
}

func handleHistoryOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetHistoryOrders(ctx, params)
}

func handleOrderDealList(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required for order_deal_list")
	}
	return c.GetOrderDealList(ctx, params)
}

func handleBatchCancelWithExternal(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("batch_cancel_with_external requires params.list (non-empty array)")
	}
	raw, ok := params["list"]
	if !ok || raw == nil {
		return nil, fmt.Errorf("batch_cancel_with_external requires params.list (non-empty array of { symbol, externalOid })")
	}
	list, ok := toSlice(raw)
	if !ok || len(list) == 0 {
		return nil, fmt.Errorf("batch_cancel_with_external requires params.list (non-empty array)")
	}
	return c.BatchCancelWithExternal(ctx, list)
}

func handleCancelWithExternal(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CancelWithExternal(ctx, params)
}

func handleChaseLimitOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChaseLimitOrder(ctx, params)
}

func handleChangeLimitOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeLimitOrder(ctx, params)
}

func handleOpenOrderTotalCount(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetOpenOrderTotalCount(ctx, params)
}

func handleOrderByExternal(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	symbol := paramStr(params, "symbol", "")
	externalOid := paramStr(params, "external_oid", "")
	if symbol == "" || externalOid == "" {
		return nil, fmt.Errorf("params.symbol and params.external_oid required for order_by_external")
	}
	return c.GetOrderByExternal(ctx, map[string]any{"symbol": symbol, "external_oid": externalOid})
}

func handleBatchQueryOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.BatchQueryOrders(ctx, params)
}

func handleBatchQueryWithExternal(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("batch_query_with_external requires params.list (non-empty array)")
	}
	raw, ok := params["list"]
	if !ok || raw == nil {
		return nil, fmt.Errorf("batch_query_with_external requires params.list (non-empty array of { symbol, externalOid })")
	}
	list, ok := toSlice(raw)
	if !ok || len(list) == 0 {
		return nil, fmt.Errorf("batch_query_with_external requires params.list (non-empty array)")
	}
	return c.BatchQueryWithExternal(ctx, list)
}

func handleCloseOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetCloseOrders(ctx, params)
}

func handleOrderDealDetails(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetOrderDealDetails(ctx, params)
}

func handleOrderFeeDetails(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetOrderFeeDetails(ctx, params)
}

// ----- private plan order -----

func handlePlanOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("plan_orders requires params.start_time and params.end_time")
	}
	if !hasParam(params, "start_time") {
		return nil, fmt.Errorf("plan_orders requires params.start_time")
	}
	if !hasParam(params, "end_time") {
		return nil, fmt.Errorf("plan_orders requires params.end_time")
	}
	pn := paramInt(params, "page_num", 1)
	ps := paramInt(params, "page_size", 20)
	out := make(map[string]any)
	for k, v := range params {
		out[k] = v
	}
	out["page_num"] = pn
	out["page_size"] = ps
	return c.GetPlanOrders(ctx, out)
}

func handlePlacePlanOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.PlacePlanOrder(ctx, params)
}

func handleChangePlanOrderPrice(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangePlanOrderPrice(ctx, params)
}

func handleCancelPlanOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("cancel_plan_order requires params.orders (array) or pass array as params.orders")
	}
	if orders, ok := params["orders"]; ok && orders != nil {
		sl, ok := toSlice(orders)
		if ok && len(sl) > 0 {
			return c.CancelPlanOrder(ctx, sl)
		}
	}
	return nil, fmt.Errorf("cancel_plan_order requires params.orders (non-empty array)")
}

func handleCancelAllPlanOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CancelAllPlanOrders(ctx, params)
}

func handleChangePlanStopOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangePlanStopOrder(ctx, params)
}

// ----- private stop order -----

func handlePlaceStopOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.PlaceStopOrder(ctx, params)
}

func handleCancelStopOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("cancel_stop_order requires params.orders (array) or pass array as params.orders")
	}
	if orders, ok := params["orders"]; ok && orders != nil {
		sl, ok := toSlice(orders)
		if ok && len(sl) > 0 {
			return c.CancelStopOrder(ctx, sl)
		}
	}
	return nil, fmt.Errorf("cancel_stop_order requires params.orders (non-empty array)")
}

func handleCancelAllStopOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CancelAllStopOrders(ctx, params)
}

func handleChangeStopOrderPrice(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeStopOrderPrice(ctx, params)
}

func handleChangeStopPlanPrice(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeStopPlanPrice(ctx, params)
}

func handleStopOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetStopOrders(ctx, params)
}

func handleStopOpenOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetStopOpenOrders(ctx, params)
}

// ----- private track order -----

func handlePlaceTrackOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.PlaceTrackOrder(ctx, params)
}

func handleCancelTrackOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CancelTrackOrder(ctx, params)
}

func handleChangeTrackOrder(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.ChangeTrackOrder(ctx, params)
}

func handleTrackOrders(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetTrackOrders(ctx, params)
}

// ----- private stp -----

func handleStpList(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.GetStpList(ctx, params)
}

func handleCreateStp(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.CreateStp(ctx, params)
}

func handleUpdateStp(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.UpdateStp(ctx, params)
}

func handleDeleteStp(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error) {
	return c.DeleteStp(ctx, params)
}
