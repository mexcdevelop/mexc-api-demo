const axios = require('axios')
const { Console, timeStamp } = require('console')
const { encode } = require('punycode')

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

const stringifyKeyValuePair = ([key, value]) => {
  let valueString;
  if (typeof value === 'object') {
    valueString = JSON.stringify(value);
  } else {
    valueString = value;
  }
  return `${key}=${encodeURIComponent(valueString)}`;
};

const buildQueryString = params => {
  if (!params) return '';
  return Object.entries(params)
    .map(stringifyKeyValuePair)
    .join('&');
};

const CreateRequest = (config) => {
  const { baseURL, method, url, params, apiKey, timestamp, Signature } = config
  if (method === 'GET' || method === 'DELETE') {
    return getRequestInstance({
      baseURL,
      headers: {
        'Content-Type': 'application/json',
        'ApiKey': apiKey,
        'Request-Time': timestamp,
        'Signature': Signature
      },
    }).request({
      method,
      url,
      params
    })
  }
  if (method === 'POST') {
    return getRequestInstance({
      baseURL,
      headers: {
        'Content-Type': 'application/json',
        'ApiKey': apiKey,
        'Request-Time': timestamp,
        'Signature': Signature
      },
    }).request({
      method,
      url,
      data: params
    })
  }
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


const pubRequest = (config) => {
  const { apiKey, method, url } = config
  return getRequestInstance({
    baseURL: 'https://www.mexc.com',
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
  pubRequest,
  defaultLogger
}