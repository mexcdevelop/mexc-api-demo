/**
 * MEXC Futures WebSocket client.
 */

const WebSocket = require('ws')
const zlib = require('zlib')
const { sign } = require('../signer')

const DEFAULT_WS_URL = 'wss://contract.mexc.co/edge'
const PING_INTERVAL_MS = 15000
const MAX_RECONNECT_DELAY_MS = 60000
const INITIAL_RECONNECT_DELAY_MS = 1000

function _toSymbolParam (params) {
  if (params == null) return {}
  if (typeof params === 'string') return { symbol: params }
  const p = { ...params }
  if (p.symbol != null && p.symbol !== '') return p
  return p
}

function _normalizeSymbols (symbols) {
  if (symbols == null || symbols === undefined) return undefined
  if (Array.isArray(symbols)) {
    const arr = symbols.filter(s => s != null && s !== '')
    if (arr.length === 0) return undefined
    const invalid = arr.find(s => typeof s !== 'string')
    if (invalid !== undefined) {
      throw new TypeError('symbols must be string array')
    }
    return arr
  }
  throw new TypeError('symbols must be string array')
}

class MexcFuturesWsClient {
  /**
   * @param {Object} [options]
   * @param {string} [options.wsURL] - Default wss://contract.mexc.com/edge
   * @param {string} [options.apiKey] - For private login
   * @param {string} [options.apiSecret] - For private login
   */
  constructor (options = {}) {
    this.wsURL = options.wsURL || DEFAULT_WS_URL
    this.apiKey = options.apiKey || ''
    this.apiSecret = options.apiSecret || ''
    this.ws = null
    this.pingTimer = null
    this.reconnectAttempts = 0
    this._handlers = Object.create(null)
    this._manualDisconnect = false
    this._reconnectTimer = null
  }

  connect () {
    this._manualDisconnect = false
    if (this.ws && (this.ws.readyState === WebSocket.CONNECTING || this.ws.readyState === WebSocket.OPEN)) {
      return
    }
    this.ws = new WebSocket(this.wsURL)
    this.ws.binaryType = 'arraybuffer'
    this.ws.on('open', () => this._onOpen())
    this.ws.on('message', (data) => this._onMessage(data))
    this.ws.on('error', (err) => this._onError(err))
    this.ws.on('close', (code, reason) => this._onClose(code, reason))
  }

  disconnect () {
    this._manualDisconnect = true
    this._stopPing()
    if (this._reconnectTimer != null) {
      clearTimeout(this._reconnectTimer)
      this._reconnectTimer = null
    }
    if (this.ws) {
      this.ws.removeAllListeners()
      this.ws.close()
      this.ws = null
    }
    this.reconnectAttempts = 0
  }

  send (data) {
    if (!this.ws || this.ws.readyState !== WebSocket.OPEN) return false
    const str = typeof data === 'string' ? data : JSON.stringify(data)
    this.ws.send(str)
    return true
  }

  /**
   * Send ping manually (server returns pong).
   * @wsChannel ping
   */
  ping () {
    return this.send({ method: 'ping' })
  }

  subscribe (method, param = {}) {
    return this.send({ method, param })
  }

  unsubscribe (method, param = {}) {
    return this.send({ method, param })
  }

  /**
   * Subscribe to all tickers.
   * @wsChannel sub.tickers
   * @param {{}} [params]
   */
  subTickers (params = {}) {
    return this.subscribe('sub.tickers', params)
  }

  /**
   * Unsubscribe from all tickers.
   * @wsChannel unsub.tickers
   * @param {{}} [params]
   */
  unsubTickers (params = {}) {
    return this.unsubscribe('unsub.tickers', params)
  }

  /**
   * Subscribe to single ticker.
   * @wsChannel sub.ticker
   * @param {{symbol: string}} params
   */
  subTicker (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.ticker', p)
  }

  /**
   * Unsubscribe ticker.
   * @wsChannel unsub.ticker
   * @param {{symbol: string}} params
   */
  unsubTicker (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.ticker', p)
  }

  /**
   * Subscribe to deals.
   * @wsChannel sub.deal
   * @param {{symbol: string}} params
   */
  subDeal (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.deal', p)
  }

  /**
   * Unsubscribe deals.
   * @wsChannel unsub.deal
   * @param {{symbol: string}} params
   */
  unsubDeal (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.deal', p)
  }

  /**
   * Subscribe to incremental depth (optional limit 5/10/20, compress true/false).
   * @wsChannel sub.depth
   * @param {{symbol: string, limit?: number, compress?: boolean}} params
   */
  subDepth (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.depth', p)
  }

  /**
   * Unsubscribe incremental depth.
   * @wsChannel unsub.depth
   * @param {{symbol: string}} params
   */
  unsubDepth (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.depth', p)
  }

  /**
   * Subscribe to full depth (limit 5/10/20).
   * @wsChannel sub.depth.full
   * @param {{symbol: string, limit?: number}} params
   */
  subDepthFull (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.depth.full', p)
  }

  /**
   * Unsubscribe full depth.
   * @wsChannel unsub.depth.full
   * @param {{symbol: string}} params
   */
  unsubDepthFull (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.depth.full', p)
  }

  /**
   * Subscribe to K-line.
   * @wsChannel sub.kline
   * @param {{symbol: string, interval: string}} params - interval: Min1,Min5,Min15,Min30,Min60,Hour4,Hour8,Day1,Week1,Month1
   */
  subKline (params) {
    const p = _toSymbolParam(params)
    if (!p.interval) throw new TypeError('params.interval required for subKline')
    return this.subscribe('sub.kline', p)
  }

  /**
   * Unsubscribe K-line.
   * @wsChannel unsub.kline
   * @param {{symbol: string, interval: string}} params
   */
  unsubKline (params) {
    const p = _toSymbolParam(params)
    if (!p.interval) throw new TypeError('params.interval required for unsubKline')
    return this.unsubscribe('unsub.kline', p)
  }

  /**
   * Subscribe to funding rate.
   * @wsChannel sub.funding.rate
   * @param {{symbol: string}} params
   */
  subFundingRate (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.funding.rate', p)
  }

  /**
   * Unsubscribe funding rate.
   * @wsChannel unsub.funding.rate
   * @param {{symbol: string}} params
   */
  unsubFundingRate (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.funding.rate', p)
  }

  /**
   * Subscribe to index price.
   * @wsChannel sub.index.price
   * @param {{symbol: string}} params
   */
  subIndexPrice (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.index.price', p)
  }

  /**
   * Unsubscribe index price.
   * @wsChannel unsub.index.price
   * @param {{symbol: string}} params
   */
  unsubIndexPrice (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.index.price', p)
  }

  /**
   * Subscribe to fair price.
   * @wsChannel sub.fair.price
   * @param {{symbol: string}} params
   */
  subFairPrice (params) {
    const p = _toSymbolParam(params)
    return this.subscribe('sub.fair.price', p)
  }

  /**
   * Unsubscribe fair price.
   * @wsChannel unsub.fair.price
   * @param {{symbol: string}} params
   */
  unsubFairPrice (params) {
    const p = _toSymbolParam(params)
    return this.unsubscribe('unsub.fair.price', p)
  }

  /**
   * Subscribe to contract data. No symbol param; receives all contract metadata updates.
   * @wsChannel sub.contract
   * @param {{}} [params]
   */
  subContract (params = {}) {
    return this.subscribe('sub.contract', params)
  }

  /**
   * Unsubscribe from contract data.
   * @wsChannel unsub.contract
   * @param {{}} [params]
   */
  unsubContract (params = {}) {
    return this.unsubscribe('unsub.contract', params)
  }

  /**
   * Subscribe to event contract data. No symbol param; receives event contract updates.
   * @wsChannel sub.event.contract
   * @param {{}} [params]
   */
  subEventContract (params = {}) {
    return this.subscribe('sub.event.contract', params)
  }

  /**
   * Unsubscribe from event contract data.
   * @wsChannel unsub.event.contract
   * @param {{}} [params]
   */
  unsubEventContract (params = {}) {
    return this.unsubscribe('unsub.event.contract', params)
  }

  /**
   * Private login.
   * @wsChannel login
   * @param {Object} [opts]
   * @param {boolean} [opts.subscribe=false] - Subscribe to all private pushes; when false, use personal.filter only
   */
  login (opts = {}) {
    const { subscribe = false } = opts
    if (!this.apiKey || !this.apiSecret) {
      throw new Error('MexcFuturesWsClient: apiKey and apiSecret required for login')
    }
    const reqTime = String(Date.now())
    const signature = sign({
      accessKey: this.apiKey,
      apiSecret: this.apiSecret,
      requestTime: reqTime,
      method: 'GET',
      params: {}
    })
    const msg = {
      method: 'login',
      param: {
        apiKey: this.apiKey,
        reqTime,
        signature
      }
    }
    if (!subscribe) {
      msg.subscribe = false
    }
    return this.send(msg)
  }

  /**
   * personal.filter: push assets only. Later rules override earlier rules.
   * @wsChannel personal.filter
   */
  filterAssets () {
    return this.send({
      method: 'personal.filter',
      param: { filters: [{ filter: 'asset' }] }
    })
  }

  /**
   * personal.filter: push orders only.
   * @wsChannel personal.filter
   * @param {Object|string[]} [params] - params.symbols or pass symbols array directly
   */
  filterOrders (params) {
    const rules = Array.isArray(params) ? _normalizeSymbols(params) : _normalizeSymbols(params?.symbols)
    const f = { filter: 'order' }
    if (rules && rules.length) f.rules = rules
    return this.send({ method: 'personal.filter', param: { filters: [f] } })
  }

  /**
   * personal.filter: push order deals only.
   * @wsChannel personal.filter
   * @param {Object|string[]} [params] - params.symbols or pass symbols array directly
   */
  filterOrderDeals (params) {
    const rules = Array.isArray(params) ? _normalizeSymbols(params) : _normalizeSymbols(params?.symbols)
    const f = { filter: 'order.deal' }
    if (rules && rules.length) f.rules = rules
    return this.send({ method: 'personal.filter', param: { filters: [f] } })
  }

  /**
   * personal.filter: push positions only.
   * @wsChannel personal.filter
   * @param {Object|string[]} [params] - params.symbols or pass symbols array directly
   */
  filterPositions (params) {
    const rules = Array.isArray(params) ? _normalizeSymbols(params) : _normalizeSymbols(params?.symbols)
    const f = { filter: 'position' }
    if (rules && rules.length) f.rules = rules
    return this.send({ method: 'personal.filter', param: { filters: [f] } })
  }

  /**
   * Reset personal.filter to restore all private pushes.
   * @wsChannel personal.filter (filters: [])
   */
  resetPersonalFilters () {
    return this.send({ method: 'personal.filter', param: { filters: [] } })
  }

  on (event, handler) {
    if (!this._handlers[event]) this._handlers[event] = []
    this._handlers[event].push(handler)
    return this
  }

  off (event, handler) {
    const list = this._handlers[event]
    if (!list) return this
    if (!handler) {
      this._handlers[event] = []
      return this
    }
    this._handlers[event] = list.filter(h => h !== handler)
    return this
  }

  _emit (event, ...args) {
    const list = this._handlers[event]
    if (list && list.length) {
      list.forEach(h => {
        try { h(...args) } catch (e) { this._onError(e) }
      })
    }
  }

  _onOpen () {
    this.reconnectAttempts = 0
    this._startPing()
    this._emit('open')
  }

  _onMessage (data) {
    let obj
    try {
      obj = this._parseMessage(data)
    } catch (err) {
      this._emit('error', err)
      return
    }
    if (!obj) return

    if (obj.channel === 'pong') {
      this._emit('pong', obj.data)
      return
    }

    if (obj.channel === 'rs.login') {
      this._emit('message', obj)
      return
    }

    if (obj.channel === 'rs.error') {
      this._emit('message', obj)
      this._emit('error', new Error(obj.data?.msg || obj.data || 'login/filter error'))
      return
    }

    this._emit('message', obj)
  }

  _parseMessage (data) {
    let str
    if (typeof data === 'string') {
      str = data
    } else if (Buffer.isBuffer(data)) {
      str = data.toString('utf8')
    } else if (data instanceof ArrayBuffer) {
      const buf = Buffer.from(data)
      try {
        str = zlib.gunzipSync(buf).toString('utf8')
      } catch {
        str = buf.toString('utf8')
      }
    } else {
      str = String(data)
    }

    try {
      return JSON.parse(str)
    } catch (err) {
      throw new Error('WebSocket message parse error: ' + err.message)
    }
  }

  _onError (err) {
    this._emit('error', err)
  }

  _onClose (code, reason) {
    this._stopPing()
    this._emit('close', code, reason)
    this._scheduleReconnect()
  }

  _scheduleReconnect () {
    if (this._manualDisconnect) return
    if (this._reconnectTimer != null) return
    const delay = Math.min(
      INITIAL_RECONNECT_DELAY_MS * Math.pow(2, this.reconnectAttempts),
      MAX_RECONNECT_DELAY_MS
    )
    this.reconnectAttempts++
    this.ws = null
    this._reconnectTimer = setTimeout(() => {
      this._reconnectTimer = null
      if (this._manualDisconnect) return
      this._emit('reconnect')
      this.connect()
    }, delay)
  }

  _startPing () {
    this._stopPing()
    this.pingTimer = setInterval(() => {
      this.send({ method: 'ping' })
    }, PING_INTERVAL_MS)
  }

  _stopPing () {
    if (this.pingTimer) {
      clearInterval(this.pingTimer)
      this.pingTimer = null
    }
  }
}

module.exports = { MexcFuturesWsClient, DEFAULT_WS_URL }
