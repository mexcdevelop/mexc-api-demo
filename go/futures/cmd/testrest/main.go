// Program testrest is the single REST test entry for MEXC Futures Go SDK.
// Edit the target config below and run: go run ./cmd/testrest
// Loads .env from project root if present. Prints HTTP status + raw body for every call.
package main

import (
	"context"
	"fmt"
	"os"
	"path/filepath"
	"time"

	"github.com/joho/godotenv"
	"mexc-futures-go/mexcfutures"
)

// Target configures which REST action to run and its parameters.
// Param rules and supported actions are defined in mexcfutures.RunAction / ListActions.
type Target struct {
	Action string
	Params map[string]any
	Note   string
}

// Edit target here. Example configs (uncomment one to use):
//
// --- Public ---
//
// Public ticker:
//
//	var target = Target{Action: "ticker", Params: map[string]any{"symbol": "BTC_USDT"}, Note: "Test GetTicker"}
//
// Public funding_rate_history (with page_num, page_size):
//
//	var target = Target{
//	  Action: "funding_rate_history",
//	  Params: map[string]any{"symbol": "BTC_USDT", "page_num": 1, "page_size": 20},
//	  Note:   "Test GetFundingRateHistory",
//	}
//
// Public risk_reverse (symbol empty = all contracts):
//
//	var target = Target{Action: "risk_reverse", Params: map[string]any{}, Note: "Test GetRiskReverse (all)"}
//
// Public risk_reverse (single symbol):
//
//	var target = Target{Action: "risk_reverse", Params: map[string]any{"symbol": "BTC_USDT"}, Note: "Test GetRiskReverse"}
//
// --- Private (require MEXC_API_KEY + MEXC_API_SECRET in .env) ---
//
// Private assets:
//
//	var target = Target{Action: "assets", Params: map[string]any{}, Note: "Test GetAssets"}
//
// Private order_detail (orderId):
//
//	var target = Target{Action: "order_detail", Params: map[string]any{"orderId": "123456"}, Note: "Test GetOrderDetail"}
//
// Private submit_order (string numbers auto-converted):
//
//	var target = Target{
//	  Action: "submit_order",
//	  Params: map[string]any{
//	    "symbol": "BTC_USDT", "vol": "1", "side": "OPEN", "type": "LIMIT", "openType": "ISOLATED", "price": "50000",
//	  },
//	  Note: "Test SubmitOrder",
//	}
//
// Private submit_order_batch (array body via batchOrder):
//
//	var target = Target{
//	  Action: "submit_order_batch",
//	  Params: map[string]any{
//	    "batchOrder": []any{
//	      map[string]any{"symbol": "BTC_USDT", "vol": "1", "side": "OPEN", "type": "LIMIT", "openType": "ISOLATED", "price": "50000"},
//	    },
//	  },
//	  Note: "Test SubmitOrderBatch",
//	}
//
// Private cancel_order by orderId or orderIds:
//
//	var target = Target{Action: "cancel_order", Params: map[string]any{"orderId": "123"}, Note: "Test CancelOrder"}
//	var target = Target{Action: "cancel_order", Params: map[string]any{"orderIds": []any{"123", 456}}, Note: "Test CancelOrder batch"}
//
// Private plan_orders (start_time, end_time, page_num, page_size):
//
//	var target = Target{
//	  Action: "plan_orders",
//	  Params: map[string]any{"start_time": 1700000000000, "end_time": 1700086400000, "page_num": 1, "page_size": 20},
//	  Note:   "Test GetPlanOrders",
//	}
//
// Private stop_open_orders (no required params):
//
//	var target = Target{Action: "stop_open_orders", Params: map[string]any{}, Note: "Test GetStopOpenOrders"}
//
// Private stp_list (example params):
//
//	var target = Target{Action: "stp_list", Params: map[string]any{"pageNum": 1, "pageSize": 20}, Note: "Test GetStpList"}
//
// Default: public kline_fair
var target = Target{
	Action: "index_price",
	Params: map[string]any{"symbol": "BTC_USDT"},
	Note:   "Public: index_price BTC_USDT",
}

// findAndLoadDotEnv walks up from the current working directory up to maxUp levels,
// looking for a .env file. Loads the first one found and returns its path and true; otherwise ("", false).
func findAndLoadDotEnv(maxUp int) (string, bool) {
	dir, err := os.Getwd()
	if err != nil {
		return "", false
	}
	for i := 0; i <= maxUp; i++ {
		candidate := filepath.Join(dir, ".env")
		fi, err := os.Stat(candidate)
		if err == nil && !fi.IsDir() {
			if loadErr := godotenv.Load(candidate); loadErr == nil {
				return candidate, true
			}
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			break
		}
		dir = parent
	}
	return "", false
}

func main() {
	if path, ok := findAndLoadDotEnv(6); ok {
		fmt.Println("Loaded .env:", path)
	}

	cfg := mexcfutures.DefaultConfig()
	apiKey := os.Getenv("MEXC_API_KEY")
	apiSecret := os.Getenv("MEXC_API_SECRET")
	if apiKey != "" && apiSecret != "" {
		cfg.WithAPIKey(apiKey, apiSecret)
	}
	if baseURL := os.Getenv("MEXC_BASE_URL"); baseURL != "" {
		cfg.BaseURL = baseURL
	}

	ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer cancel()

	client := mexcfutures.NewFuturesRestClient(cfg)

	if target.Note != "" {
		fmt.Println("Note:", target.Note)
	}

	resp, err := client.RunAction(ctx, target.Action, target.Params)
	printResult(target.Action, resp, err)
	if err != nil {
		os.Exit(1)
	}
}

func printResult(name string, resp *mexcfutures.RawResponse, err error) {
	fmt.Printf("==== %s ====\n", name)
	if resp != nil {
		fmt.Printf("HTTP status: %d\n", resp.StatusCode)
		fmt.Printf("Raw body: %s\n", resp.Text)
	} else {
		fmt.Println("No response (nil)")
	}
	if err != nil {
		if e, ok := err.(*mexcfutures.APIError); ok {
			fmt.Printf("Error: APIError http=%d code=%d message=%s\n", e.HTTPStatus, e.Code, e.Message)
			fmt.Printf("Error raw body: %s\n", string(e.RawBody))
		} else {
			fmt.Printf("Error: %v\n", err)
		}
	} else {
		fmt.Println("Error: <nil>")
	}
}
