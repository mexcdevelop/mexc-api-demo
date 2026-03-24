const CryptoJS = require('crypto-js')
const HmacSHA256 = require('crypto-js/hmac-sha256')
const { removeEmptyValue, buildQueryString, createRequest, defaultLogger } = require('./helpers/utils')

class APIBase {
  constructor(options) {
    const { apiKey, apiSecret, baseURL, logger } = options
    this.apiKey = apiKey
    this.apiSecret = apiSecret
    this.baseURL = baseURL
    this.logger = logger || defaultLogger
  }
  
  publicRequest(method, path, params = {}) {
    params = removeEmptyValue(params)
    params = buildQueryString(params)
    if (params !== '') {
      path = `${path}?${params}`
    }
    return createRequest({
      method: method,
      baseURL: this.baseURL,
      url: path,
      apiKey: this.apiKey
    })
  }

  signRequest(method, path, params = {}) {
    params = removeEmptyValue(params)
    const timestamp = Date.now()
    let queryString = buildQueryString({ ...params, timestamp })
    queryString = queryString.replace(/\(/g, '%28').replace(/\)/g, '%29');
    const signature = CryptoJS.enc.Hex.stringify(CryptoJS.HmacSHA256(queryString, this.apiSecret))
    return createRequest({
      method: method,
      baseURL: this.baseURL,
      url: `${path}?${queryString}&signature=${signature}`,
      apiKey: this.apiKey
    })
  } 
}

module.exports = APIBase