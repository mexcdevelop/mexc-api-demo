import json
import websocket
import threading, time
import PushDataV3ApiWrapper_pb2
import logging

logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

BASE_URL = 'wss://wbs-api.mexc.com/ws'

def on_message(ws, message):
    """
    Processing received messages
    Args:
        ws: WebSocket
        message: Received messages
    """
    try:
        if isinstance(message, str):
            try:
                json_data = json.loads(message)
                logger.info(f"Received JSON: {json_data}")
                return
            except json.JSONDecodeError:
                # Not JSON, continue processing as Protobuf
                message = message.encode('utf-8')
        # Deserialize the message
        result = PushDataV3ApiWrapper_pb2.PushDataV3ApiWrapper()
        result.ParseFromString(message)
        logger.info(f"Successfully parsed Protobuf message: {result}")

    except Exception as e:
        logger.error(f"Error parsing message: {str(e)}")
        logger.error(f"Error type: {type(e)}")
        if isinstance(message, bytes):
            logger.info(f"Binary message first 100 bytes: {message[:100].hex()}")
        elif isinstance(message, str):
            logger.info(f"String message first 100 chars: {message[:100]}")
        import traceback
        logger.error(f"Stack trace: {traceback.format_exc()}")


def on_error(ws, error):
    """Error handling"""
    logger.error(f"Error: {error}")


def on_close(ws, close_status_code, close_msg):
    """Handle connection closure"""
    logger.info(f"WebSocket connection closed. Status code: {close_status_code}, Message: {close_msg}")


def on_open(ws):
    """Handle connection opening"""
    logger.info("WebSocket connection opened")
    try:
        # Send subscription request using JSON format
        subscribe_message = {
            "method": "SUBSCRIPTION",
            "params": [
                "spot@public.aggre.deals.v3.api.pb@10ms@BTCUSDT",
                "spot@public.aggre.deals.v3.api.pb@10ms@ETHUSDT",
                # "spot@public.aggre.depth.v3.api.pb@100ms@ETHUSDT"
                # "spot@public.aggre.bookTicker.v3.api.pb@100ms@BTCUSDT"
                # "spot@public.deals.v3.api.pb@BTCUSDT"
                # "spot@public.kline.v3.api.pb@BTCUSDT@Min15",
                # "spot@public.aggre.depth.v3.api.pb@100ms@PIUSDT",
                # "spot@public.increase.depth.batch.v3.api.pb@BTCUSDT",
                # "spot@public.limit.depth.v3.api.pb@BTCUSDT@5",
                # "spot@public.aggre.bookTicker.v3.api.pb@100ms@BTCUSDT",
                # "spot@public.bookTicker.batch.v3.api.pb@BTCUSDT",
                # "spot@private.account.v3.api.pb",
                # "spot@private.deals.v3.api.pb",
                # "spot@private.orders.v3.api.pb"
            ]
        }
        ws.send(json.dumps(subscribe_message))
        logger.info(f"Sent subscription message: {subscribe_message}")
    except Exception as e:
        logger.error(f"Error in on_open: {str(e)}")

    def send_ping():
        while True:
            time.sleep(30)
            try:
                ws.send(json.dumps({"method": "ping"}))
            except Exception as e:
                print(f"Error sending ping: {e}")
                break

    threading.Thread(target=send_ping, daemon=True).start()


if __name__ == "__main__":
    while True:
        try:
            websocket.enableTrace(False)
            ws = websocket.WebSocketApp(BASE_URL,
                                        on_message=on_message,
                                        on_error=on_error,
                                        on_close=on_close)
            ws.on_open = on_open
            ws.run_forever(ping_interval=30, ping_timeout=10, sslopt={"cert_reqs": 0})
        except Exception as e:
            logger.error(f"Main loop error: {str(e)}")
        logger.info("Reconnecting in 5 seconds...")
        time.sleep(5)