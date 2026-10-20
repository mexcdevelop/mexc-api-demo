from __future__ import annotations

import hashlib
import hmac
import os
import time
from dataclasses import dataclass
from typing import Dict, List, Optional
from urllib.parse import urlencode

import requests
from dotenv import load_dotenv

load_dotenv()


@dataclass
class Config:
    api_key: str = os.getenv("MEXC_API_KEY", "")
    api_secret: str = os.getenv("MEXC_API_SECRET", "")
    base_url: str = os.getenv("MEXC_BASE_URL", "https://api.mexc.com")
    live_trading: bool = os.getenv("MEXC_LIVE_TRADING", "false").lower() == "true"
    symbol: str = os.getenv("TRADING_SYMBOL", "BTCUSDT").upper()
    quote_asset: str = os.getenv("QUOTE_ASSET", "USDT").upper()
    max_position_pct: float = float(os.getenv("MAX_POSITION_PCT", "5"))
    max_daily_loss_pct: float = float(os.getenv("MAX_DAILY_LOSS_PCT", "2"))
    min_order_notional: float = float(os.getenv("MIN_ORDER_NOTIONAL", "10"))
    entry_window: int = int(os.getenv("DONCHIAN_ENTRY", "20"))
    exit_window: int = int(os.getenv("DONCHIAN_EXIT", "10"))
    poll_seconds: int = int(os.getenv("POLL_SECONDS", "60"))


class MexcClient:
    def __init__(self, cfg: Config):
        self.cfg = cfg
        self.session = requests.Session()
        if cfg.api_key:
            self.session.headers.update({"X-MEXC-APIKEY": cfg.api_key})

    def _signed_params(self, params: Optional[dict] = None) -> dict:
        if not self.cfg.api_secret:
            raise RuntimeError("MEXC_API_SECRET is not configured")
        params = dict(params or {})
        params["timestamp"] = int(time.time() * 1000)
        query = urlencode(params)
        params["signature"] = hmac.new(
            self.cfg.api_secret.encode(), query.encode(), hashlib.sha256
        ).hexdigest()
        return params

    def _get(self, path: str, params: Optional[dict] = None, signed: bool = False):
        if signed:
            params = self._signed_params(params)
        # Public market requests must not inherit the account API-key header.
        headers = None if signed else {"X-MEXC-APIKEY": None}
        r = self.session.get(
            f"{self.cfg.base_url}{path}", params=params, headers=headers, timeout=15
        )
        r.raise_for_status()
        return r.json()

    def _post(self, path: str, params: dict, signed: bool = False):
        if signed:
            params = self._signed_params(params)
        r = self.session.post(f"{self.cfg.base_url}{path}", params=params, timeout=15)
        r.raise_for_status()
        return r.json()

    def exchange_info(self):
        return self._get("/api/v3/exchangeInfo", {"symbol": self.cfg.symbol})

    def klines(self, limit: int = 100) -> List[list]:
        return self._get(
            "/api/v3/klines",
            {"symbol": self.cfg.symbol, "interval": "1m", "limit": limit},
        )

    def account(self):
        return self._get("/api/v3/account", signed=True)

    def price(self) -> float:
        data = self._get("/api/v3/ticker/price", {"symbol": self.cfg.symbol})
        return float(data["price"])

    def test_order(self, side: str, quantity: float):
        params = {
            "symbol": self.cfg.symbol,
            "side": side,
            "type": "MARKET",
            "quantity": f"{quantity:.8f}",
        }
        return self._post("/api/v3/order/test", params, signed=True)

    def place_market_order(self, side: str, quantity: float):
        params = {
            "symbol": self.cfg.symbol,
            "side": side,
            "type": "MARKET",
            "quantity": f"{quantity:.8f}",
        }
        return self._post("/api/v3/order", params, signed=True)


class DonchianSignal:
    def __init__(self, entry_window: int = 20, exit_window: int = 10):
        if entry_window < 2 or exit_window < 2:
            raise ValueError("Donchian windows must be >= 2")
        self.entry_window = entry_window
        self.exit_window = exit_window

    def evaluate(self, klines: List[list], in_position: bool) -> str:
        needed = max(self.entry_window, self.exit_window) + 1
        if len(klines) < needed:
            return "HOLD"
        highs = [float(k[2]) for k in klines]
        lows = [float(k[3]) for k in klines]
        close = float(klines[-1][4])
        if not in_position:
            prior_high = max(highs[-self.entry_window - 1 : -1])
            return "BUY" if close > prior_high else "HOLD"
        prior_low = min(lows[-self.exit_window - 1 : -1])
        return "SELL" if close < prior_low else "HOLD"


class RiskManager:
    def __init__(self, cfg: Config):
        self.cfg = cfg
        self.start_equity: Optional[float] = None

    @staticmethod
    def balances(account: dict) -> Dict[str, float]:
        return {
            x["asset"]: float(x.get("free", 0)) + float(x.get("locked", 0))
            for x in account.get("balances", [])
        }

    def check_daily_loss(self, equity: float) -> None:
        if self.start_equity is None:
            self.start_equity = equity
            return
        drawdown_pct = (self.start_equity - equity) / self.start_equity * 100
        if drawdown_pct >= self.cfg.max_daily_loss_pct:
            raise RuntimeError(
                f"Kill switch: daily loss {drawdown_pct:.2f}% exceeds limit "
                f"{self.cfg.max_daily_loss_pct:.2f}%"
            )

    def order_quantity(self, quote_balance: float, price: float) -> float:
        notional = quote_balance * (self.cfg.max_position_pct / 100)
        if notional < self.cfg.min_order_notional:
            return 0.0
        return notional / price


class TradingBot:
    def __init__(self, cfg: Config):
        self.cfg = cfg
        self.client = MexcClient(cfg)
        self.signal = DonchianSignal(cfg.entry_window, cfg.exit_window)
        self.risk = RiskManager(cfg)
        self.last_action_key: Optional[str] = None

    def _position_state(self, balances: Dict[str, float], price: float) -> tuple[bool, float, str]:
        base = self.cfg.symbol.removesuffix(self.cfg.quote_asset)
        base_qty = balances.get(base, 0.0)
        return (base_qty * price >= self.cfg.min_order_notional, base_qty, base)

    def run_once(self) -> dict:
        account = self.client.account()
        balances = self.risk.balances(account)
        price = self.client.price()
        quote = balances.get(self.cfg.quote_asset, 0.0)
        in_position, base_qty, base = self._position_state(balances, price)
        equity = quote + base_qty * price
        self.risk.check_daily_loss(equity)

        klines = self.client.klines(max(self.cfg.entry_window, self.cfg.exit_window) + 5)
        action = self.signal.evaluate(klines, in_position)
        candle_key = str(klines[-1][0])
        action_key = f"{candle_key}:{action}"

        result = {
            "symbol": self.cfg.symbol,
            "price": price,
            "signal": action,
            "in_position": in_position,
            "base_asset": base,
            "base_qty": base_qty,
            "quote_balance": quote,
            "live": self.cfg.live_trading,
        }

        if action == "HOLD" or action_key == self.last_action_key:
            return result

        if action == "BUY":
            qty = self.risk.order_quantity(quote, price)
        else:
            qty = base_qty

        if qty <= 0:
            result["order"] = "SKIPPED: quantity <= 0"
            return result

        if self.cfg.live_trading:
            result["order"] = self.client.place_market_order(action, qty)
        else:
            # MEXC's /order/test touches the live API surface but does not execute.
            # To avoid requiring trading permissions during local dry-run, we only log.
            result["order"] = {"dry_run": True, "side": action, "quantity": qty}

        self.last_action_key = action_key
        return result


if __name__ == "__main__":
    cfg = Config()
    if not cfg.api_key or not cfg.api_secret:
        raise SystemExit("Set MEXC_API_KEY and MEXC_API_SECRET in .env first")
    bot = TradingBot(cfg)
    while True:
        try:
            print(bot.run_once(), flush=True)
        except Exception as exc:
            print({"error": str(exc)}, flush=True)
        time.sleep(cfg.poll_seconds)
