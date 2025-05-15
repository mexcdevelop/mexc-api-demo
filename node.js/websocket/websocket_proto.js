const WebSocket = require('ws');
const protobuf = require('protobufjs');


const protoPath = './PushDataV3ApiWrapper.proto';
let PushDataV3ApiWrapper;

protobuf.load(protoPath, (err, root) => {
  if (err) throw err;
  PushDataV3ApiWrapper = root.lookupType('PushDataV3ApiWrapper');
  console.log(' Protobuf definition loaded');
  createWebSocket();
});

const BASE_URL = 'wss://wbs-api.mexc.co/ws';

const subscribeMessage = {
  method: 'SUBSCRIPTION',
  params: [
    "spot@public.aggre.deals.v3.api.pb@10ms@BTCUSDT",
    // "spot@public.kline.v3.api.pb@BTCUSDT@Min15",
    // "spot@public.aggre.depth.v3.api.pb@100ms@ETHUSDT",
    // "spot@public.increase.depth.batch.v3.api.pb@BTCUSDT",
    // "spot@public.limit.depth.v3.api.pb@BTCUSDT@5",
    // "spot@public.aggre.bookTicker.v3.api.pb@100ms@BTCUSDT",
    // "spot@public.bookTicker.batch.v3.api.pb@BTCUSDT",
    // "spot@private.account.v3.api.pb",
    // "spot@private.deals.v3.api.pb",
    // "spot@private.orders.v3.api.pb"
  ]
};

function createWebSocket() {
  const ws = new WebSocket(BASE_URL);

  ws.on('open', () => {
    console.info('WebSocket connection opened');
    ws.send(JSON.stringify(subscribeMessage));
    console.info('Sent subscription:', JSON.stringify(subscribeMessage));

    const pingInterval = setInterval(() => {
      if (ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({ method: 'ping' }));
        console.log('Sent ping');
      }
    }, 30000);

    ws.on('close', () => {
      clearInterval(pingInterval);
      console.warn('WebSocket closed, reconnecting in 5 seconds...');
      setTimeout(createWebSocket, 5000);
    });
  });

  ws.on('message', (data) => {
    const isProbablyJson = () => {
      try {
        const str = data.toString();
        return str.startsWith('{') || str.startsWith('[');
      } catch (err) {
        return false;
      }
    };

    if (isProbablyJson()) {
      try {
        const json = JSON.parse(data.toString());
        console.log('Received JSON:', JSON.stringify(json, null, 2));
        return;
      } catch (err) {
        console.error('Failed to parse JSON:', err.message);
      }
    }

    try {
      if (!PushDataV3ApiWrapper) {
        console.error('Protobuf definition not loaded.');
        return;
      }

      const message = PushDataV3ApiWrapper.decode(data);
      const obj = PushDataV3ApiWrapper.toObject(message, { enums: String, longs: String });
      console.log('Parsed Protobuf Message:', JSON.stringify(obj, null, 2));
    } catch (err) {
      console.error('Failed to parse Protobuf:', err.message);
      console.error('Hex preview:', data.toString('hex').slice(0, 200));
    }
  });

  ws.on('error', (err) => {
    console.error('WebSocket error:', err.message);
  });

  ws.on('close', (code, reason) => {
    console.warn(`Connection closed: code=${code}, reason=${reason}`);
  });
}