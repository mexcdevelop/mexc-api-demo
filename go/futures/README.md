# MEXC Futures Go SDK

Go SDK for the MEXC Futures OpenAPI (REST + WebSocket).

This module provides:

- A **REST client** for public and private futures endpoints.
- A **WebSocket client** for public market streams and private `login + personal.filter` streams.

Two example entrypoints are provided:

- `cmd/testrest`: REST debugging and examples.
- `cmd/testws`: WebSocket debugging and examples.

---

## Installation

Use Go modules to install dependencies and work with the SDK.

From inside the `mexc-futures-go` directory:

```bash
go mod tidy
```

If you are using this SDK from another Go project, add it as a module dependency
in your own `go.mod` and run:

```bash
go get <this-repo-module-path>
```

> The SDK has been tested with **Go 1.20+**. Newer Go versions should also work.

---

## Default configuration

The Go SDK uses defaults that match the official MEXC documentation:

| Type              | Default value                      | Overridden by                   |
|-------------------|------------------------------------|---------------------------------|
| REST `BaseURL`    | `https://api.mexc.com`             | `Config.BaseURL`                |
| WebSocket `WSURL` | `wss://contract.mexc.com/edge`     | `Config.WSURL` / `MEXC_WS_URL` |

You can override these via the `mexcfutures.Config` struct or environment
variables (see Configuration below).

---

## Configuration (.env)

Both REST and WS examples (`cmd/testrest`, `cmd/testws`) will attempt to load
configuration from a `.env` file. The recommended location is the **project
root** (alongside `go.mod`).

Example `.env`:

```dotenv
MEXC_API_KEY=your_api_key_here
MEXC_API_SECRET=your_api_secret_here

# Optional: custom REST base URL (default https://api.mexc.com)
MEXC_BASE_URL=https://api.mexc.com

# Optional: custom WebSocket URL (default wss://contract.mexc.com/edge)
MEXC_WS_URL=wss://contract.mexc.com/edge
```

Security notes:

- `.env` contains API key/secret. **Do not commit it to Git.**
- The root `.gitignore` already includes:
  - `.env`
  - `*.exe`
  - `testrest.exe` / `testws.exe`

Both `testrest` and `testws` walk up from the current working directory, up to
6 levels, to find the first `.env` and load it.

---

## REST API

The REST client is implemented in `mexcfutures/futures_rest_client.go` and
exposed via `mexcfutures.FuturesRestClient`.

It covers:

- **Public market data**
  - `Ping`, `GetContractDetail`, `GetSupportCurrencies`
  - `GetDepth`, `GetDepthCommits`
  - `GetIndexPrice`, `GetFairPrice`
  - `GetFundingRate`, `GetFundingRateHistory`
  - `GetKline`, `GetKlineIndexPrice`, `GetKlineFairPrice`
  - `GetDeals`, `GetTicker`, `GetRiskReverse`, `GetRiskReverseHistory`

- **Accounts and assets (private)**
  - `GetAssets`, `GetAsset`
  - `GetTransferRecords`, `GetProfitRate`
  - Asset analysis suite: `GetAssetAnalysis`, `GetAssetAnalysisV3`,
    `GetAssetAnalysisCalendarDaily`, `GetAssetAnalysisCalendarMonthly`,
    `GetAssetAnalysisRecent`, `GetAssetAnalysisExport`
  - PnL and fee/risk configuration:
    `GetYesterdayPnl`, `GetTodayPnl`, `GetFeeDeductConfigs`, `GetRiskLimit`,
    `ChangeRiskLevel`, `GetFeeRate`, `GetTieredFeeRate`, `GetDiscountType`,
    `GetContractFeeDiscountConfig`, `GetOrderDealFeeTotal`, `GetZeroFeeRate`

- **Positions and leverage (private)**
  - `GetPositions`, `GetHistoryPositions`
  - `GetLeverage`, `GetFundingRecords`
  - `ChangeMargin`, `ChangeAutoAddIm`, `ChangeLeverage`
  - `GetPositionMode`, `ChangePositionMode`
  - `ReversePosition`, `CloseAllPositions`

- **Orders / Plan orders / Stop orders / Track orders / STP (private)**
  - Orders: `SubmitOrder`, `SubmitOrderBatch`, `CancelOrder`, `CancelAllOrders`,
    `BatchCancelWithExternal`, `CancelWithExternal`, `GetOrderDetail`,
    `GetOpenOrders`, `GetHistoryOrders`, `GetCloseOrders`, `GetOrderDealList`,
    `GetOrderByExternal`, `BatchQueryOrders`, `BatchQueryWithExternal`,
    `GetOpenOrderTotalCount`, `GetOrderDealDetails`, `GetOrderFeeDetails`
  - Plan orders: `GetPlanOrders`, `PlacePlanOrder`, `ChangePlanOrderPrice`,
    `CancelPlanOrder`, `CancelAllPlanOrders`, `ChangePlanStopOrder`
  - Stop orders: `PlaceStopOrder`, `CancelStopOrder`, `CancelAllStopOrders`,
    `ChangeStopOrderPrice`, `ChangeStopPlanPrice`, `GetStopOrders`,
    `GetStopOpenOrders`
  - Track orders: `PlaceTrackOrder`, `CancelTrackOrder`, `ChangeTrackOrder`,
    `GetTrackOrders`
  - STP (self-trade prevention, market maker): `GetStpList`, `CreateStp`,
    `UpdateStp`, `DeleteStp`

The `cmd/testrest` example uses a generic `RunAction(ctx, action, params)` entry
to exercise these methods via snake_case actions (e.g. `ticker`,
`submit_order`, `open_orders`).

---

## WebSocket

The WebSocket client is implemented in `mexcfutures/futures_ws_client.go` and
exposed via `mexcfutures.WSClient`.

### Public channels

Supported public channels include:

- `sub.tickers` / `unsub.tickers` → `SubTickers` / `UnsubTickers`
- `sub.ticker` / `unsub.ticker` → `SubTicker` / `UnsubTicker`
- `sub.deal` / `unsub.deal` → `SubDeal` / `UnsubDeal`
- `sub.depth` / `unsub.depth` → `SubDepth` / `UnsubDepth`
- `sub.depth.full` / `unsub.depth.full` → `SubDepthFull` / `UnsubDepthFull`
- `sub.kline` / `unsub.kline` → `SubKline` / `UnsubKline`
- `sub.funding.rate` / `unsub.funding.rate` → `SubFundingRate` / `UnsubFundingRate`
- `sub.index.price` / `unsub.index.price` → `SubIndexPrice` / `UnsubIndexPrice`
- `sub.fair.price` / `unsub.fair.price` → `SubFairPrice` / `UnsubFairPrice`
- `sub.contract` / `unsub.contract` → `SubContract` / `UnsubContract`
- `sub.event.contract` / `unsub.event.contract` → `SubEventContract` / `UnsubEventContract`

### Private login and personal.filter

The WS client also supports:

- `Login` with API key/secret.
- Personal filter helpers:
  - `FilterAssets`, `FilterOrders`, `FilterOrderDeals`, `FilterPositions`
  - `FilterPlanOrders`, `FilterStopOrders`, `FilterStopPlanOrders`
  - `FilterRiskLimit`, `FilterAdlLevel`
  - `ResetPersonalFilters`, `FilterCustom`

All `push.*` messages are surfaced via the registered `OnMessage` callback on
`WSClient`.

---

## Examples

### REST examples (`cmd/testrest`)

Run from the project root:

```bash
go run ./cmd/testrest
```

`cmd/testrest/main.go` defines:

```go
type Target struct {
    Action string
    Params map[string]any
    Note   string
}
```

You can set `target` to exercise different actions.

#### Public actions (examples)

- `ticker`

```go
var target = Target{
    Action: "ticker",
    Params: map[string]any{"symbol": "BTC_USDT"},
    Note:   "Test GetTicker",
}
```

- `depth`

```go
var target = Target{
    Action: "depth",
    Params: map[string]any{"symbol": "BTC_USDT", "limit": 20},
    Note:   "Test GetDepth",
}
```

- `index_price`

```go
var target = Target{
    Action: "index_price",
    Params: map[string]any{"symbol": "BTC_USDT"},
    Note:   "Test GetIndexPrice",
}
```

- `fair_price`

```go
var target = Target{
    Action: "fair_price",
    Params: map[string]any{"symbol": "BTC_USDT"},
    Note:   "Test GetFairPrice",
}
```

- `funding_rate`

```go
var target = Target{
    Action: "funding_rate",
    Params: map[string]any{"symbol": "BTC_USDT"},
    Note:   "Test GetFundingRate",
}
```

#### Private actions (examples)

These require `MEXC_API_KEY` and `MEXC_API_SECRET` in `.env`.

- `assets`

```go
var target = Target{
    Action: "assets",
    Params: map[string]any{},
    Note:   "Test GetAssets",
}
```

- `positions`

```go
var target = Target{
    Action: "positions",
    Params: map[string]any{"symbol": "BTC_USDT"},
    Note:   "Test GetPositions",
}
```

- `submit_order`

```go
var target = Target{
    Action: "submit_order",
    Params: map[string]any{
        "symbol":   "BTC_USDT",
        "price":    "50000",
        "vol":      "1",
        "leverage": 10,
        "side":     1,
        "type":     1,
        "openType": 1,
    },
    Note: "Submit limit open long",
}
```

- `submit_order_batch`

```go
var target = Target{
    Action: "submit_order_batch",
    Params: map[string]any{
        "batchOrder": []any{
            map[string]any{
                "symbol":   "BTC_USDT",
                "price":    "50000",
                "vol":      "1",
                "leverage": 10,
                "side":     1,
                "type":     1,
                "openType": 1,
            },
        },
    },
    Note: "Submit batch orders",
}
```

- `cancel_order`

```go
var target = Target{
    Action: "cancel_order",
    Params: map[string]any{"orderId": "1234567890"},
    Note:   "Cancel single order",
}
```

- `open_orders`

```go
var target = Target{
    Action: "open_orders",
    Params: map[string]any{
        "symbol":    "BTC_USDT",
        "page_num":  1,
        "page_size": 20,
    },
    Note: "List open orders",
}
```

### WS examples (`cmd/testws`)

Run from the project root:

```bash
go run ./cmd/testws
```

In `cmd/testws/main.go`, change the `target` constant to test different
channels:

```go
const target = "ping"
// const target = "subTicker"
// const target = "subDepth"
// const target = "subKline"
// const target = "subFundingRate"
// const target = "subContract"
// const target = "subEventContract"
// const target = "login"
// const target = "filterOrders"
// const target = "filterOrderDeals"
// const target = "filterPositions"
// const target = "filterAssets"
// const target = "resetPersonalFilters"
```

Common patterns:

- `ping`: sends `{"method":"ping"}` and expects a `pong`.
- `subTicker`: subscribes to `BTC_USDT` ticker and prints `[ws] message: ...`.
- `subDepth`: subscribes to `BTC_USDT` depth.
- `subKline`: subscribes to `BTC_USDT` K-line with a chosen interval.
- `subFundingRate`: subscribes to `BTC_USDT` funding rate.
- `subContract`: subscribes to contract data (`push.contract`).
- `subEventContract`: subscribes to event contract stream.
- `login`: logs in using `MEXC_API_KEY/SECRET` from `.env`.
- `filterOrders`: enables personal order push streams (requires login).
- `filterOrderDeals`: enables personal order deal streams.
- `filterPositions`: enables personal position streams.
- `filterAssets`: enables personal asset streams.
- `resetPersonalFilters`: clears personal filter configuration on the server.

`OnPong` is implemented to pretty-print timestamps and avoid scientific
notation.

---

## Troubleshooting

### API key/secret required

If you see errors like `API key/secret required for assets` or similar, common
causes:

- `.env` is missing from the `mexc-futures-go` root.
- `MEXC_API_KEY` / `MEXC_API_SECRET` are not set or are empty.
- You are running `go run` from a subdirectory and the helper cannot find `.env`.

Fixes:

- Ensure `.env` is in the root of this repo.
- Use `go run ./cmd/testrest` / `go run ./cmd/testws` **from the repo root**, or
  ensure the upward search for `.env` works from your current directory.

### Proxy and network issues

The SDK respects standard Go proxy environment variables:

- `HTTP_PROXY`
- `HTTPS_PROXY`
- `NO_PROXY`

This affects both REST and WebSocket connections. If you are using Clash or
other proxy tools on Windows:

- Make sure `api.mexc.com` and `contract.mexc.com` are reachable through the
  configured proxy, **or**
- Bypass the proxy for these hosts using `NO_PROXY`.

Temporarily disable proxies in PowerShell:

```powershell
Remove-Item Env:HTTP_PROXY  -ErrorAction SilentlyContinue
Remove-Item Env:HTTPS_PROXY -ErrorAction SilentlyContinue
Remove-Item Env:ALL_PROXY  -ErrorAction SilentlyContinue
```

Only bypass MEXC hosts:

```powershell
$env:NO_PROXY = "api.mexc.com,contract.mexc.com"
```

In Bash:

```bash
export NO_PROXY="api.mexc.com,contract.mexc.com"
```

### WebSocket appears to hang

`WSClient.Connect(ctx)` uses `DialContext` in a goroutine and a `select` on:

- the dial result, or
- `ctx.Done()`

In `cmd/testws`, `ctx` is created via `context.WithTimeout(..., 10*time.Second)`,
so **Connect will return within 10 seconds** (either success or timeout).

If you see it “stuck” on `before connect`, likely causes:

- The upstream proxy is dropping or rejecting the connection.
- TLS handshake cannot complete.

Check the logged lines:

- `ws connect: dial error ...`
- `ws connect: ctx.Done() context deadline exceeded`

These messages help you differentiate DNS/TCP timeout/TLS issues/proxy errors.

### Pong printed in scientific notation

Earlier versions of the example printed `pong` using `%v` which could produce
scientific notation (e.g. `1.728e+12`) for timestamps.

The current `cmd/testws` example formats `OnPong` based on the underlying type:

- `float64` / `int` / `int64` are printed with `%d`.
- `json.Number` is printed via `v.String()`.
- Other types still use `%v`.

This ensures timestamps and counters are printed in a human-friendly format.

---

If you find a REST/WS capability in the official docs or Node.js examples that
is not yet covered by this Go SDK, please open an issue or PR. The goal is to
keep this SDK aligned with the official MEXC documentation and the Node
reference implementation.
