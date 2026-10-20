# MEXC Bot Workbench

A Donchian signal monitor and an account-backed Spot bot scaffold. The automated
workflow uses public market data only. It never reads an account, persists a
portfolio, or places an order.

## Setup and tests

Use Python 3.12 or later in this directory:

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
python -m unittest discover -s tests -v
```

On Windows, activate with `.venv/Scripts/activate` instead. The runtime imports
only `requests` and `python-dotenv`; exchange credentials are not needed for tests
or public monitoring.

## Public-data monitoring

```bash
python monitor.py --output monitor-output/latest.json
```

Each run checks BTCUSDT using one-minute candles, a 20-period entry channel and a
10-period exit channel. It excludes the open candle and rejects missing, invalid,
out-of-order or stale data. The snapshot contains the current price and two
scenarios: the signal without a position, and the signal with a position.
Actual holdings remain unknown. A SELL signal is a conditional strategy output,
not an instruction to sell an account holding.

The monitor always uses anonymous public requests to `https://api.mexc.com`,
even if live-trading flags or credentials exist in the environment. No balance,
position size, execution, daily P/L or portfolio simulation is performed.
Request failures report an exception class and HTTP status without signed URLs.

## GitHub automation

The repository workflow `.github/workflows/mexc-workbench.yml`:

- Runs the test suite on relevant pushes and pull requests.
- Checks public signals on the default branch at minutes 7, 22, 37 and 52 UTC.
- Supports **Actions → MEXC Workbench → Run workflow** for an immediate check.
- Keeps each successful JSON snapshot as a workflow artifact for seven days.

GitHub may delay or skip scheduled runs; this is periodic monitoring, not a
continuous service or an execution scheduler. The workflow needs no exchange
secrets and has read-only repository permissions. Installation of the Workbench
plugin alone does not create or activate the GitHub workflow.

## Optional account-backed preview

Keep credentials in environment injection or a local `.env` excluded from Git.
`.env.example` contains blank credential fields and the scaffold defaults:
BTCUSDT, 5% quote-balance allocation, a configured 10 USDT minimum notional,
and a configured 2% loss threshold. These are application settings, not verified
exchange filters.

Run only one non-executing cycle:

```python
import os
os.environ["MEXC_LIVE_TRADING"] = "false"
from mexc_bot import Config, TradingBot

try:
    cfg = Config(live_trading=False, base_url="https://api.mexc.com")
    print(TradingBot(cfg).run_once())
except Exception as exc:
    response = getattr(exc, "response", None)
    print({"error_type": type(exc).__name__,
           "http_status": response.status_code if response is not None else None})
```

If a managed runtime exposes a verified key pair under other names, pass their
values to `Config(api_key=..., api_secret=..., live_trading=False,
base_url="https://api.mexc.com")` without printing or saving those values.
This preview reads account balances. A `dry_run` result is hypothetical and
does not update a paper portfolio.

## Execution limits

The inherited bot contains live-order code but is not validated for live use.
Quantity filters, free-versus-locked balances, closed candles, calendar-day loss
tracking, zero-equity handling, durable duplicate suppression and order
reconciliation still need engineering and exchange validation before live
execution. The public monitor does not enable that code path.

Astral's BTCUSD strategy uses a different quote currency and data provider from
MEXC BTCUSDT. Connecting Astral does not route this monitor's signals to MEXC.
