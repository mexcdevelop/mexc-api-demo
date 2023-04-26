const moment = require('moment');
const WebSocket = require('ws');
const pako = require('pako');
const WS_URL = 'wss://wbs.mexc.com/ws';


var ws = new WebSocket(WS_URL);

ws.onopen = () => {
    console.log('Connection open');
    ws.send('{"method":"PING"}')
    Deals()
}

ws.onmessage = e => {
    console.log(e.data);
};

ws.onclose = () => {
    console.log('close');
}

/**
 * deals
 * @param ws
 */
function Deals() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@public.deals.v3.api@BTCUSDT"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * kline
 * @param ws
 */
 function Kline() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@public.kline.v3.api@BTCUSDT@Min15"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * increasedepth
 * @param ws
 */
 function IncreaseDepth() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@public.increase.depth.v3.api@BTCUSDT"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * limitdepth
 * @param ws
 */
 function LimitDepth() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@public.limit.depth.v3.api@BTCUSDT@5"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * bookTicker
 * @param ws
 */
 function BookTicker() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@public.bookTicker.v3.api@BTCUSDT"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * account
 * WS_URL = 'wss://wbs.mexc.com/ws?listenKey=Your listenkey'
 * @param ws
 */
 function Account() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@private.account.v3.api"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * accountdeals
 * WS_URL = 'wss://wbs.mexc.com/ws?listenKey=Your listenkey'
 * @param ws
 */
 function AccountDeals() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@private.deals.v3.api"]       
    }
    ws.send(JSON.stringify(data));
}

/**
 * orders
 * WS_URL = 'wss://wbs.mexc.com/ws?listenKey=Your listenkey'
 * @param ws
 */
 function Orders() {
    var data = {       
        "method": "SUBSCRIPTION",
        "params": ["spot@private.orders.v3.api"]       
    }
    ws.send(JSON.stringify(data));
}










