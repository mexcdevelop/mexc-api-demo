/**
 * MEXC Futures WebSocket single-action verification script (params-only).
 * - Run from contract root: `node run-ws.js` or `node .\run-ws.js`
 * - Private actions require contract/.env (MEXC_API_KEY, MEXC_API_SECRET)
 * - This script keeps the connection open and prints all raw messages
 */

require('dotenv').config({ path: require('path').resolve(__dirname, '.env') })

const { MexcFuturesWsClient } = require('./src')

// Minimal target configuration: action + WS document params only.
const target = {
  actions: [
    { action: 'subTicker', params: { symbol: 'BTC_USDT' } }
  
  ]
}
// Available action groups (for help / validation only).
const WS_ACTIONS = {
  base: ['connectOnly', 'ping', 'sendRaw'],
  public: [
    'subTicker', 'unsubTicker', 'subTickers', 'unsubTickers',
    'subDeal', 'unsubDeal', 'subDepth', 'unsubDepth',
    'subDepthFull', 'unsubDepthFull', 'subKline', 'unsubKline',
    'subFundingRate', 'unsubFundingRate', 'subIndexPrice', 'unsubIndexPrice',
    'subFairPrice', 'unsubFairPrice',
    'subContract', 'unsubContract', 'subEventContract', 'unsubEventContract'
  ],
  private: [
    'login',
    'filterAssets',
    'filterOrders',
    'filterOrderDeals',
    'filterPositions',
    'resetPersonalFilters',
    'filterPlanOrders',
    'filterStopOrders',
    'filterStopPlanOrders',
    'filterRiskLimit',
    'filterAdlLevel',
    'filterCustom',
    'loginThenFilterCustom',
    'loginThenFilterRiskLimit',
    'loginThenFilterAdlLevel'
  ]
}

function getAvailableActions () {
  return [...WS_ACTIONS.base, ...WS_ACTIONS.public, ...WS_ACTIONS.private]
}

function findSimilarActions (action) {
  const all = getAvailableActions()
  const lower = String(action).toLowerCase()
  return all.filter(a => {
    const aLower = a.toLowerCase()
    return aLower.includes(lower) || lower.includes(aLower)
  })
}

// Map combined login+filter actions to their underlying filter action.
function getCombinedLoginFilterAction (action) {
  switch (action) {
    case 'loginThenFilterCustom': return 'filterCustom'
    case 'loginThenFilterRiskLimit': return 'filterRiskLimit'
    case 'loginThenFilterAdlLevel': return 'filterAdlLevel'
    default: return null
  }
}

// Best-effort detection of a successful login response message.
function isLoginSuccessMessage (msg) {
  if (!msg || typeof msg !== 'object') return false
  const channel = msg.channel

  if (channel === 'rs.login') {
    const data = msg.data
    if (data == null) return true
    if (data === 'success') return true
    if (typeof data === 'object') {
      if (data.success === true) return true
      if (data.code === 0) return true
    }
    // Any rs.login that is not an explicit error is treated as success;
    // actual errors should arrive on rs.error according to docs.
    return true
  }

  if (typeof channel === 'string') {
    const lower = channel.toLowerCase()
    if (lower.includes('login') && channel !== 'rs.error') {
      return true
    }
  }

  return false
}

// ---- personal.filter helpers ----

// Fixed allowed personal.filter keys from WS documentation.
function getAllowedPersonalFilterKeys () {
  return [
    'order',
    'order.deal',
    'position',
    'plan.order',
    'stop.order',
    'stop.planorder',
    'risk.limit',
    'adl.level',
    'asset'
  ]
}

function validatePersonalFilterKey (key) {
  const allowed = getAllowedPersonalFilterKeys()
  if (!allowed.includes(key)) {
    throw new Error(
      `Invalid personal.filter key: ${key}. Allowed keys: ${allowed.join(', ')}`
    )
  }
}

function validateSymbolsArray (symbols) {
  if (symbols == null) return undefined
  if (!Array.isArray(symbols)) {
    throw new Error('params.symbols must be an array of strings')
  }
  const arr = symbols.filter(s => s != null && s !== '')
  if (arr.length === 0) return undefined
  const invalid = arr.find(s => typeof s !== 'string')
  if (invalid !== undefined) {
    throw new Error('params.symbols must be an array of strings')
  }
  return arr
}

// Build a single filter object from a key and params (supports optional params.symbols).
function makeSingleFilter (filterKey, params) {
  validatePersonalFilterKey(filterKey)
  const filter = { filter: filterKey }
  const symbols = params && params.symbols
  const rules = validateSymbolsArray(symbols)
  if (rules && rules.length) filter.rules = rules
  return filter
}

function buildPersonalFilterPayload (filters) {
  if (!Array.isArray(filters)) {
    throw new Error('filters must be an array')
  }
  return {
    method: 'personal.filter',
    param: { filters }
  }
}

// Normalize target into an action queue: [{ action, params }, ...].
function normalizeActionQueue (targetConfig) {
  if (Array.isArray(targetConfig.actions) && targetConfig.actions.length > 0) {
    return targetConfig.actions.map((item, idx) => {
      if (!item || typeof item !== 'object') {
        throw new Error(`target.actions[${idx}] must be an object with action and params`)
      }
      return {
        action: item.action,
        params: item.params != null && typeof item.params === 'object' ? item.params : {}
      }
    })
  }
  if (!targetConfig.action) return []
  return [{
    action: targetConfig.action,
    params: targetConfig.params != null && typeof targetConfig.params === 'object' ? targetConfig.params : {}
  }]
}

// Dispatch one logical action into the underlying SDK call (params-only).
function callWsAction (client, action, params) {
  const p = params || {}

  const handlers = {
    connectOnly: () => { /* only connect, do nothing */ },
    ping: () => client.ping(),
    sendRaw: () => {
      const raw = p.method != null ? p : (p.data || p)
      if (typeof raw !== 'object') throw new Error('sendRaw requires params to be a full JSON object')
      return client.send(raw)
    },
    subTicker: () => client.subTicker(p),
    unsubTicker: () => client.unsubTicker(p),
    subTickers: () => client.subTickers(p),
    unsubTickers: () => client.unsubTickers(p),
    subDeal: () => client.subDeal(p),
    unsubDeal: () => client.unsubDeal(p),
    subDepth: () => client.subDepth(p),
    unsubDepth: () => client.unsubDepth(p),
    subDepthFull: () => client.subDepthFull(p),
    unsubDepthFull: () => client.unsubDepthFull(p),
    subKline: () => client.subKline(p),
    unsubKline: () => client.unsubKline(p),
    subFundingRate: () => client.subFundingRate(p),
    unsubFundingRate: () => client.unsubFundingRate(p),
    subIndexPrice: () => client.subIndexPrice(p),
    unsubIndexPrice: () => client.unsubIndexPrice(p),
    subFairPrice: () => client.subFairPrice(p),
    unsubFairPrice: () => client.unsubFairPrice(p),
    subContract: () => client.subContract(p),
    unsubContract: () => client.unsubContract(p),
    subEventContract: () => client.subEventContract(p),
    unsubEventContract: () => client.unsubEventContract(p),

    // Private login and filters
    login: () => client.login(p.subscribe != null ? { subscribe: p.subscribe } : {}),
    filterAssets: () => client.filterAssets(),
    filterOrders: () => client.filterOrders(p),
    filterOrderDeals: () => client.filterOrderDeals(p),
    filterPositions: () => client.filterPositions(p),
    resetPersonalFilters: () => client.resetPersonalFilters(),

    // Extended personal.filter helpers (implemented via raw send)
    filterPlanOrders: () => {
      const f = makeSingleFilter('plan.order', p)
      return client.send(buildPersonalFilterPayload([f]))
    },
    filterStopOrders: () => {
      const f = makeSingleFilter('stop.order', p)
      return client.send(buildPersonalFilterPayload([f]))
    },
    filterStopPlanOrders: () => {
      const f = makeSingleFilter('stop.planorder', p)
      return client.send(buildPersonalFilterPayload([f]))
    },
    filterRiskLimit: () => {
      const f = makeSingleFilter('risk.limit', p)
      return client.send(buildPersonalFilterPayload([f]))
    },
    filterAdlLevel: () => {
      const f = makeSingleFilter('adl.level', p)
      return client.send(buildPersonalFilterPayload([f]))
    },
    filterCustom: () => {
      const filtersInput = p.filters
      if (!Array.isArray(filtersInput)) {
        throw new Error('filterCustom requires params.filters to be an array')
      }
      const allowed = getAllowedPersonalFilterKeys()
      const filters = filtersInput.map((f, idx) => {
        if (!f || typeof f !== 'object') {
          throw new Error(`filters[${idx}] must be an object`)
        }
        const key = f.filter
        if (typeof key !== 'string') {
          throw new Error(`filters[${idx}].filter must be a string`)
        }
        validatePersonalFilterKey(key)
        const out = { filter: key }
        if (f.rules !== undefined) {
          const rules = validateSymbolsArray(f.rules)
          if (rules && rules.length) out.rules = rules
        }
        return out
      })
      // Extra defensive check for unexpected keys (should not be needed if we validated above)
      const invalid = filters.find(f => !allowed.includes(f.filter))
      if (invalid) {
        throw new Error(
          `Invalid personal.filter key in filters: ${invalid.filter}. Allowed keys: ${allowed.join(', ')}`
        )
      }
      return client.send(buildPersonalFilterPayload(filters))
    }
  }

  const fn = handlers[action]
  if (!fn) {
    const all = getAvailableActions()
    const similar = findSimilarActions(action)
    let msg = `Unknown action: ${action}\nAvailable actions:\n  ${all.join(', ')}`
    if (similar.length) {
      msg += `\nDid you mean: ${similar.join(', ')}`
    }
    throw new Error(msg)
  }
  return fn()
}

// Execute the next action in the queue. For login actions with following steps,
// wait for login success (handled in message handler) before moving to the next.
function executeNextQueuedAction (client, state) {
  const { queue } = state
  if (state.index >= queue.length) {
    if (!state.doneLogged) {
      console.log('[queue] all queued actions sent')
      state.doneLogged = true
    }
    return
  }

  const item = queue[state.index]
  console.log(`[queue] executing #${state.index + 1}: ${item.action} ${JSON.stringify(item.params)}`)

  if (item.action === 'login' && state.index < queue.length - 1) {
    const subscribe = item.params && item.params.subscribe != null ? !!item.params.subscribe : false
    console.log('[queue] waiting for login success before next action...')
    client.login({ subscribe })
    state.waitingLogin = true
    return
  }

  callWsAction(client, item.action, item.params)
  state.index += 1
  // Immediately proceed to next non-login actions in the queue.
  executeNextQueuedAction(client, state)
}

// Main flow: connect once, execute one or multiple actions, keep printing messages until Ctrl+C.
const apiKey = process.env.MEXC_API_KEY || ''
const apiSecret = process.env.MEXC_API_SECRET || ''

const client = new MexcFuturesWsClient({ apiKey, apiSecret })

let messageCount = 0

async function main () {
  const queue = normalizeActionQueue(target)
  const isQueueMode = Array.isArray(target.actions) && target.actions.length > 0

  if (queue.length === 0) {
    console.error('No action specified in target. Use target.action or target.actions[].action.')
    console.log('Available actions:', getAvailableActions().join(', '))
    process.exit(1)
  }

  const first = queue[0]
  const action = first.action
  const params = first.params

  const combinedFilterAction = !isQueueMode ? getCombinedLoginFilterAction(action) : null
  const isCombined = !!combinedFilterAction
  let combinedFilterSent = false

  const all = getAvailableActions()

  // Validate all actions in the queue.
  for (let i = 0; i < queue.length; i++) {
    const a = queue[i].action
    const similar = findSimilarActions(a)
    if (!all.includes(a)) {
      console.error(`Unknown action in queue at index ${i}: ${a}`)
      console.log('Available actions:')
      all.forEach(x => console.log('  -', x))
      if (similar.length) console.log('Did you mean:', similar.join(', '))
      process.exit(1)
    }
  }

  const isPrivateQueue = queue.some(item => WS_ACTIONS.private.includes(item.action))
  if (isPrivateQueue && (!apiKey || !apiSecret)) {
    console.warn('WARN: private actions require MEXC_API_KEY and MEXC_API_SECRET in contract/.env')
  }

  if (isQueueMode) {
    console.log('[queue] mode enabled, total actions:', queue.length)
    console.log('queue:', JSON.stringify(queue))
  } else {
    console.log('action:', action)
    console.log('params:', JSON.stringify(params))
  }
  console.log('---')

  const queueState = {
    queue,
    index: 0,
    waitingLogin: false,
    doneLogged: false
  }

  client.on('open', () => {
    console.log('[open] WebSocket connected')
    try {
      if (isQueueMode) {
        executeNextQueuedAction(client, queueState)
        return
      }

      if (isCombined) {
        const subscribe = params.subscribe != null ? !!params.subscribe : false
        console.log('[loginThenFilter] sending login with subscribe =', subscribe)
        client.login({ subscribe })
        return
      }
      if (action !== 'connectOnly') {
        callWsAction(client, action, params)
      }
    } catch (err) {
      console.error('Failed to execute action:', err.message)
    }
  })

  client.on('message', (msg) => {
    messageCount++
    console.log(`[message #${messageCount}]`, JSON.stringify(msg))

    if (isQueueMode && queueState.waitingLogin && isLoginSuccessMessage(msg)) {
      queueState.waitingLogin = false
      queueState.index += 1
      console.log('[queue] login success detected, continuing to next action...')
      executeNextQueuedAction(client, queueState)
      return
    }

    if (!isQueueMode && isCombined && !combinedFilterSent && isLoginSuccessMessage(msg)) {
      combinedFilterSent = true
      try {
        console.log('[loginThenFilter] login succeeded, sending filter action:', combinedFilterAction)
        callWsAction(client, combinedFilterAction, params)
      } catch (err) {
        console.error('Failed to execute combined filter action:', err.message)
      }
    }
  })

  client.on('pong', (data) => {
    messageCount++
    console.log('[pong]', data)
  })

  client.on('error', (err) => {
    console.error('[error]', err.message)
  })

  client.on('close', (code, reason) => {
    console.log('[close]', code, reason?.toString?.() || '')
  })

  client.on('reconnect', () => {
    console.log('[reconnect] reconnecting...')
  })

  client.connect()
}

main().catch(err => {
  console.error('err:', err.message)
  process.exit(1)
})

// Graceful shutdown on Ctrl+C: disconnect and print total message count.
process.on('SIGINT', () => {
  console.log('\n[signal] SIGINT received, closing WebSocket...')
  console.log('total messages:', messageCount)
  client.disconnect()
  setTimeout(() => process.exit(0), 500)
})
