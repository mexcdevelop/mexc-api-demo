const moment = require('moment');
const WebSocket = require('ws');
const pako = require('pako');
const crypto = require('crypto')


const WS_URL = 'wss://wbs.mexc.com/raw/ws';


var ws = new WebSocket(WS_URL);

ws.onmessage = e => {
    console.log(e.data);
};

ws.onopen = () => {
    console.log('Connection open');
    ws.send('ping');
    subPersonal()
}

ws.onclose = () => {
    console.log('close');
}

/**
 * infor
 * @param ws
 */
function subInfo(symbol) {
    var data = {
        'op': "sub.symbol",
        'symbol': symbol
    }
    ws.send(JSON.stringify(data));
}





/**
 * kline
 * @param ws
 */
function subKline(symbol) {
    var data ={
        'op':"sub.kline",
        'symbol':symbol,
        'interval':"Min30"
    }
    ws.send(JSON.stringify(data));

}

/**
 * depth
 * @param ws
 */
 function subDepth(symbol) {
    var data ={
        'op':"sub.limit.depth",

        'symbol':symbol,   //交易对
      
        "depth": 5
    }

    ws.send(JSON.stringify(data));

}

/**
 * overview
 * @param ws
 */
 function subOverview() {
    var data ={
        "op": "sub.overview"
    }

    ws.send(JSON.stringify(data));

}

/**
 * cny
 * @param ws
 */
 function subCny() {
    var data ={
        "op": "sub.cny"
    }

    ws.send(JSON.stringify(data));

}

/**
 * Subscribe to incremental depth
 * @param ws
 */
 function Subdepth(symbol) {
    var data ={
        "op":"sub.depth",
        "symbol": symbol
    }

    ws.send(JSON.stringify(data));

}

/**
 * Get account order status push
 * @param ws
 */
 function subPersonal(){
    var data ={
        'op':"sub.personal",  // sub key

        'api_key': "API Key",	//API Key
      
        'sign': sign,
      
        'req_time': "timestamp "	//当前时间的时间戳 current timestamp 
    }
    ws.send(JSON.stringify(data));
}

/**
 * deals
 * @param ws
 */
 function subDeals(){
    var data ={
        'op':"sub.personal.deals",  // sub key

        'api_key': "API Key",	//API Key
      
        'sign': sign,
      
        'req_time': 'timestamp '	//当前时间的时间戳 current timestamp 
    }
    ws.send(JSON.stringify(data));
}


//签名 sign
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

