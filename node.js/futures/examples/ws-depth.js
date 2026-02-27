/**
 * MEXC Futures WebSocket depth example.
 * Run: node examples/ws-depth.js
 */

const { MexcFuturesWsClient } = require('../src')

const client = new MexcFuturesWsClient()

client.on('open', () => {
  console.log('WebSocket connected')
  client.subDepth('BTC_USDT')
})

client.on('message', (msg) => {
  if (msg.channel === 'push.depth' && msg.data) {
    const d = msg.data
    const asks = (d.asks || []).slice(0, 3)
    const bids = (d.bids || []).slice(0, 3)
    console.log('[depth]', msg.symbol, 'v', d.version)
    console.log('  asks:', asks)
    console.log('  bids:', bids)
  }
})

client.on('error', (err) => console.error('error:', err.message))
client.on('close', (code) => console.log('closed:', code))

client.connect()
