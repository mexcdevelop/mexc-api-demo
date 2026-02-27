/**
 * MEXC Futures WebSocket ticker example.
 * Run: node examples/ws-ticker.js
 */

const { MexcFuturesWsClient } = require('../src')

const client = new MexcFuturesWsClient()

client.on('open', () => {
  console.log('WebSocket connected')
  client.subTicker('BTC_USDT')
})

client.on('message', (msg) => {
  if (msg.channel === 'push.ticker' && msg.data) {
    const d = msg.data
    console.log(
      '[ticker]',
      d.symbol,
      'last:', d.lastPrice,
      'bid:', d.bid1,
      'ask:', d.ask1,
      'vol24:', d.volume24
    )
  }
})

client.on('error', (err) => console.error('error:', err.message))
client.on('close', (code) => console.log('closed:', code))

client.connect()
