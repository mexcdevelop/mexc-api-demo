# Mexc in Nodejs

## Installation

```
npm install 
```

## RESTful APIs

```javascript
const Future = require('../../src/future')

const apiKey = ''
const apiSecret = ''
const client = new Future(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })

client.PlaceNewOrder({
    symbol:'IMX_USDT',
    price: 0.1,
    vol: 10,
    side:1,
    type:1,
    openType:2
  }).then(response => client.logger.log(response.data)) 
```

```javascript

const Future = require('../../src/future')

const client = new Future()

client.Ticker({symbol:'BTC_USDT'}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

```

Please find `modules` folder to check for more endpoints.


### Base URL
# V1
`https://contract.mexc.com`
# V2
`https://www.mexc.com`
# V3
`https://api.mexc.com`

### Optional Parameters

Optional parameters are encapsulated to a single object as the last function parameter.

```javascript
const Spot = require('../../src/spot')

const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })

client.AccountInformation().then(response => client.logger.log(response.data)) 
```

```javascript
const Spot = require('../../src/spot')

const client = new Spot()
client.Depth({symbol:'BTCUSDT',depth:5}).then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

```

## Websocket
## Environmental requirements
nodejs 6.0+

## Demo Description
ws.js
Demo using spot websocket

## User's Guide
```
node ws.js
```