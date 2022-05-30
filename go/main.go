package main

import (
	"fmt"

	spotList "github.com/ross-ht/api-demo/spot"
)

//说明：
//在params中输入json格式的参数 如：`{"symbol":"BTCUSDT",	"limit":"200"}`
var params string = `{"symbol":"BTCUSDT",	"limit":"200"}`

func main() {

	depthinfo := spotList.SpotMarketDepth(params)
	fmt.Println("返回信息:", depthinfo)

	// accountInfo := spotList.SpotmyTrade(`{
	// 	"symbol":"MXUSDT"
	// }`)
	// fmt.Println("返回信息:", accountInfo)
	// subAccount := spotList.CreateSub(params)
	// fmt.Println("返回信息:", subAccount)

}
