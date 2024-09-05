# Mexc in Nodejs

## Installation

```
npm install 
```

## RESTful APIs

```javascript
const Spot = require('../src/spot')
const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })


client.CancelWithdraw().then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))
```

```javascript
const Spot = require('../src/spot')
const apiKey = ''
const apiSecret = ''
const client = new Spot(apiKey, apiSecret, { baseURL: 'https://api.mexc.com' })


client.AccountInformation().then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))

```

Please find `modules` folder to check for more endpoints.


### Base URL
# Contract
`https://contract.mexc.com`
# spot
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