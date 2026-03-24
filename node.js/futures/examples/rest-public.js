/**
 * MEXC Futures public endpoints example.
 * Returns full server response { success, code, data } by default.
 * Run: node examples/rest-public.js
 */

const { MexcFuturesRestClient } = require('../src')

const client = new MexcFuturesRestClient()

async function main () {
  try {
    const r1 = await client.ping()
    console.log('ping full response:', JSON.stringify(r1).slice(0, 80) + '...')
    console.log('ping success:', r1.success, 'data:', r1.data)

    const r2 = await client.getContractDetail('BTC_USDT')
    console.log('contractDetail success:', r2.success, 'data.symbol:', r2.data?.symbol)

    const r3 = await client.getTicker('BTC_USDT')
    console.log('ticker lastPrice:', r3.data?.lastPrice)

    const r4 = await client.getDepth('BTC_USDT', 5)
    console.log('depth bids count:', r4.data?.bids?.length)
  } catch (e) {
    console.error(e.message)
  }
}

main()
