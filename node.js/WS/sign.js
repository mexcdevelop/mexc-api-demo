//签名 sign
const crypto = require('crypto')

function sign(REQ_TIME,API_KEY,SECRET_KEY,OP){
    let param = new Map();
    param.set('api_key',API_KEY);
    param.set('req_time',REQ_TIME);
    param.set('op',OP)
    param.set('sign',build_mexc_sign(param,SECRET_KEY));
    return param.get('sign');
  }
  
  function build_mexc_sign(paramMap,secret_key){
    const hash = crypto.createHash('md5');
    let keys = paramMap.keys();
    let keyArr = new Array();
    for (const key of keys) {
        keyArr.push(key);
    }
    let sortedKeys = keyArr.sort();
    let signStr = "";
    for (const key of sortedKeys) {
        signStr += key+"="+paramMap.get(key)+"&";
    }
    signStr += "api_secret="+secret_key;
    hash.update(signStr,'utf-8');
    return hash.digest('hex');
  }