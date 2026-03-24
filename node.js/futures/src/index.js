/**
 * MEXC Futures SDK
 * CommonJS entry
 */

const { sign, filterParams, buildQueryStringForSign, buildJsonStringForSign } = require('../signer')
const {
  request,
  publicRequest,
  privateRequest,
  DEFAULT_BASE_URL,
  DEFAULT_RECV_WINDOW,
  paramsToQueryString
} = require('../httpClient')
const { MexcFuturesRestClient } = require('./futuresRestClient')
const { MexcFuturesWsClient, DEFAULT_WS_URL } = require('./futuresWsClient')
const ENDPOINTS = require('./endpoints')

module.exports = {
  MexcFuturesRestClient,
  MexcFuturesWsClient,
  DEFAULT_WS_URL,
  signer: {
    sign,
    filterParams,
    buildQueryStringForSign,
    buildJsonStringForSign
  },
  httpClient: {
    request,
    publicRequest,
    privateRequest,
    DEFAULT_BASE_URL,
    DEFAULT_RECV_WINDOW,
    paramsToQueryString
  },
  ENDPOINTS,
  sign,
  request,
  publicRequest,
  privateRequest,
  filterParams,
  DEFAULT_BASE_URL,
  DEFAULT_RECV_WINDOW
}
