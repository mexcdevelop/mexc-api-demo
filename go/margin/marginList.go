package marginList

import (
	"demo/config"
	"demo/utils"
	"fmt"
)

//杠杆请求配置

//切换杠杆账户模式
func MarginTradeMode(jsonParams string) interface{} {
	caseUrl := "/margin/tradeMode"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

//杠杆账户下单
func PlaceOrder(jsonParams string) interface{} {
	caseUrl := "/margin/order"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

//借贷
func Loan(jsonParams string) interface{} {
	caseUrl := "/margin/loan"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

//归还借贷
func Repay(jsonParams string) interface{} {
	caseUrl := "/margin/repay"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}

//撤销单一交易对的所有挂单
func DeleteOpenOrders(jsonParams string) interface{} {
	caseUrl := "/margin/openOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

//撤销订单
func CancelOrder(jsonParams string) interface{} {
	caseUrl := "/margin/order"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateDelete(requestUrl, jsonParams)
	return response
}

//查询借贷记录
func QueryLoan(jsonParams string) interface{} {
	caseUrl := "/margin/loan"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询历史委托记录
func QueryAllOrders(jsonParams string) interface{} {
	caseUrl := "/margin/allOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询历史成交记录
func QueryMyTrades(jsonParams string) interface{} {
	caseUrl := "/margin/myTrades"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询当前挂单记录
func QueryOpenOrders(jsonParams string) interface{} {
	caseUrl := "/margin/openOrders"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询最大可转出额
func QueryMaxTransferable(jsonParams string) interface{} {
	caseUrl := "/margin/maxTransferable"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询杠杆价格指数
func QueryPriceIndex(jsonParams string) interface{} {
	caseUrl := "/margin/priceIndex"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询杠杆账户订单详情
func QueryOrder(jsonParams string) interface{} {
	caseUrl := "/margin/order"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询杠杆逐仓账户信息
func QueryIsolatedAccount(jsonParams string) interface{} {
	caseUrl := "/margin/isolated/account"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询账户最大可借贷额度
func MaxBorrowable(jsonParams string) interface{} {
	caseUrl := "/margin/maxBorrowable"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询还贷记录
func QueryRepayRecord(jsonParams string) interface{} {
	caseUrl := "/margin/repay"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//查询逐仓杠杆交易对
func QueryIsolatedPair(jsonParams string) interface{} {
	caseUrl := "/margin/isolated/pair"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//获取账户强制平仓记录
func ForceLiquidationRec(jsonParams string) interface{} {
	caseUrl := "/margin/forceLiquidationRec"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//获取逐仓杠杆利率及限额
func IsolatedMarginData(jsonParams string) interface{} {
	caseUrl := "/margin/isolatedMarginData"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}

//获取逐仓档位信息
func IsolatedMarginTier(jsonParams string) interface{} {
	caseUrl := "/margin/isolatedMarginTier"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}
