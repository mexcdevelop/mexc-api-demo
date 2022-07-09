# Mexc in Nodejs

## Installation

```
npm install 
```

## RESTful APIs

```javascript
const Spot = require('../../src/spot')

const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://contract.mexc.com' })

client.newOrder('BNBUSDT', 'BUY', 'LIMIT', {
  price: 10,
  quantity: 1 
}).then(response => client.logger.log(response.data))
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
const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret)

client.account({ recvWindow: 2000 }).then(response => client.logger.log(response.data))

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