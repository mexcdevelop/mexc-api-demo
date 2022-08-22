package main

import (
	"demo/margin"
	"demo/spot"
	"fmt"
)

//说明：
//在params中输入json格式的参数 如：`{"symbol":"BTCUSDT",	"limit":"200"}`

//现货参数
var spotparams string = `{"symbol":"BTCUSDT",	"limit":"100"}`

//杠杆参数
var marginparams string = `{"tradeMode":"0",	"symbol":"BTCUSDT"}`
var marginparams1 string = `{"asset":"BTC",	"symbol":"BTCUSDT", "tranId":"2597392"}`

func main() {

	//现货接口调用
	depthinfo := spotList.SpotMarketDepth(spotparams)
	fmt.Println("返回信息:", depthinfo)

	//杠杆接口调用
	marginmode := marginList.MarginTradeMode(marginparams)
	fmt.Println("返回信息:", marginmode)

	repayrecord := marginList.QueryRepayRecord(marginparams1)
	fmt.Println("返回信息:", repayrecord)

	// accountInfo := spotList.SpotmyTrade(`{
	// 	"symbol":"MXUSDT"
	// }`)
	// fmt.Println("返回信息:", accountInfo)
	// subAccount := spotList.CreateSub(params)
	// fmt.Println("返回信息:", subAccount)

}
