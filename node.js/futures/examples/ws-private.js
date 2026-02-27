/**
 * MEXC Futures WebSocket private login example.
 * Requires MEXC_API_KEY / MEXC_API_SECRET.
 * Run: node examples/ws-private.js
 */

require('dotenv').config({ path: require('path').resolve(__dirname, '../.env') })

const { MexcFuturesWsClient } = require('../src')

const apiKey = process.env.MEXC_API_KEY || ''
const apiSecret = process.env.MEXC_API_SECRET || ''

if (!apiKey || !apiSecret) {
  console.log('Set env vars MEXC_API_KEY and MEXC_API_SECRET')
  console.log('Or create .env in contract dir')
  process.exit(1)
}

const client = new MexcFuturesWsClient({ apiKey, apiSecret })

client.on('open', () => {
  console.log('WebSocket connected')
  client.login({ subscribe: false })
})

client.on('message', (msg) => {
  if (msg.channel === 'rs.login') {
    console.log('login:', msg.data)
    client.filterAssets()
    client.filterPositions()
  } else if (msg.channel && msg.channel.startsWith('push.personal.')) {
    console.log(msg.channel, ':', JSON.stringify(msg.data || msg).slice(0, 120) + '...')
  } else {
    console.log('msg:', msg.channel || msg)
  }
})

client.on('error', (err) => console.error('error:', err.message))
client.on('close', (code) => console.log('closed:', code))

client.connect()
