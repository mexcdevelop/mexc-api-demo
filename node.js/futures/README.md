# MEXC Futures SDK

Node.js CommonJS SDK for the MEXC Futures OpenAPI (REST + WebSocket).

This package provides:

- A **REST client** for public and private futures endpoints.
- A **WebSocket client** for public market streams and private `login + personal.filter` streams.

---

## Installation

Use npm to install all dependencies of the example project:

```bash
npm install
```

---

## Default configuration

The SDK has sensible defaults that match the official documentation:

| Type            | Default value                     | Overridden by              |
|-----------------|-----------------------------------|----------------------------|
| REST `baseURL`  | `https://api.mexc.com`           | `baseURL` in constructor   |
| WebSocket `wsURL` | `wss://contract.mexc.com/edge` | `wsURL` in constructor     |

You can override these when instantiating the clients if you need to point to
a sandbox or a regional endpoint.

---

## REST API

By default the REST client returns the **full server response**:

```json
{ "success": true, "code": 0, "data": { ... } }
```

If you prefer to only work with the `data` field, you can pass
`unwrapData: true` in the constructor.

```javascript
const { MexcFuturesRestClient } = require('./src')

async function main () {
  const client = new MexcFuturesRestClient({
    apiKey: 'YOUR_API_KEY',
    apiSecret: 'YOUR_API_SECRET',
    // baseURL: 'https://api.mexc.com', // optional override
    // unwrapData: true,                // only return the data field
  })

  const res = await client.getAssets()
  // If unwrapData is false:
  //   res = { success, code, data }
  // If unwrapData is true:
  //   res = data (asset list)

  console.log(res)
}

main().catch(console.error)
```

See `src/futuresRestClient.js` and the `examples/rest-*.js` files for more
endpoint-specific examples (public market data, account/position, orders,
plan/stop/track orders, STP, etc).

---

## WebSocket

### Public channels

```javascript
const { MexcFuturesWsClient } = require('./src')

const client = new MexcFuturesWsClient({
  // wsURL: 'wss://contract.mexc.com/edge', // optional override
})

client.on('open', () => {
  // Subscribe to a single ticker when the socket is open.
  client.subTicker('BTC_USDT')
})

client.on('message', (msg) => {
  console.log('message:', msg)
})

client.on('error', (err) => {
  console.error('ws error:', err)
})

client.connect()
```

The WS client supports all documented public futures channels, for example:

- `sub.tickers` / `unsub.tickers`
- `sub.ticker` / `unsub.ticker`
- `sub.deal` / `unsub.deal`
- `sub.depth` / `unsub.depth`
- `sub.depth.full` / `unsub.depth.full`
- `sub.kline` / `unsub.kline`
- `sub.funding.rate` / `unsub.funding.rate`
- `sub.index.price` / `unsub.index.price`
- `sub.fair.price` / `unsub.fair.price`
- `sub.contract` / `unsub.contract`
- `sub.event.contract` / `unsub.event.contract`

### Private login and personal.filter

```javascript
const { MexcFuturesWsClient } = require('./src')

const client = new MexcFuturesWsClient({
  apiKey: 'YOUR_API_KEY',
  apiSecret: 'YOUR_API_SECRET',
})

client.on('open', () => {
  // Login without auto-subscribing personal filters.
  client.login({ subscribe: false })
})

client.on('message', (msg) => {
  if (msg.channel === 'rs.login') {
    // After login, you can choose which personal streams to receive.
    client.filterAssets()
    client.filterPositions()
    // client.filterOrders({ symbols: ['BTC_USDT'] })
    // client.filterOrderDeals({ symbols: ['BTC_USDT'] })
  }

  console.log('message:', msg)
})

client.on('error', (err) => {
  console.error('ws error:', err)
})

client.connect()
```

The WS client exposes helpers for all documented personal filter streams,
including `filterAssets`, `filterOrders`, `filterOrderDeals`, `filterPositions`,
`filterPlanOrders`, `filterStopOrders`, `filterStopPlanOrders`, `filterRiskLimit`,
`filterAdlLevel`, `resetPersonalFilters`, and `filterCustom`.

---

## Examples

You can run the included Node examples directly:

```bash
node examples/rest-public.js
node examples/rest-private.js   # requires MEXC_API_KEY and MEXC_API_SECRET
node examples/ws-ticker.js
node examples/ws-depth.js
node examples/ws-private.js     # requires .env or environment variables
```

These examples demonstrate the basic usage of the REST and WebSocket clients,
and are a good starting point for integrating the SDK into your own project.
