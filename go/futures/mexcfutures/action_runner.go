package mexcfutures

import (
	"context"
	"encoding/json"
	"fmt"
	"sort"
	"strconv"
	"strings"
)

const defaultSymbol = "BTC_USDT"
const defaultDepthLimit = 20

// actionHandler is the type for a single action implementation.
type actionHandler func(ctx context.Context, c *FuturesRestClient, params map[string]any) (*RawResponse, error)

// privateActions contains all private action names (account, position, order, plan, stop, track, stp).
var privateActions = map[string]bool{}

var actionHandlers map[string]actionHandler

func init() {
	actionHandlers = initActionHandlers()
	// Build privateActions from all registered actions that are not public.
	publicSet := map[string]bool{
		"ping": true, "ticker": true, "depth": true, "contract_detail": true, "support_currencies": true,
		"depth_commits": true, "index_price": true, "fair_price": true, "funding_rate": true, "funding_rate_history": true,
		"kline": true, "kline_index": true, "kline_fair": true, "deals": true, "risk_reverse": true, "risk_reverse_history": true,
	}
	for name := range actionHandlers {
		if !publicSet[name] {
			privateActions[name] = true
		}
	}
}

func initActionHandlers() map[string]actionHandler {
	m := make(map[string]actionHandler)
	registerAllActions(m)
	return m
}

// RunAction runs a single REST action by name with the given params.
// Params may be nil; default values apply per action.
// Private actions require the client to be configured with API key/secret; otherwise returns error.
func (c *FuturesRestClient) RunAction(ctx context.Context, action string, params map[string]any) (*RawResponse, error) {
	if action == "" {
		return nil, fmt.Errorf("action required")
	}
	if privateActions[action] && !c.http.hasAuth() {
		return nil, fmt.Errorf("API key/secret required for %s", action)
	}
	h, ok := actionHandlers[action]
	if !ok {
		supported := ListActions()
		sort.Strings(supported)
		return nil, fmt.Errorf("unknown action: %s. supported actions: %s", action, strings.Join(supported, ", "))
	}
	return h(ctx, c, params)
}

// ListActions returns a slice of supported action names (for help/documentation).
func ListActions() []string {
	names := make([]string, 0, len(actionHandlers))
	for k := range actionHandlers {
		names = append(names, k)
	}
	return names
}

// ----- param helpers (params may be nil; support string/int/int64/float64) -----

func paramStr(params map[string]any, key, defaultVal string) string {
	if params == nil {
		return defaultVal
	}
	v, ok := params[key]
	if !ok || v == nil {
		return defaultVal
	}
	s, _ := v.(string)
	if s == "" {
		return defaultVal
	}
	return s
}

// paramStrOrFormat returns string value; if key is present but not string (e.g. int), formats it.
func paramStrOrFormat(params map[string]any, key string) string {
	if params == nil {
		return ""
	}
	v, ok := params[key]
	if !ok || v == nil {
		return ""
	}
	if s, ok := v.(string); ok && s != "" {
		return s
	}
	return fmt.Sprint(v)
}

func paramInt(params map[string]any, key string, defaultVal int) int {
	if params == nil {
		return defaultVal
	}
	v, ok := params[key]
	if !ok || v == nil {
		return defaultVal
	}
	switch x := v.(type) {
	case int:
		return x
	case int64:
		return int(x)
	case float64:
		return int(x)
	case string:
		n, err := strconv.Atoi(x)
		if err != nil {
			return defaultVal
		}
		return n
	default:
		return defaultVal
	}
}

func paramInt64(params map[string]any, key string, defaultVal int64) int64 {
	if params == nil {
		return defaultVal
	}
	v, ok := params[key]
	if !ok || v == nil {
		return defaultVal
	}
	switch x := v.(type) {
	case int64:
		return x
	case int:
		return int64(x)
	case float64:
		return int64(x)
	case string:
		n, err := strconv.ParseInt(x, 10, 64)
		if err != nil {
			return defaultVal
		}
		return n
	default:
		return defaultVal
	}
}

func hasParam(params map[string]any, key string) bool {
	if params == nil {
		return false
	}
	v, ok := params[key]
	return ok && v != nil
}

func klineParamsFromMap(params map[string]any) KlineParams {
	return KlineParams{
		Interval: paramStr(params, "interval", "Min1"),
		Start:    paramInt64(params, "start", 0),
		End:      paramInt64(params, "end", 0),
	}
}

// parseOrderNumber converts vol/price/leverage from string, json.Number, or numeric type to float64.
func parseOrderNumber(params map[string]any, key string) (float64, bool, error) {
	if params == nil {
		return 0, false, nil
	}
	v, ok := params[key]
	if !ok || v == nil {
		return 0, false, nil
	}
	switch val := v.(type) {
	case string:
		f, err := strconv.ParseFloat(val, 64)
		if err != nil {
			return 0, true, fmt.Errorf("create_order params.%s must be a number (got %T)", key, v)
		}
		return f, true, nil
	case json.Number:
		f, err := val.Float64()
		if err != nil {
			return 0, true, fmt.Errorf("create_order params.%s must be a number (got %T)", key, v)
		}
		return f, true, nil
	case int:
		return float64(val), true, nil
	case int64:
		return float64(val), true, nil
	case float64:
		return val, true, nil
	default:
		return 0, true, fmt.Errorf("create_order params.%s must be a number (got %T)", key, v)
	}
}

func cancelOrderIDsFromParams(params map[string]any) ([]int64, error) {
	if params == nil {
		return nil, fmt.Errorf("cancel_order requires params.orderId or params.orderIds")
	}
	var ids []int64
	if raw, ok := params["orderIds"]; ok && raw != nil {
		sl, ok := toSlice(raw)
		if !ok {
			return nil, fmt.Errorf("cancel_order requires params.orderId or params.orderIds")
		}
		for _, v := range sl {
			id := anyToOrderID(v)
			if id != 0 {
				ids = append(ids, id)
			}
		}
	}
	if len(ids) == 0 {
		if raw, ok := params["orderId"]; ok && raw != nil {
			id := anyToOrderID(raw)
			if id != 0 {
				ids = []int64{id}
			}
		}
	}
	if len(ids) == 0 {
		return nil, fmt.Errorf("cancel_order requires params.orderId or params.orderIds")
	}
	return ids, nil
}

func anyToOrderID(v any) int64 {
	if v == nil {
		return 0
	}
	switch x := v.(type) {
	case int64:
		return x
	case int:
		return int64(x)
	case float64:
		return int64(x)
	case string:
		id, err := strconv.ParseInt(x, 10, 64)
		if err != nil {
			return 0
		}
		return id
	case json.Number:
		n, err := x.Int64()
		if err != nil {
			return 0
		}
		return n
	default:
		return 0
	}
}
