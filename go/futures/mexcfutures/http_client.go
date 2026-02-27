package mexcfutures

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"net/url"
	"sort"
	"strconv"
	"strings"
	"time"
)

// RawResponse holds full HTTP response information for a REST call.
// It keeps the original body so callers can log/inspect the exact server payload.
type RawResponse struct {
	StatusCode int    // HTTP status code
	Body       []byte // Raw response body bytes
	Text       string // Raw response body as string (helper)
}

// DecodeJSON decodes the raw body into v. It does not modify the response itself.
func (r *RawResponse) DecodeJSON(v any) error {
	if r == nil {
		return fmt.Errorf("nil RawResponse")
	}
	if len(r.Body) == 0 {
		return nil
	}
	return json.Unmarshal(r.Body, v)
}

// HTTPClient performs MEXC Futures REST requests (public and private).
type HTTPClient struct {
	cfg    Config
	client *http.Client
}

// NewHTTPClient builds an HTTP client with the given config (timeout from config).
func NewHTTPClient(cfg Config) *HTTPClient {
	timeout := cfg.Timeout
	if timeout <= 0 {
		timeout = DefaultTimeout
	}
	return &HTTPClient{
		cfg: cfg,
		client: &http.Client{
			Timeout: timeout,
		},
	}
}

// hasAuth reports whether API key and secret are configured (for private endpoints).
func (c *HTTPClient) hasAuth() bool {
	return c.cfg.APIKey != "" && c.cfg.APISecret != ""
}

// PublicRequest sends a request without signature. params is used as query for GET/DELETE or ignored for POST (use body in that case).
// For POST, pass body as second argument or as params; here we use a single params for both query and body for simplicity; method decides.
func (c *HTTPClient) PublicRequest(ctx context.Context, method, path string, params map[string]interface{}) (*RawResponse, error) {
	return c.doRequest(ctx, method, path, params, nil, false)
}

// PrivateRequest sends a signed request. For GET/DELETE params are query; for POST params are JSON body (or pass body as raw map/array).
func (c *HTTPClient) PrivateRequest(ctx context.Context, method, path string, params interface{}) (*RawResponse, error) {
	return c.doRequest(ctx, method, path, params, nil, true)
}

func (c *HTTPClient) doRequest(ctx context.Context, method, path string, params interface{}, bodyBytes []byte, private bool) (*RawResponse, error) {
	baseURL := strings.TrimSuffix(c.cfg.BaseURL, "/")
	fullPath := baseURL + path
	var reqBody io.Reader
	var query string

	switch method {
	case http.MethodGet, http.MethodDelete:
		m := toParamsMap(params)
		filtered := FilterParams(m)
		query = buildEncodedQuery(filtered)
		if query != "" {
			fullPath = fullPath + "?" + query
		}
	case http.MethodPost:
		if bodyBytes != nil {
			reqBody = bytes.NewReader(bodyBytes)
		} else {
			payload, err := buildPostBody(params)
			if err != nil {
				return nil, err
			}
			reqBody = bytes.NewReader(payload)
		}
	default:
		return nil, fmt.Errorf("unsupported method: %s", method)
	}

	req, err := http.NewRequestWithContext(ctx, method, fullPath, reqBody)
	if err != nil {
		return nil, err
	}
	req.Header.Set("Content-Type", "application/json")
	if c.cfg.UserAgent != "" {
		req.Header.Set("User-Agent", c.cfg.UserAgent)
	}

	if private {
		requestTime := strconv.FormatInt(time.Now().UnixMilli(), 10)
		// Sign with same params used for query (GET/DELETE) or body (POST).
		sig, err := Sign(c.cfg.APIKey, c.cfg.APISecret, requestTime, method, params)
		if err != nil {
			return nil, err
		}
		req.Header.Set("ApiKey", c.cfg.APIKey)
		req.Header.Set("Request-Time", requestTime)
		req.Header.Set("Signature", sig)
		if c.cfg.RecvWindow > 0 {
			req.Header.Set("Recv-Window", strconv.FormatInt(c.cfg.RecvWindow, 10))
		}
		if c.cfg.Debug {
			debugLogRequest(method, fullPath, req.Header, reqBody, true)
		}
	} else if c.cfg.Debug {
		debugLogRequest(method, fullPath, req.Header, reqBody, false)
	}

	resp, err := c.client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()
	raw, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	out := &RawResponse{
		StatusCode: resp.StatusCode,
		Body:       raw,
		Text:       string(raw),
	}

	var decoded map[string]interface{}
	if len(raw) > 0 {
		_ = json.Unmarshal(raw, &decoded)
	}

	if resp.StatusCode >= 400 {
		apiErr := &APIError{
			HTTPStatus: resp.StatusCode,
			RawBody:    raw,
		}
		if decoded != nil {
			if c, ok := decoded["code"].(float64); ok {
				apiErr.Code = int(c)
			}
			if m, ok := decoded["message"].(string); ok {
				apiErr.Message = m
			}
		}
		return out, apiErr
	}
	// Optional: treat success=false as error even with 200
	if decoded != nil {
		if s, ok := decoded["success"].(bool); ok && !s {
			apiErr := &APIError{HTTPStatus: resp.StatusCode, RawBody: raw}
			if c, ok := decoded["code"].(float64); ok {
				apiErr.Code = int(c)
			}
			if m, ok := decoded["message"].(string); ok {
				apiErr.Message = m
			}
			return out, apiErr
		}
	}
	return out, nil
}

func toParamsMap(v interface{}) map[string]interface{} {
	if v == nil {
		return nil
	}
	m, ok := v.(map[string]interface{})
	if ok {
		return m
	}
	return nil
}

func buildEncodedQuery(params map[string]interface{}) string {
	if len(params) == 0 {
		return ""
	}
	keys := make([]string, 0, len(params))
	for k := range params {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	vals := make(url.Values)
	for _, k := range keys {
		vals.Set(k, queryValueString(params[k]))
	}
	return vals.Encode()
}

// queryValueString converts a param value to string for URL query (same semantics as util for consistency).
func queryValueString(v interface{}) string {
	if v == nil {
		return ""
	}
	switch x := v.(type) {
	case string:
		return x
	case float64:
		if x == float64(int64(x)) {
			return strconv.FormatInt(int64(x), 10)
		}
		return strconv.FormatFloat(x, 'f', -1, 64)
	case int:
		return strconv.Itoa(x)
	case int64:
		return strconv.FormatInt(x, 10)
	case bool:
		if x {
			return "true"
		}
		return "false"
	default:
		data, _ := json.Marshal(v)
		return string(data)
	}
}

func buildPostBody(params interface{}) ([]byte, error) {
	if params == nil {
		return []byte("{}"), nil
	}
	switch v := params.(type) {
	case map[string]interface{}:
		filtered := FilterParams(v)
		return json.Marshal(filtered)
	case []interface{}:
		return json.Marshal(v)
	default:
		return json.Marshal(params)
	}
}

// debugLogRequest logs method and URL only; never logs APISecret or Signature.
func debugLogRequest(method, fullURL string, headers http.Header, body io.Reader, redactSignature bool) {
	log.Printf("[mexcfutures] %s %s", method, fullURL)
	if redactSignature && headers.Get("Signature") != "" {
		log.Printf("[mexcfutures] Signature: ***")
	}
	_ = headers
	_ = body
}
