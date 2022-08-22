//签名 sign
const CryptoJS = require('crypto-js')

function sign(API_KEY,OP,REQ_TIME,SECRET_KEY){
    let param = new Map();
    param.set('api_key',API_KEY);
    param.set('op',OP)
    param.set('req_time',REQ_TIME);
    param.set('sign',build_mexc_sign(param,SECRET_KEY));
    return param.get('sign');
  }
  
  function build_mexc_sign(paramMap,secret_key){
    let keys = paramMap.keys();
    let keyArr = new Array();
    for (const key of keys) {
    keyArr.push(key);
    }
    let signStr = "";
    for (const key of keyArr) {
    signStr += `${key}=${paramMap.get(key)}&`;
    }
    signStr += `api_secret=${secret_key}`;
    const signature = CryptoJS.enc.Hex.stringify(CryptoJS.MD5(signStr))  
    return signature;
    }