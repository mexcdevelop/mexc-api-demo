package spotList

import (
	"fmt"
	"github.com/ross-ht/api-demo/config"
	"github.com/ross-ht/api-demo/utils"
)

//具体请求配置
func SpotMarketDepth(jsonParams string) interface{} {
	caseUrl := "/depth"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PublicGet(requestUrl, jsonParams)
	return response
}
func SpotAccountInfo(jsonParams string) interface{} {
	caseUrl := "/account"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}
func SpotmyTrade(jsonParams string) interface{} {
	caseUrl := "/myTrades"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivateGet(requestUrl, jsonParams)
	return response
}
func CreateSub(jsonParams string) interface{} {
	caseUrl := "/sub-account/virtualSubAccount"
	requestUrl := config.BASE_URL + caseUrl
	fmt.Println("requestUrl:", requestUrl)
	response := utils.PrivatePost(requestUrl, jsonParams)
	return response
}
