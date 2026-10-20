import hashlib
import hmac
import unittest
from urllib.parse import parse_qs, urlsplit

import requests

from mexc_bot import Config, MexcClient


class RecordingAdapter(requests.adapters.BaseAdapter):
    """Capture prepared requests without contacting the exchange."""

    def __init__(self):
        super().__init__()
        self.requests = []

    def send(self, request, **kwargs):
        self.requests.append(request)
        response = requests.Response()
        response.status_code = 200
        response.request = request
        response._content = b'{"price": "100"}'
        return response

    def close(self):
        pass


class AuthenticationTests(unittest.TestCase):
    def setUp(self):
        self.cfg = Config(
            api_key="unit-test-key",
            api_secret="unit-test-secret",
            base_url="https://api.mexc.com",
            live_trading=False,
        )
        self.client = MexcClient(self.cfg)
        self.adapter = RecordingAdapter()
        self.client.session.mount("https://", self.adapter)
        self.addCleanup(self.client.session.close)

    def test_public_market_requests_are_anonymous(self):
        for method in (self.client.klines, self.client.price, self.client.exchange_info):
            with self.subTest(endpoint=method.__name__):
                method()
                request = self.adapter.requests[-1]
                self.assertNotIn("X-MEXC-APIKEY", request.headers)
                self.assertNotIn("signature", parse_qs(urlsplit(request.url).query))

    def test_account_requests_keep_key_and_valid_signature(self):
        self.client.account()
        request = self.adapter.requests[-1]
        self.assertEqual(request.headers["X-MEXC-APIKEY"], self.cfg.api_key)
        query, signature = urlsplit(request.url).query.rsplit("&signature=", 1)
        expected = hmac.new(
            self.cfg.api_secret.encode(), query.encode(), hashlib.sha256
        ).hexdigest()
        self.assertEqual(signature, expected)


if __name__ == "__main__":
    unittest.main()
