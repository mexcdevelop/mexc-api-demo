/**
 * MEXC Futures private endpoints example.
 * Returns full server response { success, code, data } by default.
 * Requires API Key / Secret (.env or env vars).
 * Run: node examples/rest-private.js
 */

require('dotenv').config({ path: require('path').resolve(__dirname, '../.env') })

const { MexcFuturesRestClient } = require('../src')

const apiKey = process.env.MEXC_API_KEY || ''
const apiSecret = process.env.MEXC_API_SECRET || ''

if (!apiKey || !apiSecret) {
  console.log('Set env vars MEXC_API_KEY and MEXC_API_SECRET')
  console.log('Or create .env in contract dir')
  process.exit(1)
}

const client = new MexcFuturesRestClient({
  apiKey,
  apiSecret,
  timeoutMs: 10000,
  recvWindow: 10000
})

async function main () {
  try {
    const r1 = await client.getAssets()
    console.log('getAssets full response success:', r1.success, 'code:', r1.code)
    console.log('response.data (asset list):', Array.isArray(r1.data) ? r1.data.length + ' items' : r1.data)

    const r2 = await client.getPositions({})
    console.log('getPositions:', r2.success ? (r2.data?.length ?? 0) + ' open' : r2)
  } catch (e) {
    console.error(e.message)
  }
}

main()
