"""One credential-free public-market check; never reads accounts or submits orders."""

import argparse
from datetime import datetime, timezone
import json
import math
from pathlib import Path
import time

from mexc_bot import Config, DonchianSignal, MexcClient


def build_snapshot(client, now_ms):
    closed = []
    previous_open = None
    for candle in client.klines(25):
        if not isinstance(candle, (list, tuple)) or len(candle) < 7:
            raise ValueError('Malformed candle')
        opened, ended = int(candle[0]), int(candle[6])
        if ended - opened != 60_000 or (
            previous_open is not None and opened - previous_open != 60_000
        ):
            raise ValueError('Expected consecutive one-minute candles')
        previous_open = opened
        if ended >= now_ms:
            continue
        high, low, close = (float(candle[index]) for index in (2, 3, 4))
        if not all(math.isfinite(value) and value > 0 for value in (high, low, close)):
            raise ValueError('Invalid candle prices')
        if not low <= close <= high:
            raise ValueError('Inconsistent candle prices')
        closed.append(candle)
    if len(closed) < 21:
        raise ValueError('At least 21 closed candles are required')
    last_close = int(closed[-1][6])
    if now_ms - last_close > 120_000:
        raise ValueError('Market candles are stale')
    price = float(client.price())
    if not math.isfinite(price) or price <= 0:
        raise ValueError('Invalid market price')
    signal = DonchianSignal(20, 10)
    return {
        'mode': 'public_signal_monitor',
        'symbol': 'BTCUSDT',
        'interval': '1m',
        'observed_at': datetime.fromtimestamp(now_ms / 1000, timezone.utc).isoformat(),
        'last_closed_candle_ms': last_close,
        'price': price,
        'signals': {
            'without_position': signal.evaluate(closed, False),
            'with_position': signal.evaluate(closed, True),
        },
        'position_state': 'unknown',
        'account_access': False,
        'live': False,
    }


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path, help='Write the public snapshot as JSON')
    args = parser.parse_args(argv)
    client = None
    try:
        cfg = Config(
            api_key='', api_secret='', base_url='https://api.mexc.com',
            live_trading=False, symbol='BTCUSDT', quote_asset='USDT',
            entry_window=20, exit_window=10,
        )
        client = MexcClient(cfg)
        snapshot = build_snapshot(client, int(time.time() * 1000))
        payload = json.dumps(snapshot, allow_nan=False, indent=2)
        if args.output:
            args.output.parent.mkdir(parents=True, exist_ok=True)
            args.output.write_text(payload + '\n', encoding='utf-8')
        print(payload)
        return 0
    except Exception as exc:
        error = {'mode': 'public_signal_monitor', 'live': False, 'error_type': type(exc).__name__}
        response = getattr(exc, 'response', None)
        if response is not None:
            error['http_status'] = response.status_code
        print(json.dumps(error))
        return 1
    finally:
        if client is not None:
            client.session.close()


if __name__ == '__main__':
    raise SystemExit(main())
