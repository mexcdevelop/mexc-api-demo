package mexcfutures

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"strconv"
	"time"
)

// Sign computes the MEXC Futures OPEN-API signature (HMAC-SHA256, lowercase hex).
// stringToSign = accessKey + requestTime + parameterString.
// GET/DELETE: parameterString is sorted query string (key=value&..., no URL encoding).
// POST: parameterString is the raw JSON string of the body (no key sorting; arrays preserved).
// See https://www.mexc.com/api-docs/futures/integration-guide.
func Sign(accessKey, apiSecret, requestTime, method string, params interface{}) (string, error) {
	if accessKey == "" || apiSecret == "" {
		return "", fmt.Errorf("accessKey and apiSecret required for sign")
	}
	reqTime := requestTime
	if reqTime == "" {
		reqTime = strconv.FormatInt(nowMs(), 10)
	}

	var parameterString string
	switch method {
	case "GET", "DELETE":
		m, ok := toMap(params)
		if !ok {
			parameterString = ""
		} else {
			filtered := FilterParams(m)
			parameterString = BuildQueryString(filtered)
		}
	case "POST":
		var err error
		parameterString, err = parameterStringForPost(params)
		if err != nil {
			return "", err
		}
	default:
		parameterString = ""
	}

	stringToSign := accessKey + reqTime + parameterString
	h := hmac.New(sha256.New, []byte(apiSecret))
	h.Write([]byte(stringToSign))
	sig := hex.EncodeToString(h.Sum(nil))
	return sig, nil
}

// parameterStringForPost returns the exact JSON string to sign for POST (no key sorting).
// Supports map or slice (raw array body).
func parameterStringForPost(params interface{}) (string, error) {
	if params == nil {
		return "", nil
	}
	switch v := params.(type) {
	case []interface{}:
		data, err := json.Marshal(v)
		if err != nil {
			return "", err
		}
		return string(data), nil
	case []byte:
		return string(v), nil
	}
	m, ok := toMap(params)
	if !ok {
		data, err := json.Marshal(params)
		if err != nil {
			return "", err
		}
		return string(data), nil
	}
	filtered := FilterParams(m)
	data, err := json.Marshal(filtered)
	if err != nil {
		return "", err
	}
	return string(data), nil
}

func toMap(params interface{}) (map[string]interface{}, bool) {
	if params == nil {
		return nil, false
	}
	m, ok := params.(map[string]interface{})
	return m, ok
}

// nowMs returns current Unix time in milliseconds (used when requestTime is empty).
var nowMs = func() int64 { return time.Now().UnixMilli() }
