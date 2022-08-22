<!-- # api-demo
## v3 现货 api的调用
说明：
* **公共接口**
    1. 进入main.go中，无参数的请求直接调用
    2. 带参数的请求需要在params中输入json格式的参数 如：`{"symbol":"BTCUSDT",	"limit":"200"}`
* **私有接口**
    1. 需要先在config.go中配置相关的api_key和sec_key
    2. 后续操作与公共接口相同

## 现货ws的调用
**说明：**
1. 进入publicws.go,在payload参数中输入相应的json格式的request payload
2. 私有频道用privateWs.go进行订阅 -->
# API-Demo
## V3 Spot API
Description:
* **Public API**
    1. access to main.go，create a function you want to call in spotList.go. eg:
       > `depthinfo := spotList.SpotMarketDepth(params)`
    2. input the params in json format if this request need any params,like：
       > `var params string = {"symbol":"BTCUSDT","limit":"200"}`  
    3. if no params needed, just write 
       > `var params string =""`
    4. run the request with code 
       > `go run main.go`
* **Private API**
    1. input the `api_key` and `sec_key` in config.go first
    2. same as how to call Public api in next

## WebSocket For Spot
**Description:：**
1. access to publicws.go,input request payload in `payload` (json format).eg:
    > `{"symbol":"MX_USDT","op":"sub.symbol"}`
2. subscribe the private information by `privateWs.go`
3. run the request with code 
    > `go run publicws.go`