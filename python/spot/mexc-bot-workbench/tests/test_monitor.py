import contextlib
import io
import json
import math
import os
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

import requests

import monitor


NOW = 1_800_000_000_000


def candles(last_close=95):
    rows = []
    for index in range(21):
        end = NOW - (21 - index) * 60_000
        close = last_close if index == 20 else 95
        rows.append([end - 60_000, 95, max(100, close), min(90, close), close, 1, end])
    return rows


class PublicClient:
    def __init__(self, rows=None, price=101):
        self.rows = candles() if rows is None else rows
        self.current_price = price

    def klines(self, limit):
        return self.rows

    def price(self):
        return self.current_price


class MonitorTests(unittest.TestCase):
    def test_open_candle_is_excluded_from_entry_signal(self):
        rows = candles()
        rows.append([NOW - 60_000, 95, 1000, 90, 999, 1, NOW])
        result = monitor.build_snapshot(PublicClient(rows), NOW)
        self.assertEqual(result['signals']['without_position'], 'HOLD')
        self.assertEqual(result['last_closed_candle_ms'], NOW - 60_000)

    def test_breakout_and_exit_scenarios_do_not_assume_holdings(self):
        entry = monitor.build_snapshot(PublicClient(candles(110)), NOW)
        exit_result = monitor.build_snapshot(PublicClient(candles(80)), NOW)
        self.assertEqual(entry['signals']['without_position'], 'BUY')
        self.assertEqual(exit_result['signals']['with_position'], 'SELL')
        self.assertEqual(entry['position_state'], 'unknown')
        self.assertFalse(entry['account_access'])
        self.assertFalse(entry['live'])
        self.assertNotIn('order', entry)

    def test_insufficient_and_stale_candles_are_rejected(self):
        with self.assertRaises(ValueError):
            monitor.build_snapshot(PublicClient(candles()[:20]), NOW)
        with self.assertRaises(ValueError):
            monitor.build_snapshot(PublicClient(candles()), NOW + 5 * 60_000)

    def test_malformed_or_nonfinite_market_data_is_rejected(self):
        for price in (0, -1, math.nan, math.inf):
            with self.subTest(price=price), self.assertRaises(ValueError):
                monitor.build_snapshot(PublicClient(price=price), NOW)
        for bad in ([1, 2], [NOW-60_000, 95, math.nan, 90, 95, 1, NOW-1]):
            with self.subTest(candle=bad), self.assertRaises(ValueError):
                monitor.build_snapshot(PublicClient(candles() + [bad]), NOW)

    def test_duplicate_and_out_of_order_candles_are_rejected(self):
        for rows in (candles() + [candles()[-1]], list(reversed(candles()))):
            with self.assertRaises(ValueError):
                monitor.build_snapshot(PublicClient(rows), NOW)

    def test_missing_minutes_and_incorrect_duration_are_rejected(self):
        missing_minute = candles()
        for row in missing_minute[:10]:
            row[0] -= 60_000
            row[6] -= 60_000
        short_candle = candles()
        short_candle[-1][6] = short_candle[-1][0] + 2
        for name, rows in [('missing_minute', missing_minute), ('short_candle', short_candle)]:
            with self.subTest(case=name), self.assertRaises(ValueError):
                monitor.build_snapshot(PublicClient(rows), NOW)

    def test_monitor_ignores_live_credentials_and_custom_host(self):
        fake = PublicClient()
        fake.session = type('Session', (), {'close': lambda self: None})()
        with patch.dict(os.environ, {'MEXC_LIVE_TRADING': 'true', 'MEXC_API_KEY': 'unused-test-key', 'MEXC_API_SECRET': 'unused-test-secret', 'MEXC_BASE_URL': 'https://invalid.example'}):
            with patch('monitor.MexcClient', return_value=fake) as constructor:
                with patch('monitor.time.time', return_value=NOW / 1000), contextlib.redirect_stdout(io.StringIO()):
                    self.assertEqual(monitor.main([]), 0)
        cfg = constructor.call_args.args[0]
        self.assertEqual(cfg.api_key, '')
        self.assertEqual(cfg.api_secret, '')
        self.assertEqual(cfg.base_url, 'https://api.mexc.com')
        self.assertFalse(cfg.live_trading)

    def test_snapshot_is_written_as_json(self):
        fake = PublicClient()
        fake.session = type('Session', (), {'close': lambda self: None})()
        with tempfile.TemporaryDirectory() as folder:
            output = Path(folder) / 'results/latest.json'
            with patch('monitor.MexcClient', return_value=fake), patch('monitor.time.time', return_value=NOW / 1000):
                with contextlib.redirect_stdout(io.StringIO()) as stdout:
                    self.assertEqual(monitor.main(['--output', str(output)]), 0)
            self.assertEqual(json.loads(stdout.getvalue()), json.loads(output.read_text()))

    def test_request_errors_do_not_expose_urls_or_credentials(self):
        error = requests.HTTPError('https://api.mexc.com/?signature=sensitive-test-value')
        error.response = requests.Response()
        error.response.status_code = 403
        fake = PublicClient()
        fake.klines = lambda limit: (_ for _ in ()).throw(error)
        fake.session = type('Session', (), {'close': lambda self: None})()
        with patch('monitor.MexcClient', return_value=fake), contextlib.redirect_stdout(io.StringIO()) as stdout:
            self.assertEqual(monitor.main([]), 1)
        result = json.loads(stdout.getvalue())
        self.assertEqual(result['error_type'], 'HTTPError')
        self.assertEqual(result['http_status'], 403)
        self.assertNotIn('sensitive-test-value', stdout.getvalue())


if __name__ == '__main__':
    unittest.main()
