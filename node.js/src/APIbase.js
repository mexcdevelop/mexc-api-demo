const crypto = require('crypto')
const { removeEmptyValue, buildQueryString, createRequest, CreateRequest, defaultLogger } = require('./helpers/utils')

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
      url: `${path}`,
      apiKey: this.apiKey
    })
  }



  //v3
  signRequest (method, path, params = {}) {
    params = removeEmptyValue(params)
    const timestamp = Date.now()
    const queryString = buildQueryString({ ...params, timestamp })
    const signature = crypto
      .createHmac('sha256', this.apiSecret)
      .update(queryString)
      .digest('hex')

    return createRequest({
      method: method,
      baseURL: this.baseURL,
      url: `${path}?${queryString}&signature=${signature}`,
      apiKey: this.apiKey
    })
  }

        //v2
        SignRequest (method, path, params = {}) {
          params = removeEmptyValue(params)
          const timestamp = Date.now()
          const apiKey = this.apiKey
          let objectString = apiKey + timestamp
         
          if (method === 'POST'){
            path = `${path}`
           // objectString = JSON.stringify(objectString)
            objectString += JSON.stringify(params)
            //objectString = JSON.stringify(objectString)
            console.log("参数类型:",typeof(objectString))
            console.log("参数:",objectString)
          } else{ 
            let queryString = buildQueryString({ ...params })      
            path = `${path}?${queryString}`
            objectString += queryString
          }
          const Signature = crypto
            .createHmac('sha256', this.apiSecret)
            .update(objectString)
            .digest('hex')
            console.log("签名:",Signature)
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