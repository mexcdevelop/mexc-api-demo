// Program testws verifies MEXC Futures WebSocket behavior.
// Change the target constant, then run: go run ./cmd/testws
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"os"
	"os/signal"
	"path/filepath"
	"strings"
	"syscall"
	"time"

	"github.com/joho/godotenv"
	"mexc-futures-go/mexcfutures"
)

// target selects which WS action to run.
// Base: connectOnly, ping, sendRaw
// Public: subTicker, unsubTicker, subTickers, unsubTickers, subDeal, unsubDeal,
//
//	subDepth, unsubDepth, subDepthFull, unsubDepthFull, subKline, unsubKline,
//	subFundingRate, unsubFundingRate, subIndexPrice, unsubIndexPrice,
//	subFairPrice, unsubFairPrice, subContract, unsubContract, subEventContract, unsubEventContract
//
// Private: login, filterAssets, filterOrders, filterOrderDeals, filterPositions,
//
//	filterPlanOrders, filterStopOrders, filterStopPlanOrders, filterRiskLimit,
//	filterAdlLevel, resetPersonalFilters, filterCustom
const target = "subContract"

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

	apiKey := os.Getenv("MEXC_API_KEY")
	apiSecret := os.Getenv("MEXC_API_SECRET")

	cfg := mexcfutures.DefaultConfig()
	if apiKey != "" && apiSecret != "" {
		cfg.WithAPIKey(apiKey, apiSecret)
	}
	if wsURL := os.Getenv("MEXC_WS_URL"); wsURL != "" {
		cfg.WSURL = wsURL
	}

	// Diagnostic output before Connect.
	if wd, err := os.Getwd(); err == nil {
		fmt.Println("CWD:", wd)
	}
	fmt.Println("WSURL:", cfg.WSURL)
	fmt.Println("HasKey:", cfg.APIKey != "", "HasSecret:", cfg.APISecret != "")

	ws := mexcfutures.NewWSClient(cfg)

	// Basic event logging.
	ws.OnOpen(func() {
		fmt.Println("[ws] open")
	})
	ws.OnReconnect(func() {
		fmt.Println("[ws] reconnecting")
	})
	ws.OnClose(func(code int, text string) {
		fmt.Printf("[ws] close code=%d text=%s\n", code, text)
	})
	ws.OnError(func(err error) {
		fmt.Printf("[ws] error: %v\n", err)
	})
	ws.OnPong(func(data any) {
		switch v := data.(type) {
		case float64:
			fmt.Printf("[ws] pong: %d\n", int64(v))
		case int64:
			fmt.Printf("[ws] pong: %d\n", v)
		case int:
			fmt.Printf("[ws] pong: %d\n", v)
		case json.Number:
			fmt.Printf("[ws] pong: %s\n", v.String())
		default:
			fmt.Printf("[ws] pong: %v\n", data)
		}
	})
	ws.OnMessage(func(raw string, msg map[string]any) {
		fmt.Printf("[ws] message: %s\n", raw)
	})

	// Connect with timeout so it never blocks silently.
	fmt.Println("before connect")
	dialCtx, dialCancel := context.WithTimeout(context.Background(), 10*time.Second)
	if err := ws.Connect(dialCtx); err != nil {
		dialCancel()
		fmt.Printf("connect error: %v\n", err)
		return
	}
	dialCancel()
	fmt.Println("after connect ok")

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	fmt.Printf("Action: %s\n", target)

	if err := runTarget(ctx, ws, cfg, target); err != nil {
		fmt.Printf("runTarget error: %v\n", err)
	}

	// Keep running until Ctrl+C.
	fmt.Println("Press Ctrl+C to exit.")
	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, os.Interrupt, syscall.SIGTERM)
	<-sigCh

	fmt.Println("Shutting down...")
	ws.Disconnect()
}

func runTarget(ctx context.Context, ws *mexcfutures.WSClient, cfg mexcfutures.Config, action string) error {
	// Determine if this is a private filter action.
	isPrivateFilter := strings.HasPrefix(action, "filter") || action == "login"
	if isPrivateFilter && cfg.APIKey != "" && cfg.APISecret != "" && action != "login" {
		// Best-effort login before filter actions.
		fmt.Println("[ws] auto login before private filter action")
		if err := ws.Login(false); err != nil {
			return fmt.Errorf("login failed: %w", err)
		}
		// Give the server a brief moment; real apps should handle rs.login channel explicitly.
		time.Sleep(500 * time.Millisecond)
	}

	switch action {
	case "connectOnly":
		// Do nothing else.
		return nil
	case "ping":
		return ws.Ping()
	case "sendRaw":
		raw := `{"method":"ping"}`
		return ws.SendText(raw)

	case "subTickers":
		return ws.SubTickers(map[string]any{})
	case "unsubTickers":
		return ws.UnsubTickers(map[string]any{})
	case "subTicker":
		return ws.SubTicker(map[string]any{"symbol": "BTC_USDT"})
	case "unsubTicker":
		return ws.UnsubTicker(map[string]any{"symbol": "BTC_USDT"})
	case "subDeal":
		return ws.SubDeal(map[string]any{"symbol": "BTC_USDT"})
	case "unsubDeal":
		return ws.UnsubDeal(map[string]any{"symbol": "BTC_USDT"})
	case "subDepth":
		return ws.SubDepth(map[string]any{"symbol": "BTC_USDT", "limit": 20})
	case "unsubDepth":
		return ws.UnsubDepth(map[string]any{"symbol": "BTC_USDT"})
	case "subDepthFull":
		return ws.SubDepthFull(map[string]any{"symbol": "BTC_USDT", "limit": 20})
	case "unsubDepthFull":
		return ws.UnsubDepthFull(map[string]any{"symbol": "BTC_USDT"})
	case "subKline":
		return ws.SubKline(map[string]any{"symbol": "BTC_USDT", "interval": "Min1"})
	case "unsubKline":
		return ws.UnsubKline(map[string]any{"symbol": "BTC_USDT", "interval": "Min1"})
	case "subFundingRate":
		return ws.SubFundingRate(map[string]any{"symbol": "BTC_USDT"})
	case "unsubFundingRate":
		return ws.UnsubFundingRate(map[string]any{"symbol": "BTC_USDT"})
	case "subIndexPrice":
		return ws.SubIndexPrice(map[string]any{"symbol": "BTC_USDT"})
	case "unsubIndexPrice":
		return ws.UnsubIndexPrice(map[string]any{"symbol": "BTC_USDT"})
	case "subFairPrice":
		return ws.SubFairPrice(map[string]any{"symbol": "BTC_USDT"})
	case "unsubFairPrice":
		return ws.UnsubFairPrice(map[string]any{"symbol": "BTC_USDT"})

	case "subContract":
		return ws.SubContract()
	case "unsubContract":
		return ws.UnsubContract()
	case "subEventContract":
		return ws.SubEventContract()
	case "unsubEventContract":
		return ws.UnsubEventContract()

	case "login":
		return ws.Login(false)
	case "filterAssets":
		return ws.FilterAssets()
	case "filterOrders":
		return ws.FilterOrders(map[string]any{"symbols": []string{"BTC_USDT"}})
	case "filterOrderDeals":
		return ws.FilterOrderDeals(map[string]any{"symbols": []string{"BTC_USDT"}})
	case "filterPositions":
		return ws.FilterPositions(map[string]any{"symbols": []string{"BTC_USDT"}})
	case "filterPlanOrders":
		return ws.FilterPlanOrders(map[string]any{"symbols": []string{"BTC_USDT"}})
	case "filterStopOrders":
		return ws.FilterStopOrders(map[string]any{"symbols": []string{"BTC_USDT"}})
	case "filterStopPlanOrders":
		return ws.FilterStopPlanOrders(map[string]any{"symbols": []string{"BTC_USDT"}})
	case "filterRiskLimit":
		return ws.FilterRiskLimit()
	case "filterAdlLevel":
		return ws.FilterAdlLevel()
	case "resetPersonalFilters":
		return ws.ResetPersonalFilters()
	case "filterCustom":
		params := map[string]any{
			"filters": []map[string]any{
				{"filter": "risk.limit"},
				{"filter": "adl.level"},
			},
		}
		return ws.FilterCustom(params)
	default:
		fmt.Printf("Unknown target %q\n", action)
		fmt.Println("Available actions: connectOnly, ping, sendRaw, subTicker, unsubTicker, subTickers, unsubTickers, subDeal, unsubDeal, subDepth, unsubDepth, subDepthFull, unsubDepthFull, subKline, unsubKline, subFundingRate, unsubFundingRate, subIndexPrice, unsubIndexPrice, subFairPrice, unsubFairPrice, login, filterAssets, filterOrders, filterOrderDeals, filterPositions, filterPlanOrders, filterStopOrders, filterStopPlanOrders, filterRiskLimit, filterAdlLevel, resetPersonalFilters, filterCustom")
		// Simple suggestion: list actions containing the substring.
		suggestActions(action)
		return nil
	}
}

func suggestActions(name string) {
	actions := []string{
		"connectOnly", "ping", "sendRaw",
		"subTicker", "unsubTicker", "subTickers", "unsubTickers",
		"subDeal", "unsubDeal", "subDepth", "unsubDepth",
		"subDepthFull", "unsubDepthFull", "subKline", "unsubKline",
		"subFundingRate", "unsubFundingRate", "subIndexPrice", "unsubIndexPrice",
		"subFairPrice", "unsubFairPrice",
		"subContract", "unsubContract", "subEventContract", "unsubEventContract",
		"login", "filterAssets", "filterOrders", "filterOrderDeals",
		"filterPositions", "filterPlanOrders", "filterStopOrders",
		"filterStopPlanOrders", "filterRiskLimit", "filterAdlLevel",
		"resetPersonalFilters", "filterCustom",
	}
	var suggestions []string
	for _, a := range actions {
		if strings.Contains(a, name) {
			suggestions = append(suggestions, a)
		}
	}
	if len(suggestions) > 0 {
		fmt.Printf("Did you mean: %s ?\n", strings.Join(suggestions, ", "))
	}
}
