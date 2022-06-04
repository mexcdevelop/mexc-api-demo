const moment = require('moment');
const WebSocket = require('ws');
const pako = require('pako');
const CryptoJS = require ('crypto-js')


const WS_URL = 'wss://wbs.mexc.com/raw/ws';

// 修改您的accessKey 和 secretKey
const config = {
    API_KEY: "XXXXXXXX-XXXXXXXX-XXXXXXXX-XXXXX",
    SECRET_KEY: "XXXXXXXX-XXXXXXXX-XXXXXXXX-XXXXX",
    REQ_TIME: Date.now()
}


/**
 * 签名计算
 */
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


/**
 * privata
 * @param ws
 */
function privata(ws){
    var data ={
        'op':"sub.personal",  // sub key

        'api_key': "api_key",	//API Key
      
        'sign': sign,
      
        'req_time': "current timestamp "	//当前时间的时间戳 current timestamp 
    }
}

/**
 * infor
 * @param ws
 */
function infor(ws) {
    var data ={
        'op':"sub.symbol", 
        'symbol':"VDS_USDT"      

    }
    ws.send(JSON.stringify(data));

}

/**
 * kline
 * @param ws
 */
function kline(ws) {
    var data ={
        'op':"sub.kline",
        'symbol':"VDS_USDT",
        'interval':"Min30"
    }

    ws.send(JSON.stringify(data));

}



function init() {
    var ws = new WebSocket(WS_URL);
    ws.on('open', () => {
        console.log('open');
        kline(ws);
    });
    ws.on('message', (data) => {
        let text = pako.inflate(data, {
            to: 'string'
        });
        let msg = JSON.parse(text);
            console.log(msg)
        }

    );
    ws.on('close', () => {
        // websocket连接关闭处理
        console.log('close');
        init();
    });

    ws.on('error', err => {
        // websocket连接关闭处理
        console.log('error', err);
        init();
    });
}

init();

