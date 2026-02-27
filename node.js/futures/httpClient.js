/**
 * MEXC Futures HTTP client.
 * Uses Futures OpenAPI signature rules.
 */

const axios = require('axios')
const { sign, filterParams } = require('./signer')

const DEFAULT_BASE_URL = 'https://api.mexc.com'
const DEFAULT_RECV_WINDOW = 10000

/**
 * Convert params to URL query string (for GET/DELETE).
 * Raw key=value used for signing (no URL encode); actual request URL is encoded.
 */
function paramsToQueryString (params) {
  const filtered = filterParams(params)
  const keys = Object.keys(filtered).sort()
  return keys
    .map(k => `${encodeURIComponent(k)}=${encodeURIComponent(String(filtered[k]))}`)
    .join('&')
}

/**
 * Send request.
 * @param {Object} options
 * @param {string} options.baseURL
 * @param {string} options.method - GET | POST | DELETE
 * @param {string} options.path - Path, e.g. /api/v1/contract/ping
 * @param {Object} options.params - Business params
 * @param {string} [options.apiKey]
 * @param {string} [options.apiSecret]
 * @param {number} [options.recvWindow] - Optional, milliseconds
 * @param {number} [options.timeout] - Optional, request timeout milliseconds
 * @param {boolean} [options.unwrapData=false] - If true, return only data field on success; failure still returns full body
 * @param {Object} [options.headers] - Extra headers, merged with default (e.g. timezone-login)
 * @param {Object} [options.logger]
 * @returns {Promise}
 */
function request (options) {
  const {
    baseURL = DEFAULT_BASE_URL,
    method = 'GET',
    path,
    params = {},
    apiKey,
    apiSecret,
    recvWindow = DEFAULT_RECV_WINDOW,
    timeout,
    unwrapData = false,
    headers: extraHeaders,
    logger
  } = options

  const isPrivate = !!(apiKey && apiSecret)
  const requestTime = String(Date.now())

  let url = path
  let body = null
  const headers = {
    'Content-Type': 'application/json'
  }
  if (extraHeaders && typeof extraHeaders === 'object') {
    Object.assign(headers, extraHeaders)
  }

  if (method === 'GET' || method === 'DELETE') {
    const qs = paramsToQueryString(params)
    if (qs) {
      url = `${path}${path.includes('?') ? '&' : '?'}${qs}`
    }
  } else if (method === 'POST') {
    body = Array.isArray(params) ? params : filterParams(params)
  }

  if (isPrivate) {
    const signature = sign({
      accessKey: apiKey,
      apiSecret,
      requestTime,
      method,
      params: method === 'POST' ? body : params
    })

    headers['ApiKey'] = apiKey
    headers['Request-Time'] = requestTime
    headers['Signature'] = signature
    if (recvWindow != null) {
      headers['Recv-Window'] = String(recvWindow)
    }
  }

  const config = {
    baseURL,
    method,
    url,
    headers,
    data: body,
    validateStatus: () => true
  }
  if (timeout != null && timeout > 0) {
    config.timeout = timeout
  }

  if (logger && typeof logger.debug === 'function') {
    logger.debug('[Futures HTTP]', method, url, body ? '(body)' : '')
  }

  return axios(config).then(res => {
    const body = res.data
    if (logger && res.status >= 400 && typeof logger.error === 'function') {
      logger.error('[Futures HTTP]', res.status, body)
    }
    if (unwrapData && body && body.success === true && 'data' in body) {
      return body.data
    }
    return body != null ? body : res.data
  })
}

/**
 * Public request (no signature).
 */
function publicRequest (method, path, params = {}, baseURL = DEFAULT_BASE_URL, logger) {
  return request({
    baseURL,
    method,
    path,
    params,
    logger
  })
}

/**
 * Private request (with signature).
 */
function privateRequest (method, path, params = {}, options = {}) {
  const { apiKey, apiSecret, baseURL = DEFAULT_BASE_URL, recvWindow, timeout, logger } = options
  return request({
    baseURL,
    method,
    path,
    params,
    apiKey,
    apiSecret,
    recvWindow,
    timeout,
    logger
  })
}

module.exports = {
  request,
  publicRequest,
  privateRequest,
  DEFAULT_BASE_URL,
  DEFAULT_RECV_WINDOW,
  paramsToQueryString
}
