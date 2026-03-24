/**
 * MEXC Futures OpenAPI signer.
 * Note: Uses Futures signature rules, not Spot v3.
 */

const crypto = require('crypto')

/**
 * Filter null/undefined params.
 * @param {Object} params
 * @returns {Object}
 */
function filterParams (params) {
  if (!params || typeof params !== 'object') return {}
  const filtered = {}
  for (const [key, value] of Object.entries(params)) {
    if (value !== null && value !== undefined) {
      filtered[key] = value
    }
  }
  return filtered
}

/**
 * GET/DELETE: params sorted by key; key=value joined with &.
 * Raw values for signing (no URL encode).
 * @param {Object} params
 * @returns {string}
 */
function buildQueryStringForSign (params) {
  const filtered = filterParams(params)
  const keys = Object.keys(filtered).sort()
  return keys
    .map(k => `${k}=${filtered[k]}`)
    .join('&')
}

/**
 * POST: Use JSON string (no sorting). Array body is stringified directly.
 * @param {Object|Array} params
 * @returns {string}
 */
function buildJsonStringForSign (params) {
  if (Array.isArray(params)) return JSON.stringify(params)
  const filtered = filterParams(params)
  return JSON.stringify(filtered)
}

/**
 * Generate MEXC Futures signature.
 * @param {Object} options
 * @param {string} options.accessKey
 * @param {string} options.apiSecret
 * @param {string|number} options.requestTime - Millisecond timestamp
 * @param {string} options.method - GET | POST | DELETE
 * @param {Object} options.params - Business params (excluding path params)
 * @returns {string} Hex lowercase signature
 */
function sign (options) {
  const { accessKey, apiSecret, requestTime, method, params = {} } = options
  const reqTime = String(requestTime)
  let parameterString

  if (method === 'GET' || method === 'DELETE') {
    parameterString = buildQueryStringForSign(params)
  } else if (method === 'POST') {
    parameterString = buildJsonStringForSign(params)
  } else {
    parameterString = ''
  }

  const stringToSign = accessKey + reqTime + parameterString
  const hmac = crypto.createHmac('sha256', apiSecret)
  hmac.update(stringToSign)
  return hmac.digest('hex')
}

module.exports = {
  filterParams,
  buildQueryStringForSign,
  buildJsonStringForSign,
  sign
}
