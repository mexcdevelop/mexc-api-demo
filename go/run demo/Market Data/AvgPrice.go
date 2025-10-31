package main

import (
	spotList "demo/spot"
	"fmt"
)

//说明：
//在params中输入json格式的参数,如:`{"symbol":"BTCUSDT",	"limit":"200"}`
//如果没有参数则输入:""

// 现货参数
var params string = `{"symbol":"️BTCUSDT"}`

func main() {

	//接口调用
	AvgPrice := spotList.AvgPrice(params)
	fmt.Println("返回信息:", AvgPrice)

}
