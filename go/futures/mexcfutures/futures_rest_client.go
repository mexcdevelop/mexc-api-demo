package mexcfutures

import (
	"context"
	"fmt"
	"strconv"
	"strings"
)

// FuturesRestClient is a REST client for MEXC Futures API (public and private endpoints).
type FuturesRestClient struct {
	http *HTTPClient
}

// NewFuturesRestClient creates a client with the given config (internally builds and holds HTTPClient).
func NewFuturesRestClient(cfg Config) *FuturesRestClient {
	return &FuturesRestClient{http: NewHTTPClient(cfg)}
}

// Ping checks connectivity to the Futures API.
// @endpoint GET /api/v1/contract/ping
func (c *FuturesRestClient) Ping(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PublicRequest(ctx, "GET", PathPing, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetTicker returns ticker for the given symbol or all contracts when symbol is empty.
// @endpoint GET /api/v1/contract/ticker
func (c *FuturesRestClient) GetTicker(ctx context.Context, symbol string) (*RawResponse, error) {
	params := map[string]interface{}{}
	if symbol != "" {
		params["symbol"] = symbol
	}
	resp, err := c.http.PublicRequest(ctx, "GET", PathTicker, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetDepth returns order book depth for the given symbol and limit (optional; <=0 uses API default 20).
// @endpoint GET /api/v1/contract/depth
func (c *FuturesRestClient) GetDepth(ctx context.Context, symbol string, limit int) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	if limit <= 0 {
		limit = 20
	}
	if err := CheckLimit(limit, 100); err != nil {
		return nil, err
	}
	path := PathDepth + "/" + symbol
	params := map[string]interface{}{"limit": limit}
	resp, err := c.http.PublicRequest(ctx, "GET", path, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssets returns account assets (private; requires API key and secret).
// @endpoint GET /api/v1/private/account/assets
func (c *FuturesRestClient) GetAssets(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathAccountAssets, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetPositions returns open positions (private). params can include symbol, page_num, page_size, etc.
// @endpoint GET /api/v1/private/position/open_positions
func (c *FuturesRestClient) GetPositions(ctx context.Context, params map[string]any) (*RawResponse, error) {
	var p map[string]interface{}
	if params != nil {
		p = make(map[string]interface{}, len(params))
		for k, v := range params {
			p[k] = v
		}
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathPositionOpenPositions, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetContractDetail returns contract detail for a single symbol or all contracts when symbol is empty.
// @endpoint GET /api/v1/contract/detail
func (c *FuturesRestClient) GetContractDetail(ctx context.Context, symbol string) (*RawResponse, error) {
	params := map[string]interface{}{}
	if symbol != "" {
		params["symbol"] = symbol
	}
	resp, err := c.http.PublicRequest(ctx, "GET", PathContractDetail, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetSupportCurrencies returns transferable currencies list.
// @endpoint GET /api/v1/contract/support_currencies
func (c *FuturesRestClient) GetSupportCurrencies(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PublicRequest(ctx, "GET", PathSupportCurrencies, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetDepthCommits returns recent depth snapshots for the given symbol and limit.
// @endpoint GET /api/v1/contract/depth_commits/{symbol}/{limit}
func (c *FuturesRestClient) GetDepthCommits(ctx context.Context, symbol string, limit int) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	if limit < 1 {
		return nil, fmt.Errorf("limit required and must be >= 1")
	}
	path := PathDepthCommits + "/" + symbol + "/" + strconv.Itoa(limit)
	resp, err := c.http.PublicRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetIndexPrice returns index price for the given symbol.
// @endpoint GET /api/v1/contract/index_price/{symbol}
func (c *FuturesRestClient) GetIndexPrice(ctx context.Context, symbol string) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	path := PathIndexPrice + "/" + symbol
	resp, err := c.http.PublicRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetFairPrice returns fair price (mark price) for the given symbol.
// @endpoint GET /api/v1/contract/fair_price/{symbol}
func (c *FuturesRestClient) GetFairPrice(ctx context.Context, symbol string) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	path := PathFairPrice + "/" + symbol
	resp, err := c.http.PublicRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetFundingRate returns current funding rate for the given symbol.
// @endpoint GET /api/v1/contract/funding_rate/{symbol}
func (c *FuturesRestClient) GetFundingRate(ctx context.Context, symbol string) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	path := PathFundingRate + "/" + symbol
	resp, err := c.http.PublicRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// FundingRateHistoryParams defines parameters for funding rate history.
// TODO: Confirm allowed page_num/page_size ranges from official docs.
type FundingRateHistoryParams struct {
	Symbol   string
	PageNum  int
	PageSize int
}

// GetFundingRateHistory returns historical funding rates with pagination.
// @endpoint GET /api/v1/contract/funding_rate/history
func (c *FuturesRestClient) GetFundingRateHistory(ctx context.Context, p FundingRateHistoryParams) (*RawResponse, error) {
	if p.Symbol == "" {
		return nil, ErrSymbolRequired
	}
	if err := CheckPage(p.PageNum, p.PageSize, 100); err != nil {
		return nil, err
	}
	params := map[string]interface{}{
		"symbol": p.Symbol,
	}
	if p.PageNum > 0 {
		params["page_num"] = p.PageNum
	}
	if p.PageSize > 0 {
		params["page_size"] = p.PageSize
	}
	resp, err := c.http.PublicRequest(ctx, "GET", PathFundingRateHistory, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// KlineParams defines shared parameters for kline endpoints.
// Time fields follow the Futures docs (commonly seconds since epoch).
// TODO: Confirm exact time unit (seconds vs milliseconds) from official docs.
type KlineParams struct {
	Interval string
	Start    int64
	End      int64
}

// GetKline returns candlestick data for a symbol.
// @endpoint GET /api/v1/contract/kline/{symbol}
func (c *FuturesRestClient) GetKline(ctx context.Context, symbol string, p KlineParams) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	path := PathKline + "/" + symbol
	params := map[string]interface{}{}
	if p.Interval != "" {
		params["interval"] = p.Interval
	}
	if p.Start > 0 {
		params["start"] = p.Start
	}
	if p.End > 0 {
		params["end"] = p.End
	}
	resp, err := c.http.PublicRequest(ctx, "GET", path, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetKlineIndexPrice returns index price kline for a symbol.
// @endpoint GET /api/v1/contract/kline/index_price/{symbol}
func (c *FuturesRestClient) GetKlineIndexPrice(ctx context.Context, symbol string, p KlineParams) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	path := PathKlineIndexPrice + "/" + symbol
	params := map[string]interface{}{}
	if p.Interval != "" {
		params["interval"] = p.Interval
	}
	if p.Start > 0 {
		params["start"] = p.Start
	}
	if p.End > 0 {
		params["end"] = p.End
	}
	resp, err := c.http.PublicRequest(ctx, "GET", path, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetKlineFairPrice returns fair price kline for a symbol.
// @endpoint GET /api/v1/contract/kline/fair_price/{symbol}
func (c *FuturesRestClient) GetKlineFairPrice(ctx context.Context, symbol string, p KlineParams) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	path := PathKlineFairPrice + "/" + symbol
	params := map[string]interface{}{}
	if p.Interval != "" {
		params["interval"] = p.Interval
	}
	if p.Start > 0 {
		params["start"] = p.Start
	}
	if p.End > 0 {
		params["end"] = p.End
	}
	resp, err := c.http.PublicRequest(ctx, "GET", path, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetDeals returns recent trades for a symbol.
// @endpoint GET /api/v1/contract/deals/{symbol}
func (c *FuturesRestClient) GetDeals(ctx context.Context, symbol string, limit int) (*RawResponse, error) {
	if symbol == "" {
		return nil, ErrSymbolRequired
	}
	if limit <= 0 {
		limit = 100
	}
	if err := CheckLimit(limit, 100); err != nil {
		return nil, err
	}
	path := PathDeals + "/" + symbol
	params := map[string]interface{}{}
	if limit > 0 {
		params["limit"] = limit
	}
	resp, err := c.http.PublicRequest(ctx, "GET", path, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetRiskReverse returns risk fund balance, for a single symbol when provided or all contracts when symbol is empty.
// @endpoint GET /api/v1/contract/risk_reverse or /api/v1/contract/risk_reverse/{symbol}
func (c *FuturesRestClient) GetRiskReverse(ctx context.Context, symbol string) (*RawResponse, error) {
	if symbol == "" {
		resp, err := c.http.PublicRequest(ctx, "GET", PathRiskReverse, nil)
		if err != nil {
			return nil, err
		}
		return resp, nil
	}
	path := PathRiskReverse + "/" + symbol
	resp, err := c.http.PublicRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// RiskReverseHistoryParams defines parameters for risk reverse history.
// TODO: Confirm allowed page_num/page_size ranges from official docs.
type RiskReverseHistoryParams struct {
	Symbol   string
	PageNum  int
	PageSize int
}

// GetRiskReverseHistory returns risk fund balance history.
// @endpoint GET /api/v1/contract/risk_reverse/history
func (c *FuturesRestClient) GetRiskReverseHistory(ctx context.Context, p RiskReverseHistoryParams) (*RawResponse, error) {
	if p.Symbol == "" {
		return nil, ErrSymbolRequired
	}
	if err := CheckPage(p.PageNum, p.PageSize, 100); err != nil {
		return nil, err
	}
	params := map[string]interface{}{
		"symbol": p.Symbol,
	}
	if p.PageNum > 0 {
		params["page_num"] = p.PageNum
	}
	if p.PageSize > 0 {
		params["page_size"] = p.PageSize
	}
	resp, err := c.http.PublicRequest(ctx, "GET", PathRiskReverseHistory, params)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private account -----

// GetAsset returns single currency asset. Params must include "currency".
// @endpoint GET /api/v1/private/account/asset/{currency}
func (c *FuturesRestClient) GetAsset(ctx context.Context, params map[string]any) (*RawResponse, error) {
	currency := getStringParam(params, "currency")
	if currency == "" {
		return nil, fmt.Errorf("params.currency required")
	}
	path := PathAccountAsset + "/" + currency
	resp, err := c.http.PrivateRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetTransferRecords returns transfer records. Params may include currency, state, type, page_num, page_size.
// If page_num is omitted or <1, it defaults to 1. If page_size is omitted or <1, it defaults to 20 (per API docs).
// @endpoint GET /api/v1/private/account/transfer_record
func (c *FuturesRestClient) GetTransferRecords(ctx context.Context, params map[string]any) (*RawResponse, error) {
	p := toInterfaceMap(params)
	if p == nil {
		p = make(map[string]interface{})
	}
	pn := getIntParam(params, "page_num")
	if pn < 1 {
		pn = 1
	}
	ps := getIntParam(params, "page_size")
	if ps < 1 {
		ps = 20
	}
	if err := CheckPage(pn, ps, 100); err != nil {
		return nil, err
	}
	p["page_num"] = pn
	p["page_size"] = ps
	resp, err := c.http.PrivateRequest(ctx, "GET", PathTransferRecord, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetProfitRate returns profit rate. Params must include "type" (1: day, 2: week).
// @endpoint GET /api/v1/private/account/profit_rate/{type}
func (c *FuturesRestClient) GetProfitRate(ctx context.Context, params map[string]any) (*RawResponse, error) {
	typ := getIntParam(params, "type")
	if typ == 0 {
		return nil, fmt.Errorf("params.type required (1: day, 2: week)")
	}
	path := PathProfitRate + "/" + strconv.Itoa(typ)
	resp, err := c.http.PrivateRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetYesterdayPnl returns yesterday PnL.
// @endpoint GET /api/v1/private/account/asset/analysis/yesterday_pnl
func (c *FuturesRestClient) GetYesterdayPnl(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathYesterdayPnl, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetTodayPnl returns today PnL. Params may include reverse, includeUnrealisedPnl.
// @endpoint GET /api/v1/private/account/asset/analysis/today_pnl
func (c *FuturesRestClient) GetTodayPnl(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathTodayPnl, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetFeeDeductConfigs returns fee deduct configs.
// @endpoint GET /api/v1/private/account/feeDeductConfigs
func (c *FuturesRestClient) GetFeeDeductConfigs(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathFeeDeductConfigs, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetRiskLimit returns risk limit. Params may include symbol.
// @endpoint GET /api/v1/private/account/risk_limit
func (c *FuturesRestClient) GetRiskLimit(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathRiskLimit, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetFeeRate returns contract fee rate. Params may include symbol.
// @endpoint GET /api/v1/private/account/contract/fee_rate
func (c *FuturesRestClient) GetFeeRate(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathContractFeeRate, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetTieredFeeRate returns tiered fee rate. Params may include symbol.
// @endpoint GET /api/v1/private/account/tiered_fee_rate/v2
func (c *FuturesRestClient) GetTieredFeeRate(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathTieredFeeRate, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetDiscountType returns user discount type.
// @endpoint GET /api/v1/private/account/discountType
func (c *FuturesRestClient) GetDiscountType(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathDiscountType, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssetAnalysis returns asset analysis for a currency and period type.
// @endpoint GET /api/v1/private/account/asset/analysis/{type}
func (c *FuturesRestClient) GetAssetAnalysis(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "currency") == "" {
		return nil, fmt.Errorf("params.currency required")
	}
	typ := getIntParam(params, "type")
	if typ == 0 {
		return nil, fmt.Errorf("params.type required")
	}
	path := PathAssetAnalysis + "/" + strconv.Itoa(typ)
	body := map[string]interface{}{
		"currency":  getStringParam(params, "currency"),
		"startTime": params["startTime"],
		"endTime":   params["endTime"],
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", path, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssetAnalysisV3 returns asset analysis v3.
// @endpoint POST /api/v1/private/account/asset/analysis/v3
func (c *FuturesRestClient) GetAssetAnalysisV3(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || params["startTime"] == nil || params["endTime"] == nil {
		return nil, fmt.Errorf("params.startTime and params.endTime required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathAssetAnalysisV3, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssetAnalysisCalendarDaily returns daily calendar asset analysis v3.
// @endpoint POST /api/v1/private/account/asset/analysis/calendar/daily/v3
func (c *FuturesRestClient) GetAssetAnalysisCalendarDaily(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || params["startTime"] == nil || params["endTime"] == nil {
		return nil, fmt.Errorf("params.startTime and params.endTime required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathAssetAnalysisCalendarDailyV3, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssetAnalysisCalendarMonthly returns monthly calendar asset analysis v3.
// @endpoint POST /api/v1/private/account/asset/analysis/calendar/monthly/v3
func (c *FuturesRestClient) GetAssetAnalysisCalendarMonthly(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathAssetAnalysisCalendarMonthlyV3, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssetAnalysisRecent returns recent asset analysis v3.
// @endpoint POST /api/v1/private/account/asset/analysis/recent/v3
func (c *FuturesRestClient) GetAssetAnalysisRecent(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathAssetAnalysisRecentV3, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetAssetAnalysisExport exports asset analysis report. Extra headers may include timezone-login.
// @endpoint GET /api/v1/private/account/asset/analysis/export
func (c *FuturesRestClient) GetAssetAnalysisExport(ctx context.Context, params map[string]any, extraHeaders map[string]string) (*RawResponse, error) {
	if params == nil || params["startTime"] == nil || params["endTime"] == nil {
		return nil, fmt.Errorf("params.startTime and params.endTime required")
	}
	// TODO: extraHeaders (e.g. timezone-login) are not yet wired into HTTPClient; extend HTTP layer if header-level control is required.
	resp, err := c.http.PrivateRequest(ctx, "GET", PathAssetAnalysisExport, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetContractFeeDiscountConfig returns contract fee discount configuration.
// @endpoint GET /api/v1/private/account/config/contractFeeDiscountConfig
func (c *FuturesRestClient) GetContractFeeDiscountConfig(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathContractFeeDiscountConfig, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOrderDealFeeTotal returns total order deal fees (last 30 days).
// @endpoint GET /api/v1/private/account/asset_book/order_deal_fee/total
func (c *FuturesRestClient) GetOrderDealFeeTotal(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderDealFeeTotal, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetZeroFeeRate returns zero-fee rate pairs.
// @endpoint GET /api/v1/private/account/contract/zero_fee_rate
func (c *FuturesRestClient) GetZeroFeeRate(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathZeroFeeRate, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeRiskLevel changes risk level configuration (may be disabled by server).
// @endpoint POST /api/v1/private/account/change_risk_level
func (c *FuturesRestClient) ChangeRiskLevel(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathChangeRiskLevel, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private position -----

// GetHistoryPositions returns history positions. Params: symbol, type, start_time, end_time, page_num, page_size.
// @endpoint GET /api/v1/private/position/list/history_positions
func (c *FuturesRestClient) GetHistoryPositions(ctx context.Context, params map[string]any) (*RawResponse, error) {
	p := toInterfaceMap(params)
	if p != nil {
		if pn, ps := getIntParam(params, "page_num"), getIntParam(params, "page_size"); pn != 0 || ps != 0 {
			if err := CheckPage(pn, ps, 100); err != nil {
				return nil, err
			}
		}
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathPositionHistoryPositions, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetLeverage returns position leverage. Params must include symbol.
// @endpoint GET /api/v1/private/position/leverage
func (c *FuturesRestClient) GetLeverage(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathPositionLeverage, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetFundingRecords returns funding records. Params should include symbol, page_num, page_size, position_type, start_time, end_time (API requirements).
// @endpoint GET /api/v1/private/position/funding_records
func (c *FuturesRestClient) GetFundingRecords(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathPositionFundingRecords, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private order -----

// ChangeMargin changes margin for a position.
// @endpoint POST /api/v1/private/position/change_margin
func (c *FuturesRestClient) ChangeMargin(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || params["positionId"] == nil || params["amount"] == nil || getStringParam(params, "type") == "" {
		return nil, fmt.Errorf("params.positionId, params.amount, params.type required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPositionChangeMargin, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeAutoAddIm enables or disables auto add margin for a position.
// @endpoint POST /api/v1/private/position/change_auto_add_im
func (c *FuturesRestClient) ChangeAutoAddIm(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || params["positionId"] == nil || params["isEnabled"] == nil {
		return nil, fmt.Errorf("params.positionId and params.isEnabled required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPositionChangeAutoAddIm, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeLeverage changes leverage for positions.
// @endpoint POST /api/v1/private/position/change_leverage
func (c *FuturesRestClient) ChangeLeverage(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || params["leverage"] == nil {
		return nil, fmt.Errorf("params.leverage required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPositionChangeLeverage, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetPositionMode returns current position mode.
// @endpoint GET /api/v1/private/position/position_mode
func (c *FuturesRestClient) GetPositionMode(ctx context.Context) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathPositionMode, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangePositionMode changes position mode (e.g. hedge or one-way).
// @endpoint POST /api/v1/private/position/change_position_mode
func (c *FuturesRestClient) ChangePositionMode(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || params["positionMode"] == nil {
		return nil, fmt.Errorf("params.positionMode required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPositionChangePositionMode, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ReversePosition reverses a position.
// @endpoint POST /api/v1/private/position/reverse
func (c *FuturesRestClient) ReversePosition(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil || getStringParam(params, "symbol") == "" || params["positionId"] == nil || params["vol"] == nil {
		return nil, fmt.Errorf("params.symbol, params.positionId, params.vol required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPositionReverse, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CloseAllPositions closes all positions.
// @endpoint POST /api/v1/private/position/close_all
func (c *FuturesRestClient) CloseAllPositions(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPositionCloseAll, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private order -----

// SubmitOrder submits a single order. Params: symbol, vol, side, type, openType, price? (for limit), leverage? (for open).
// @endpoint POST /api/v1/private/order/create
func (c *FuturesRestClient) SubmitOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	if _, ok := params["vol"]; !ok {
		return nil, fmt.Errorf("params.vol required")
	}
	if _, ok := params["side"]; !ok {
		return nil, fmt.Errorf("params.side required")
	}
	if _, ok := params["type"]; !ok {
		return nil, fmt.Errorf("params.type required")
	}
	if _, ok := params["openType"]; !ok {
		return nil, fmt.Errorf("params.openType required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderCreate, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// SubmitOrderBatch submits multiple orders. Params must be a slice of order objects (raw array body) or map with "batchOrder" key (slice).
// Body sent to API is exactly the JSON array of order objects (same as Node/Postman).
// @endpoint POST /api/v1/private/order/submit_batch
func (c *FuturesRestClient) SubmitOrderBatch(ctx context.Context, params interface{}) (*RawResponse, error) {
	var body interface{}
	switch v := params.(type) {
	case []interface{}:
		if len(v) == 0 {
			return nil, fmt.Errorf("batchOrder must not be empty")
		}
		body = v
	case map[string]any:
		bo, ok := v["batchOrder"]
		if !ok {
			return nil, fmt.Errorf("params.batchOrder required (array) or pass array directly")
		}
		sl, ok := toSlice(bo)
		if !ok || len(sl) == 0 {
			return nil, fmt.Errorf("batchOrder must not be empty")
		}
		body = sl
	default:
		return nil, fmt.Errorf("params must be []interface{} or map with batchOrder")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderSubmitBatch, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelOrder cancels one or more orders. Params: orderIds ([]int64) or orderId (single int64). Body sent is JSON array of order IDs.
// @endpoint POST /api/v1/private/order/cancel
func (c *FuturesRestClient) CancelOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	ids := orderIDsFromParams(params)
	if len(ids) == 0 {
		return nil, fmt.Errorf("orderIds or orderId required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderCancel, ids)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelAllOrders cancels all orders. Params may include symbol (omit to cancel all contracts).
// @endpoint POST /api/v1/private/order/cancel_all
func (c *FuturesRestClient) CancelAllOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderCancelAll, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOrderDetail returns order detail by order id. Params must include orderId.
// @endpoint GET /api/v1/private/order/get/{orderId}
func (c *FuturesRestClient) GetOrderDetail(ctx context.Context, params map[string]any) (*RawResponse, error) {
	orderId := getOrderIDParam(params, "orderId")
	if orderId == 0 {
		orderId = getOrderIDParam(params, "order_id")
	}
	if orderId == 0 {
		return nil, fmt.Errorf("params.orderId or order_id required")
	}
	path := PathOrderGet + "/" + strconv.FormatInt(orderId, 10)
	resp, err := c.http.PrivateRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOpenOrders returns open orders. Params may include page_num (default 1), page_size (default 20).
// @endpoint GET /api/v1/private/order/list/open_orders
func (c *FuturesRestClient) GetOpenOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	p := toInterfaceMap(params)
	if p == nil {
		p = make(map[string]interface{})
	}
	pn, ps := getIntParam(params, "page_num"), getIntParam(params, "page_size")
	if pn == 0 {
		pn = 1
		p["page_num"] = 1
	}
	if ps == 0 {
		ps = 20
		p["page_size"] = 20
	}
	if err := CheckPage(pn, ps, 100); err != nil {
		return nil, err
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderListOpenOrders, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetHistoryOrders returns history orders. Params: symbol?, states?, category?, startTime?, endTime?, page_num, page_size, orderId?.
// @endpoint GET /api/v1/private/order/list/history_orders
func (c *FuturesRestClient) GetHistoryOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	p := toInterfaceMap(params)
	if p != nil {
		if pn, ps := getIntParam(params, "page_num"), getIntParam(params, "page_size"); pn != 0 || ps != 0 {
			if err := CheckPage(pn, ps, 100); err != nil {
				return nil, err
			}
		}
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderListHistoryOrders, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOrderDealList returns order deal list (v3). Params must include symbol; page_num, page_size optional.
// @endpoint GET /api/v1/private/order/list/order_deals/v3
func (c *FuturesRestClient) GetOrderDealList(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	p := toInterfaceMap(params)
	if p != nil {
		if pn, ps := getIntParam(params, "page_num"), getIntParam(params, "page_size"); pn != 0 || ps != 0 {
			if err := CheckPage(pn, ps, 100); err != nil {
				return nil, err
			}
		}
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderListOrderDealsV3, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// BatchCancelWithExternal cancels multiple orders by external order identifiers (raw array body).
// @endpoint POST /api/v1/private/order/batch_cancel_with_external
func (c *FuturesRestClient) BatchCancelWithExternal(ctx context.Context, params interface{}) (*RawResponse, error) {
	list, ok := toSlice(params)
	if !ok || len(list) == 0 {
		return nil, fmt.Errorf("params must be non-empty array of { symbol, externalOid }")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderBatchCancelWithExternal, list)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelWithExternal cancels order(s) by external order id. Body is a single-element array for compatibility with API.
// @endpoint POST /api/v1/private/order/cancel_with_external
func (c *FuturesRestClient) CancelWithExternal(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	if getStringParam(params, "externalOid") == "" {
		return nil, fmt.Errorf("params.externalOid required")
	}
	body := []interface{}{toInterfaceMap(params)}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderCancelWithExternal, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChaseLimitOrder moves a limit order price to the next price level.
// @endpoint POST /api/v1/private/order/chase_limit_order
func (c *FuturesRestClient) ChaseLimitOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getOrderIDParam(params, "orderId") == 0 {
		return nil, fmt.Errorf("params.orderId required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderChaseLimitOrder, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeLimitOrder changes price and/or volume of an existing limit order.
// @endpoint POST /api/v1/private/order/change_limit_order
func (c *FuturesRestClient) ChangeLimitOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getOrderIDParam(params, "orderId") == 0 {
		return nil, fmt.Errorf("params.orderId required")
	}
	if _, ok := params["price"]; !ok {
		return nil, fmt.Errorf("params.price required")
	}
	if _, ok := params["vol"]; !ok {
		return nil, fmt.Errorf("params.vol required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderChangeLimitOrder, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOpenOrderTotalCount returns total count of open orders.
// @endpoint POST /api/v1/private/order/open_order_total_count
func (c *FuturesRestClient) GetOpenOrderTotalCount(ctx context.Context, params map[string]any) (*RawResponse, error) {
	// TODO: Confirm exact request params from official Futures docs (Node SDK mentions doc inconsistency).
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderOpenOrderTotalCount, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOrderByExternal returns order detail by external order id.
// @endpoint GET /api/v1/private/order/external/{symbol}/{external_oid}
func (c *FuturesRestClient) GetOrderByExternal(ctx context.Context, params map[string]any) (*RawResponse, error) {
	symbol := getStringParam(params, "symbol")
	if symbol == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	externalOid := getStringParam(params, "external_oid")
	if externalOid == "" {
		return nil, fmt.Errorf("params.external_oid required")
	}
	path := PathOrderExternal + "/" + symbol + "/" + externalOid
	resp, err := c.http.PrivateRequest(ctx, "GET", path, nil)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// BatchQueryOrders queries orders by IDs (comma-separated list or array).
// @endpoint GET /api/v1/private/order/batch_query
func (c *FuturesRestClient) BatchQueryOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	_, hasOrderIds := params["order_ids"]
	_, hasOrderIdsCamel := params["orderIds"]
	if !hasOrderIds && !hasOrderIdsCamel {
		return nil, fmt.Errorf("params.order_ids or orderIds required")
	}
	p := toInterfaceMap(params)
	// Normalize array of IDs to comma-separated string on order_ids.
	if v, ok := p["order_ids"]; ok {
		if sl, ok2 := toSlice(v); ok2 {
			parts := make([]string, 0, len(sl))
			for _, x := range sl {
				id := toInt64(x)
				if id != 0 {
					parts = append(parts, strconv.FormatInt(id, 10))
				}
			}
			p["order_ids"] = strings.Join(parts, ",")
		}
	} else if v, ok := p["orderIds"]; ok {
		if sl, ok2 := toSlice(v); ok2 {
			parts := make([]string, 0, len(sl))
			for _, x := range sl {
				id := toInt64(x)
				if id != 0 {
					parts = append(parts, strconv.FormatInt(id, 10))
				}
			}
			p["order_ids"] = strings.Join(parts, ",")
			delete(p, "orderIds")
		}
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderBatchQuery, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// BatchQueryWithExternal queries orders by external order identifiers using raw array body.
// @endpoint POST /api/v1/private/order/batch_query_with_external
func (c *FuturesRestClient) BatchQueryWithExternal(ctx context.Context, params interface{}) (*RawResponse, error) {
	list, ok := toSlice(params)
	if !ok || len(list) == 0 {
		return nil, fmt.Errorf("params must be non-empty array of { symbol, externalOid }")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathOrderBatchQueryWithExternal, list)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetCloseOrders returns closed orders list.
// @endpoint GET /api/v1/private/order/list/close_orders
func (c *FuturesRestClient) GetCloseOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	pn := getIntParam(params, "page_num")
	ps := getIntParam(params, "page_size")
	if pn == 0 {
		pn = 1
	}
	if ps == 0 {
		ps = 20
	}
	if err := CheckPage(pn, ps, 1000); err != nil {
		return nil, err
	}
	p := toInterfaceMap(params)
	if p == nil {
		p = make(map[string]interface{})
	}
	p["page_num"] = pn
	p["page_size"] = ps
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderListCloseOrders, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOrderDealDetails returns deal details for a single order.
// @endpoint GET /api/v1/private/order/deal_details/{orderId}
func (c *FuturesRestClient) GetOrderDealDetails(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	orderId := getOrderIDParam(params, "order_id")
	if orderId == 0 {
		orderId = getOrderIDParam(params, "orderId")
	}
	if orderId == 0 {
		return nil, fmt.Errorf("params.order_id or orderId required")
	}
	path := PathOrderDealDetails + "/" + strconv.FormatInt(orderId, 10)
	body := map[string]interface{}{"symbol": getStringParam(params, "symbol")}
	resp, err := c.http.PrivateRequest(ctx, "GET", path, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetOrderFeeDetails returns fee details for orders.
// @endpoint GET /api/v1/private/order/fee_details
func (c *FuturesRestClient) GetOrderFeeDetails(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if getStringParam(params, "symbol") == "" {
		return nil, fmt.Errorf("params.symbol required")
	}
	resp, err := c.http.PrivateRequest(ctx, "GET", PathOrderFeeDetails, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private plan order -----

// GetPlanOrders returns plan orders list with pagination.
// @endpoint GET /api/v1/private/planorder/list/orders
func (c *FuturesRestClient) GetPlanOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	if _, ok := params["start_time"]; !ok {
		return nil, fmt.Errorf("params.start_time required")
	}
	if _, ok := params["end_time"]; !ok {
		return nil, fmt.Errorf("params.end_time required")
	}
	pn := getIntParam(params, "page_num")
	ps := getIntParam(params, "page_size")
	if pn == 0 {
		pn = 1
	}
	if ps == 0 {
		ps = 20
	}
	if err := CheckPage(pn, ps, 100); err != nil {
		return nil, err
	}
	p := toInterfaceMap(params)
	if p == nil {
		p = make(map[string]interface{})
	}
	p["page_num"] = pn
	p["page_size"] = ps
	resp, err := c.http.PrivateRequest(ctx, "GET", PathPlanListOrders, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// PlacePlanOrder places a new plan order.
// @endpoint POST /api/v1/private/planorder/place/v2
func (c *FuturesRestClient) PlacePlanOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPlanPlaceV2, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangePlanOrderPrice changes the price of an existing plan order.
// @endpoint POST /api/v1/private/planorder/change_price
func (c *FuturesRestClient) ChangePlanOrderPrice(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPlanChangePrice, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelPlanOrder cancels plan orders. Accepts either an array body or map with "orders" array.
// @endpoint POST /api/v1/private/planorder/cancel
func (c *FuturesRestClient) CancelPlanOrder(ctx context.Context, params interface{}) (*RawResponse, error) {
	list, ok := toSlice(params)
	if !ok || len(list) == 0 {
		// Try wrapper with orders key.
		if m, ok2 := params.(map[string]any); ok2 {
			if orders, exists := m["orders"]; exists {
				if sl, ok3 := toSlice(orders); ok3 {
					list = sl
				}
			}
		} else if m, ok2 := params.(map[string]interface{}); ok2 {
			if orders, exists := m["orders"]; exists {
				if sl, ok3 := toSlice(orders); ok3 {
					list = sl
				}
			}
		}
	}
	if len(list) == 0 {
		return nil, fmt.Errorf("params.orders or array of { symbol, orderId } required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPlanCancel, list)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelAllPlanOrders cancels all plan orders for the account (optionally filtered by symbol).
// @endpoint POST /api/v1/private/planorder/cancel_all
func (c *FuturesRestClient) CancelAllPlanOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPlanCancelAll, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangePlanStopOrder changes the stop (TP/SL) configuration for a plan order.
// @endpoint POST /api/v1/private/planorder/change_stop_order
func (c *FuturesRestClient) ChangePlanStopOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathPlanChangeStopOrder, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private stop order -----

// PlaceStopOrder places a stop order (TP/SL).
// @endpoint POST /api/v1/private/stoporder/place
func (c *FuturesRestClient) PlaceStopOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStopPlace, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelStopOrder cancels stop orders. Accepts either an array body or map with "orders" array.
// @endpoint POST /api/v1/private/stoporder/cancel
func (c *FuturesRestClient) CancelStopOrder(ctx context.Context, params interface{}) (*RawResponse, error) {
	list, ok := toSlice(params)
	if !ok || len(list) == 0 {
		if m, ok2 := params.(map[string]any); ok2 {
			if orders, exists := m["orders"]; exists {
				if sl, ok3 := toSlice(orders); ok3 {
					list = sl
				}
			}
		} else if m, ok2 := params.(map[string]interface{}); ok2 {
			if orders, exists := m["orders"]; exists {
				if sl, ok3 := toSlice(orders); ok3 {
					list = sl
				}
			}
		}
	}
	if len(list) == 0 {
		return nil, fmt.Errorf("params.orders or array of { stopPlanOrderId } required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStopCancel, list)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelAllStopOrders cancels all stop orders.
// @endpoint POST /api/v1/private/stoporder/cancel_all
func (c *FuturesRestClient) CancelAllStopOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStopCancelAll, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeStopOrderPrice changes stop order price.
// @endpoint POST /api/v1/private/stoporder/change_price
func (c *FuturesRestClient) ChangeStopOrderPrice(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStopChangePrice, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeStopPlanPrice changes stop plan price.
// @endpoint POST /api/v1/private/stoporder/change_plan_price
func (c *FuturesRestClient) ChangeStopPlanPrice(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStopChangePlanPrice, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetStopOrders returns stop orders list with pagination.
// @endpoint GET /api/v1/private/stoporder/list/orders
func (c *FuturesRestClient) GetStopOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	pn := getIntParam(params, "page_num")
	ps := getIntParam(params, "page_size")
	if pn == 0 {
		pn = 1
	}
	if ps == 0 {
		ps = 20
	}
	if err := CheckPage(pn, ps, 100); err != nil {
		return nil, err
	}
	p := toInterfaceMap(params)
	if p == nil {
		p = make(map[string]interface{})
	}
	p["page_num"] = pn
	p["page_size"] = ps
	resp, err := c.http.PrivateRequest(ctx, "GET", PathStopListOrders, p)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetStopOpenOrders returns current stop orders.
// @endpoint GET /api/v1/private/stoporder/open_orders
func (c *FuturesRestClient) GetStopOpenOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathStopOpenOrders, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private track order -----

// PlaceTrackOrder places a track order.
// @endpoint POST /api/v1/private/trackorder/place
func (c *FuturesRestClient) PlaceTrackOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathTrackPlace, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CancelTrackOrder cancels a track order.
// @endpoint POST /api/v1/private/trackorder/cancel
func (c *FuturesRestClient) CancelTrackOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathTrackCancel, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ChangeTrackOrder changes a track order.
// @endpoint POST /api/v1/private/trackorder/change_order
func (c *FuturesRestClient) ChangeTrackOrder(ctx context.Context, params map[string]any) (*RawResponse, error) {
	if params == nil {
		return nil, fmt.Errorf("params required")
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathTrackChangeOrder, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// GetTrackOrders returns track order list.
// @endpoint GET /api/v1/private/trackorder/list/orders
func (c *FuturesRestClient) GetTrackOrders(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathTrackListOrders, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- private STP (self-trade prevention, market maker only) -----

// GetStpList queries STP groups and group members.
// @endpoint GET /api/v1/private/market_maker/self_trade/blacklist
func (c *FuturesRestClient) GetStpList(ctx context.Context, params map[string]any) (*RawResponse, error) {
	resp, err := c.http.PrivateRequest(ctx, "GET", PathStpBlacklist, toInterfaceMap(params))
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// CreateStp creates an STP group configuration.
// @endpoint POST /api/v1/private/market_maker/self_trade/blacklist/create
func (c *FuturesRestClient) CreateStp(ctx context.Context, params map[string]any) (*RawResponse, error) {
	name := getStringParam(params, "configName")
	if name == "" {
		return nil, fmt.Errorf("params.configName required")
	}
	bl, ok := params["blacklist"]
	if !ok {
		return nil, fmt.Errorf("params.blacklist required and must be array")
	}
	list, ok := toSlice(bl)
	if !ok || len(list) == 0 {
		return nil, fmt.Errorf("params.blacklist required and must be array")
	}
	body := map[string]interface{}{
		"configName": name,
		"blacklist":  list,
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStpBlacklistCreate, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// UpdateStp updates an existing STP group configuration.
// @endpoint POST /api/v1/private/market_maker/self_trade/blacklist/update
func (c *FuturesRestClient) UpdateStp(ctx context.Context, params map[string]any) (*RawResponse, error) {
	name := getStringParam(params, "configName")
	if name == "" {
		return nil, fmt.Errorf("params.configName required")
	}
	bl, ok := params["blacklist"]
	if !ok {
		return nil, fmt.Errorf("params.blacklist required and must be array")
	}
	list, ok := toSlice(bl)
	if !ok || len(list) == 0 {
		return nil, fmt.Errorf("params.blacklist required and must be array")
	}
	body := map[string]interface{}{
		"configName": name,
		"blacklist":  list,
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStpBlacklistUpdate, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// DeleteStp deletes an STP group.
// @endpoint POST /api/v1/private/market_maker/self_trade/blacklist/delete
func (c *FuturesRestClient) DeleteStp(ctx context.Context, params map[string]any) (*RawResponse, error) {
	name := getStringParam(params, "configName")
	if name == "" {
		return nil, fmt.Errorf("params.configName required")
	}
	body := map[string]interface{}{
		"configName": name,
	}
	resp, err := c.http.PrivateRequest(ctx, "POST", PathStpBlacklistDelete, body)
	if err != nil {
		return nil, err
	}
	return resp, nil
}

// ----- helpers (private) -----

func toInterfaceMap(m map[string]any) map[string]interface{} {
	if m == nil {
		return nil
	}
	out := make(map[string]interface{}, len(m))
	for k, v := range m {
		out[k] = v
	}
	return out
}

func getStringParam(m map[string]any, key string) string {
	if m == nil {
		return ""
	}
	v, ok := m[key]
	if !ok {
		return ""
	}
	s, _ := v.(string)
	return s
}

func getIntParam(m map[string]any, key string) int {
	if m == nil {
		return 0
	}
	v, ok := m[key]
	if !ok {
		return 0
	}
	switch x := v.(type) {
	case int:
		return x
	case int64:
		return int(x)
	case float64:
		return int(x)
	default:
		return 0
	}
}

func getOrderIDParam(m map[string]any, key string) int64 {
	if m == nil {
		return 0
	}
	v, ok := m[key]
	if !ok {
		return 0
	}
	switch x := v.(type) {
	case int64:
		return x
	case int:
		return int64(x)
	case float64:
		return int64(x)
	default:
		return 0
	}
}

// orderIDsFromParams builds a slice of order IDs for cancel body. Supports orderIds ([]int64 or []interface{}) or orderId (single).
func orderIDsFromParams(params map[string]any) []int64 {
	if params == nil {
		return nil
	}
	if ids, ok := params["orderIds"]; ok {
		sl, ok := toSlice(ids)
		if !ok {
			return nil
		}
		out := make([]int64, 0, len(sl))
		for _, v := range sl {
			if id := toInt64(v); id != 0 {
				out = append(out, id)
			}
		}
		return out
	}
	if id := getOrderIDParam(params, "orderId"); id != 0 {
		return []int64{id}
	}
	return nil
}

func toSlice(v interface{}) ([]interface{}, bool) {
	if v == nil {
		return nil, false
	}
	// []interface{}
	if s, ok := v.([]interface{}); ok {
		return s, true
	}
	// []int64 or similar - convert to []interface{} for JSON
	if s, ok := v.([]int64); ok {
		out := make([]interface{}, len(s))
		for i := range s {
			out[i] = s[i]
		}
		return out, true
	}
	// []map[string]any
	if s, ok := v.([]map[string]any); ok {
		out := make([]interface{}, len(s))
		for i := range s {
			out[i] = s[i]
		}
		return out, true
	}
	// []map[string]interface{}
	if s, ok := v.([]map[string]interface{}); ok {
		out := make([]interface{}, len(s))
		for i := range s {
			out[i] = s[i]
		}
		return out, true
	}
	return nil, false
}

func toInt64(v interface{}) int64 {
	if v == nil {
		return 0
	}
	switch x := v.(type) {
	case int64:
		return x
	case int:
		return int64(x)
	case float64:
		return int64(x)
	default:
		return 0
	}
}
