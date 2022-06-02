const { Console } = require('console')
const fs = require('fs')
const Spot = require('../../../src/spot')
const { validateRequiredParameters } = require('../helpers/validation')
const { isEmptyValue } = require('../helpers/utils')
const WebSocketClient = require('ws')


const Websocket = superclass => class extends superclass {
  constructor (options) {
    super(options)
    this.wsURL = options.wsURL || 'wss://contract.mexc.com/ws'
    this.reconnectDelay = 1000
  }


  aggTradeWS (symbol, callbacks) {
    validateRequiredParameters({ symbol })
    const url = `${this.wsURL}/ws/${symbol.toLowerCase()}@aggTrade`
    return this.subscribe(url, callbacks)
  }

  tradeWS (symbol, callbacks) {
    validateRequiredParameters({ symbol })
    const url = `${this.wsURL}/ws/${symbol.toLowerCase()}@trade`
    return this.subscribe(url, callbacks)
  }


  klineWS (symbol, interval, callbacks) {
    validateRequiredParameters({ symbol, interval })

    const url = `${this.wsURL}/ws/${symbol.toLowerCase()}@kline_${interval}`
    return this.subscribe(url, callbacks)
  }

  miniTickerWS (symbol = null, callbacks) {
    let path = '!miniTicker@arr'
    if (!isEmptyValue(symbol)) {
      path = `${symbol.toLowerCase()}@miniTicker`
    }
    const url = `${this.wsURL}/ws/${path}`
    return this.subscribe(url, callbacks)
  }


  tickerWS (symbol = null, callbacks) {
    let path = '!ticker@arr'
    if (!isEmptyValue(symbol)) {
      path = `${symbol.toLowerCase()}@ticker`
    }
    const url = `${this.wsURL}/ws/${path}`
    return this.subscribe(url, callbacks)
  }


  bookTickerWS (symbol = null, callbacks) {
    let path = '!bookTicker'
    if (!isEmptyValue(symbol)) {
      path = `${symbol.toLowerCase()}@bookTicker`
    }
    const url = `${this.wsURL}/ws/${path}`
    return this.subscribe(url, callbacks)
  }


  partialBookDepth (symbol, levels, speed, callbacks) {
    validateRequiredParameters({ symbol, levels, speed })

    const url = `${this.wsURL}/ws/${symbol.toLowerCase()}@depth${levels}@${speed}`
    return this.subscribe(url, callbacks)
  }


  diffBookDepth (symbol, speed, callbacks) {
    validateRequiredParameters({ symbol, speed })

    const url = `${this.wsURL}/ws/${symbol.toLowerCase()}@depth@${speed}`
    return this.subscribe(url, callbacks)
  }


  userData (listenKey, callbacks) {
    validateRequiredParameters({ listenKey })

    const url = `${this.wsURL}/ws/${listenKey}`
    return this.subscribe(url, callbacks)
  }


  combinedStreams (streams, callbacks) {
    if (!Array.isArray(streams)) {
      streams = [streams]
    }

    const url = `${this.wsURL}/stream?streams=${streams.join('/')}`
    return this.subscribe(url, callbacks)
  }

  subscribe (url, callbacks) {
    const wsRef = {}
    wsRef.closeInitiated = false
    const initConnect = () => {
      const ws = new WebSocketClient(url)
      wsRef.ws = ws
      Object.keys(callbacks).forEach((event, _) => {
        this.logger.debug(`listen to event: ${event}`)
        ws.on(event, callbacks[event])
      })

      ws.on('ping', () => {
        this.logger.debug('Received ping from server')
        ws.pong()
      })

      ws.on('pong', () => {
        this.logger.debug('Received pong from server')
      })

      ws.on('error', err => {
        this.logger.error(err)
      })

      ws.on('close', (closeEventCode, reason) => {
        if (!wsRef.closeInitiated) {
          this.logger.error(`Connection close due to ${closeEventCode}: ${reason}.`)
          setTimeout(() => {
            this.logger.debug('Reconnect to the server.')
            initConnect()
          }, this.reconnectDelay)
        } else {
          wsRef.closeInitiated = false
        }
      })
    }
    this.logger.debug(url)
    initConnect()
    return wsRef
  }


  unsubscribe (wsRef) {
    if (!wsRef || !wsRef.ws) this.logger.warn('No connection to close.')
    else {
      wsRef.closeInitiated = true
      wsRef.ws.close()
    }
  }
}

module.exports = Websocket

const output = fs.createWriteStream('./stdout.log')
const errorOutput = fs.createWriteStream('./stderr.log')
const logger = new Console({
  stdout: output,
  stderr: errorOutput
})

const client = new Spot('', '', {
  logger
})

const callbacks = {
  open: () => client.logger.debug('open'),
  close: () => client.logger.debug('closed'),
  message: data => client.logger.log(data)
}

const wsRef = client.klineWS('bnbusdt', '1m', callbacks)
setTimeout(() => client.unsubscribe(wsRef), 5000)