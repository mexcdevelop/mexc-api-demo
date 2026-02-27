package mexcfutures

import (
	"encoding/json"
	"fmt"
	"sort"
	"strconv"
)

// FilterParams returns a copy of m with nil/zero values omitted for optional query params.
// Keys with empty string are kept (API may treat "" differently). Only explicit nil is dropped.
func FilterParams(m map[string]interface{}) map[string]interface{} {
	if m == nil {
		return nil
	}
	out := make(map[string]interface{}, len(m))
	for k, v := range m {
		if v == nil {
			continue
		}
		out[k] = v
	}
	return out
}

// BuildQueryString builds a URL query string from params: keys sorted, key=value joined with &.
// Used for GET/DELETE request URL and for signature (signer uses raw values; caller encodes for URL).
func BuildQueryString(params map[string]interface{}) string {
	if len(params) == 0 {
		return ""
	}
	keys := make([]string, 0, len(params))
	for k := range params {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	// Build without URL encoding here; encoder is applied when building final URL.
	var b []byte
	for i, k := range keys {
		if i > 0 {
			b = append(b, '&')
		}
		b = append(b, k...)
		b = append(b, '=')
		b = append(b, []byte(stringValue(params[k]))...)
	}
	return string(b)
}

func stringValue(v interface{}) string {
	if v == nil {
		return ""
	}
	switch x := v.(type) {
	case string:
		return x
	case float64:
		return jsonNumber(x)
	case int:
		return jsonNumber(float64(x))
	case int64:
		return jsonNumber(float64(x))
	case bool:
		if x {
			return "true"
		}
		return "false"
	default:
		// Fallback: JSON encode single value (e.g. array as "[1,2,3]")
		data, _ := json.Marshal(v)
		return string(data)
	}
}

func jsonNumber(f float64) string {
	if f == float64(int64(f)) {
		return strconv.FormatInt(int64(f), 10)
	}
	return strconv.FormatFloat(f, 'f', -1, 64)
}

// CheckSymbol validates symbol is a non-empty string (for required symbol).
func CheckSymbol(symbol, name string) error {
	if name == "" {
		name = "symbol"
	}
	if symbol == "" {
		return fmt.Errorf("%s required and must be string", name)
	}
	return nil
}

// CheckLimit validates limit is in [1, max].
func CheckLimit(limit int, max int) error {
	if max == 0 {
		max = 100
	}
	if limit < 1 || limit > max {
		return fmt.Errorf("limit must be number 1-%d", max)
	}
	return nil
}

// CheckPage validates page_num >= 1 and page_size in [1, pageSizeMax].
func CheckPage(pageNum, pageSize int, pageSizeMax int) error {
	if pageSizeMax == 0 {
		pageSizeMax = 100
	}
	if pageNum < 1 {
		return fmt.Errorf("page_num must be number >= 1")
	}
	if pageSize < 1 || pageSize > pageSizeMax {
		return fmt.Errorf("page_size must be number 1-%d", pageSizeMax)
	}
	return nil
}
