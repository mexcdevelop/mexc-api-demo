const axios = require('axios')
const { Console, timeStamp } = require('console')

const removeEmptyValue = obj => {
  if (!(obj instanceof Object)) return {}
  Object.keys(obj).forEach(key => isEmptyValue(obj[key]) && delete obj[key])
  return obj
}

const isEmptyValue = input => {
  return (!input && input !== false && input !== 0) ||
    ((typeof input === 'string' || input instanceof String) && /^\s+$/.test(input)) ||
    (input instanceof Object && !Object.keys(input).length) ||
    (Array.isArray(input) && !input.length)
}

const buildQueryString = params => {
  if (!params) return ''
  return Object.entries(params)
    .map(stringifyKeyValuePair)
    .join('&')
}




const CreateRequest = (config) => {
  const { baseURL, method, url, apiKey, timestamp, Signature} = config
  return getRequestInstance({
    baseURL, 
    headers: {
      'Content-Type': 'application/json',
      'ApiKey': apiKey,
      'Request-Time':timestamp,
      'Signature': Signature
    },  
  }).request({
    method,
    url
  })
}


const stringifyKeyValuePair = ([key, value]) => {
  const valueString = Array.isArray(value) ? `["${value.join('","')}"]` : value
  return `${key}=${encodeURIComponent(valueString)}`
}

const getRequestInstance = (config) => {
  return axios.create({
    ...config
  })
}

const createRequest = (config) => {
  const { baseURL, apiKey, method, url } = config
  return getRequestInstance({
    baseURL,
    headers: {
      'Content-Type': 'application/json',
      'X-MEXC-APIKEY': apiKey,

    }
  }).request({
    method,
    url
  })
}




const flowRight = (...functions) => input => functions.reduceRight(
  (input, fn) => fn(input),
  input
)

const defaultLogger = new Console({
  stdout: process.stdout,
  stderr: process.stderr
})




module.exports = {
  isEmptyValue,
  removeEmptyValue,
  buildQueryString,
  createRequest,
  flowRight,
  CreateRequest,
  defaultLogger
}