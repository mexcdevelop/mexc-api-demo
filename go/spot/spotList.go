package spotList

import (
	"demo/config"
	"demo/utils"
	"fmt"
)

// # 具体请求配置

// ## 行情接口 Market Data Endpoints

// ### 1 测试服务器连通性 Test Connectivity
func Ping(jsonParams string) interface{} {
	caseUrl := "/ping"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 2 获取服务器时间 Check Server Time
func Time(jsonParams string) interface{} {
	caseUrl := "/time"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 3 API交易对 API default symbol
func ApiSymbol(jsonParams string) interface{} {
	caseUrl := "/defaultSymbols"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 4 交易规范信息 Exchange Information
func ExchangeInfo(jsonParams string) interface{} {
	caseUrl := "/exchangeInfo"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 5 深度信息 Depth
func Depth(jsonParams string) interface{} {
	caseUrl := "/depth"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 6 近期成交列表 Recent Trades List
func Trades(jsonParams string) interface{} {
	caseUrl := "/trades"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 7 近期成交列表（归集） Aggregate Trades List
func AggTrades(jsonParams string) interface{} {
	caseUrl := "/aggTrades"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 8 K线数据 K-line Data
func Kline(jsonParams string) interface{} {
	caseUrl := "/klines"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 9 当前平均价格 Current Average Price
func AvgPrice(jsonParams string) interface{} {
	caseUrl := "/avgPrice"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 10 24小时价格滚动情况 24hr Ticker Price Change Statistics
func Ticker24hr(jsonParams string) interface{} {
	caseUrl := "/ticker/24hr"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 11 最新价格 Symbol Price Ticker
func Price(jsonParams string) interface{} {
	caseUrl := "/ticker/price"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ### 12 当前最优挂单 Symbol Order Book Ticker
func BookTicker(jsonParams string) interface{} {
	caseUrl := "/ticker/bookTicker"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}

// ## 母子账户接口 Sub-Account Endpoints

// ### 1 创建子账户 Create a Sub-account(For Master Account)
func CreateSub(jsonParams string) interface{} {
	caseUrl := "/sub-account/virtualSubAccount"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 2 查看子账户列表 Query Sub-account List (For Master Account)
func QuerySub(jsonParams string) interface{} {
	caseUrl := "/sub-account/list"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 3 创建子账户的APIkey Create an APIKey for a sub-account (For Master Account)
func CreateSubApikey(jsonParams string) interface{} {
	caseUrl := "/sub-account/apiKey"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 4 查询子账户的APIKey Query the APIKey of a sub-account (For Master Account)
func QuerySubApikey(jsonParams string) interface{} {
	caseUrl := "/sub-account/apiKey"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 5 删除子账户的APIKey Delete the APIKey of a sub-account (For Master Account)
func DeleteSubApikey(jsonParams string) interface{} {
	caseUrl := "/sub-account/apiKey"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

// ### 6 母子用户万向划转 Universal Transfer (For Master Account)
func UniTransfer(jsonParams string) interface{} {
	caseUrl := "/capital/sub-account/universalTransfer"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 7 查询母子万向划转历史 Query Universal Transfer History (For Master Account)
func QueryUniTransfer(jsonParams string) interface{} {
	caseUrl := "/capital/sub-account/universalTransfer"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ## 现货账户和交易接口 Spot Account and Trade

// ### 1 用户API交易对 User API default symbol
func SelfSymbols(jsonParams string) interface{} {
	caseUrl := "/selfSymbols"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 2 测试下单 Test New Order
func TestOrder(jsonParams string) interface{} {
	caseUrl := "/order/test"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 3 下单 New Order
func PlaceOrder(jsonParams string) interface{} {
	caseUrl := "/order"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 4 批量下单 Batch Orders
func BatchOrder(jsonParams string) interface{} {
	caseUrl := "/batchOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 5 撤销订单 Cancel Order
func CancelOrder(jsonParams string) interface{} {
	caseUrl := "/order"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

// ### 6 撤销单一交易对所有订单 Cancel all Open Orders on a Symbol
func CancelAllOrders(jsonParams string) interface{} {
	caseUrl := "/openOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

// ### 7 查询订单 Query Order
func QueryOrder(jsonParams string) interface{} {
	caseUrl := "/order"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 8 当前挂单 Current Open Orders
func OpenOrder(jsonParams string) interface{} {
	caseUrl := "/openOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 9 查询所有订单 All Orders
func AllOrders(jsonParams string) interface{} {
	caseUrl := "/allOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 10 账户信息 Account Information
func SpotAccountInfo(jsonParams string) interface{} {
	caseUrl := "/account"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 11 账户成交历史 Account Trade List
func SpotmyTrade(jsonParams string) interface{} {
	caseUrl := "/myTrades"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 12 开启MX抵扣 Enable MX Deduct
func MxDeduct(jsonParams string) interface{} {
	caseUrl := "/mxDeduct/enable"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 13 查看MX抵扣状态 Query MX Deduct Status
func QueryMxDeduct(jsonParams string) interface{} {
	caseUrl := "/mxDeduct/enable"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ## 钱包接口 Wallet Endpoints

// ### 1 查询币种信息 Query the currency information
func QueryCurrencyInfo(jsonParams string) interface{} {
	caseUrl := "/capital/config/getall"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 2 提币 Withdraw
func Withdraw(jsonParams string) interface{} {
	caseUrl := "/capital/withdraw/apply"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 3 取消提币 Cancel withdraw
func CancelWithdraw(jsonParams string) interface{} {
	caseUrl := "/capital/withdraw"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

// ### 4 获取充值历史 Deposit History
func DepositHistory(jsonParams string) interface{} {
	caseUrl := "/capital/deposit/hisrec"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 5 获取提币历史 Withdraw History
func WithdrawHistory(jsonParams string) interface{} {
	caseUrl := "/capital/withdraw/historyl"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 6 生成充值地址 Generate deposit address
func GenDepositAddress(jsonParams string) interface{} {
	caseUrl := "/capital/deposit/address"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 7 获取充值地址 Deposit Address
func DepositAddress(jsonParams string) interface{} {
	caseUrl := "/capital/deposit/address"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 8 获取提币地址 Withdraw Address
func WithdrawAddress(jsonParams string) interface{} {
	caseUrl := "/capital/withdraw/address"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 9 用户万向划转 User Universal Transfer
func Transfer(jsonParams string) interface{} {
	caseUrl := "/capital/transfer"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 10 查询用户万向划转历史 Query User Universal Transfer History
func TransferHistory(jsonParams string) interface{} {
	caseUrl := "/capital/transfer"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 11 查询用户万向划转历史（根据tranId） Query User Universal Transfer History （by tranId）
func TransferHistoryById(jsonParams string) interface{} {
	caseUrl := "/capital/transfer/tranId"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 12 获取小额资产可兑换列表 Get Assets That Can Be Converted Into MX
func ConvertList(jsonParams string) interface{} {
	caseUrl := "/capital/convert/list"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 13 小额资产兑换 Dust Transfer
func Convert(jsonParams string) interface{} {
	caseUrl := "/capital/convert"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 14 查询小额资产兑换历史 DustLog
func ConvertHistory(jsonParams string) interface{} {
	caseUrl := "/capital/convert"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 15 获取ETF基础信息 Get ETF info
func ETFInfo(jsonParams string) interface{} {
	caseUrl := "/etf/info"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 16 用户站内转账 Internal Transfer
func InternalTransfer(jsonParams string) interface{} {
	caseUrl := "/capital/transfer/internal"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 17 用户站内转账历史 Internal Transfer History
func InternalTransferHistory(jsonParams string) interface{} {
	caseUrl := "/capital/transfer/internal"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ## WS ListenKey

// ### 1 生成 Listen Key  Create a ListenKey
func CreateListenKey(jsonParams string) interface{} {
	caseUrl := "/userDataStream"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

// ### 2 延长 Listen Key 有效期  Keep-alive a ListenKey
func KeepListenKey(jsonParams string) interface{} {
	caseUrl := "/userDataStream"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePut(requestUrl, jsonParams)
	return response
}

// ### 3 关闭 Listen Key  Close a ListenKey
func CloseListenKey(jsonParams string) interface{} {
	caseUrl := "/userDataStream"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

// ## 邀请返佣接口

// ### 1 获取邀请返佣记录 Get Rebate History Records
func RebateHistory(jsonParams string) interface{} {
	caseUrl := "/rebate/taxQuery"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 2 获取返佣记录明细 Get Rebate Records Detail
func RebateDetail(jsonParams string) interface{} {
	caseUrl := "/rebate/detail"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 3 获取自返记录明细 Get Self Rebate Records Detail
func SelfRecordsDetail(jsonParams string) interface{} {
	caseUrl := "/rebate/detail/kickback"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 4 获取邀请人 Query ReferCode
func ReferCode(jsonParams string) interface{} {
	caseUrl := "/rebate/referCode"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 5 获取代理邀请返佣记录 （代理账户）Get Affiliate Commission Record (affiliate only)
func AffiliateCommission(jsonParams string) interface{} {
	caseUrl := "/rebate/affiliate/commission"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 6 获取代理提现记录 （代理账户）Get Affiliate Withdraw Record (affiliate only)
func AffiliateWithdraw(jsonParams string) interface{} {
	caseUrl := "/rebate/affiliate/withdraw"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 7 获取代理返佣明细 （代理账户）Get Affiliate Commission Detail Record (affiliate only)
func AffiliateCommissionDetail(jsonParams string) interface{} {
	caseUrl := "/rebate/affiliate/commission/detail"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 8 查询直客页面数据 （代理账户）Get Affiliate Referral Data（affiliate only）
func AffiliateReferral(jsonParams string) interface{} {
	caseUrl := "/rebate/affiliate/referral"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

// ### 9 查询子代理页面数据 （代理账户）Get Subaffiliates Data (affiliate only)
func Subaffiliates(jsonParams string) interface{} {
	caseUrl := "/rebate/affiliate/subaffiliates"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}
