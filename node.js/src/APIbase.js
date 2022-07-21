const CryptoJS = require('crypto-js')
const HmacSHA256 = require('crypto-js/hmac-sha256')
const { removeEmptyValue, buildQueryString, createRequest, CreateRequest, pubRequest, defaultLogger } = require('./helpers/utils')

class APIBase {
  constructor (options) {
    const { apiKey, apiSecret, baseURL ,logger } = options
    this.apiKey = apiKey
    this.apiSecret = apiSecret
    this.baseURL = baseURL
    this.logger = logger || defaultLogger
  }

  publicRequest (method, path, params = {}) {
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



//V3
  signRequest (method, path, params = {}) {
    params = removeEmptyValue(params)
    const timestamp = Date.now()
    const queryString = buildQueryString({ ...params, timestamp })
    const signature = CryptoJS.enc.Hex.stringify(CryptoJS.HmacSHA256(queryString, this.apiSecret))
    return createRequest({
      method: method,
      baseURL: this.baseURL,
      url: `${path}?${queryString}&signature=${signature}`,
      apiKey: this.apiKey
    })
  }


    //V2
    PublicRequest (method, path, params = {}) {
      params = removeEmptyValue(params)
      params = buildQueryString(params)
      if (params !== '') {
        path = `${path}?${params}`
      }
      return pubRequest({
        method: method,
        baseURL: this.baseURL,
        url: path,
        apiKey: this.apiKey
      })
    }
  
        SignRequest (method, path, params = {}) {
          params = removeEmptyValue(params)
          const timestamp = Date.now()
          const apiKey = this.apiKey
          let objectString = apiKey + timestamp
         
          if (method === 'POST'){
            path = `${path}`
            objectString += JSON.stringify(params)  
          } else{ 
            let queryString = buildQueryString({ ...params })      
            path = `${path}?${queryString}`
            objectString += queryString
          }
            const Signature = CryptoJS.enc.Hex.stringify(CryptoJS.HmacSHA256(objectString, this.apiSecret))
            return CreateRequest({
              method: method,
              baseURL: this.baseURL,
              url: path,
              apiKey: this.apiKey,
              timestamp: timestamp,
              Signature: Signature,
              params:params              
            })
            
        }
}

module.exports = APIBase