# Mexc Nodejs Demo

Before you use the demo, you need to generate your apikey & apisecret, then enter them first.

* <https://www.mexc.com/user/openapi>

## Spot V2、V3 Demo 

Fill in the corresponding function according to the parameters mentioned in the API documentation and execute it. => `print()`

**Rest API V2 doc**   `URL = 'https://www.mexc.com'`

* <https://mxcdevelop.github.io/apidocs/spot_v2_cn/#5ea2e0cde2>

**Rest API V3 doc**   `URL = 'https://api.mexc.com'`

* <https://mxcdevelop.github.io/apidocs/spot_v3_cn/#45fa4e00db>


> ### Example(Spot V3) :
```node.js
const Spot = require('../../../src/Future')

const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })

client.newOrder('BNBUSDT', 'BUY', 'LIMIT', {
  price: '10',
  quantity: 1 
}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))
```

## Spot Websocket Demo 

According to the information you want to subscribe, change the content of the params according to the websocket documentation, ex: "op" or "symbol". 
**WebSocket doc**   `URL = 'wss://wbs.mexc.com/raw/ws'`

* <https://github.com/mxcdevelop/APIDoc/blob/master/websocket/spot/websocket.2022_2_28.md>


> ### Example(Spot WebSocket) :
const { Console } = require('console')
const fs = require('fs')
const Spot = require('../../../src/spot')

const output = fs.createWriteStream('./stdout.log')
const errorOutput = fs.createWriteStream('./stderr.log')
const logger = new Console({
  stdout: output,
  stderr: errorOutput
})

const client = new Spot('', '', {
  logger
})

const callbacks = {
  open: () => client.logger.debug('open'),
  close: () => client.logger.debug('closed'),
  message: data => client.logger.log(data)
}

const wsRef = client.aggTradeWS('bnbusdt', callbacks)
setTimeout(() => client.unsubscribe(wsRef), 5000)
// check the output file