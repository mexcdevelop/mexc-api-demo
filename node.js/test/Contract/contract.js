const axios = require('axios');
const crypto = require('crypto');


const BASE_URL = 'https://contract.mexc.com'; 
const API_KEY = 'your apikey';  
const SECRET_KEY = 'your secret key';  


function _getServerTime() {
    return Date.now();
}


function _signRequest(apiKey, secretKey, timestamp, params = '') {
    const message = apiKey + timestamp + params;
    return crypto.createHmac('sha256', secretKey)
                 .update(message)
                 .digest('hex');
}


function buildQueryString(params) {
    return Object.keys(params)
        .sort() 
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&');
}


async function sendApiRequest(config) {
    const { method, path, params = {}, isPrivate = false, recvWindow } = config;
    const url = `${BASE_URL}${path}`;
    const timestamp = _getServerTime();
    let queryString = '';
    let signature = '';
    let headers = {
        'Content-Type': 'application/json',
    };

    if (isPrivate) {

        if (method === 'GET' || method === 'DELETE') {
            queryString = buildQueryString(params);
            signature = _signRequest(API_KEY, SECRET_KEY, timestamp, queryString);
        } else if (method === 'POST') {
            const jsonParams = JSON.stringify(params); 
            signature = _signRequest(API_KEY, SECRET_KEY, timestamp, jsonParams);
        }


        headers = {
            ...headers,
            'ApiKey': API_KEY,
            'Request-Time': timestamp.toString(),
            'Signature': signature,
        };

        if (recvWindow) {
            headers['Recv-Window'] = recvWindow;
        }
    }

    try {
        let response;
        if (method === 'GET' || method === 'DELETE') {

            queryString = buildQueryString(params); 
            const fullUrl = queryString ? `${url}?${queryString}` : url;
            console.log('Full URL:', fullUrl);  
            response = await axios.request({ method, url: fullUrl, headers });
        } else if (method === 'POST') {
          
            response = await axios.post(url, params, { headers });
        }
        return response.data;
    } catch (error) {
        console.error('Error:', error.response ? error.response.data : error.message);
        return null;
    }
}

// Example: Send a public GET request
sendApiRequest({
    method: 'GET',
    path: '/api/v1/contract/depth/BTC_USDT',  // Replace with your public API path
    params: ''  // Replace with your query parameters
}).then(res => console.log(res.data));

// Example: Send a private GET request
sendApiRequest({
    method: 'GET',
    path: '/api/v1/private/position/leverage',  // Replace with your API path
    params: { symbol: 'BTC_USDT' }, // Replace with your query parameters
    isPrivate: true
}).then(res => console.log(res));

// Example: Send a private POST request
sendApiRequest({
    method: 'POST',
    path: '/api/v1/private/position/change_position_mode',  // Replace with your API path
    params: { "positionMode": 1 },  // Replace with your POST data
    isPrivate: true
}).then(res => console.log(res));