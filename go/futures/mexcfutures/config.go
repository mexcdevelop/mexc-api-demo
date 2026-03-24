package mexcfutures

import "time"

// DefaultBaseURL is the default MEXC Futures REST API base URL (from official Integration Guide).
const DefaultBaseURL = "https://api.mexc.com"

// DefaultRecvWindowMs is the default receive window in milliseconds for signature time validation.
const DefaultRecvWindowMs = 10000

// DefaultTimeout is the default HTTP client timeout.
const DefaultTimeout = 30 * time.Second

// DefaultWSURL is the default MEXC Futures WebSocket URL.
const DefaultWSURL = "wss://contract.mexc.com/edge"

// DefaultWSPingInterval is the default WebSocket ping interval.
const DefaultWSPingInterval = 15 * time.Second

// DefaultWSReconnectInitialDelay is the initial WebSocket reconnect delay.
const DefaultWSReconnectInitialDelay = 1 * time.Second

// DefaultWSReconnectMaxDelay is the maximum WebSocket reconnect delay.
const DefaultWSReconnectMaxDelay = 60 * time.Second

// Config holds client configuration. Aligns with Node.js SDK options.
type Config struct {
	// BaseURL is the REST API base URL. Default: https://api.mexc.com
	BaseURL string
	// APIKey is the MEXC API access key (for private endpoints).
	APIKey string
	// APISecret is the MEXC API secret key (for signing).
	APISecret string
	// Timeout is the HTTP request timeout.
	Timeout time.Duration
	// RecvWindow is the signature time window in milliseconds (max 60000; docs recommend <= 30000).
	RecvWindow int64
	// UserAgent is an optional custom User-Agent header.
	UserAgent string
	// Debug when true logs method, URL, headers (secret redacted), request/response body.
	Debug bool
	// WSURL is the WebSocket endpoint URL. Default: wss://contract.mexc.com/edge
	WSURL string
	// WSReconnect when true enables automatic reconnect on unexpected close.
	WSReconnect bool
	// WSReconnectInitialDelay is the initial delay before reconnecting.
	WSReconnectInitialDelay time.Duration
	// WSReconnectMaxDelay is the maximum delay between reconnect attempts.
	WSReconnectMaxDelay time.Duration
	// WSPingInterval is the interval between ping messages.
	WSPingInterval time.Duration
}

// DefaultConfig returns a config with defaults matching the Node.js SDK.
func DefaultConfig() Config {
	return Config{
		BaseURL:                 DefaultBaseURL,
		Timeout:                 DefaultTimeout,
		RecvWindow:              DefaultRecvWindowMs,
		WSURL:                   DefaultWSURL,
		WSReconnect:             true,
		WSReconnectInitialDelay: DefaultWSReconnectInitialDelay,
		WSReconnectMaxDelay:     DefaultWSReconnectMaxDelay,
		WSPingInterval:          DefaultWSPingInterval,
	}
}

// WithAPIKey sets API key and secret and returns the config (for chaining).
func (c *Config) WithAPIKey(key, secret string) *Config {
	c.APIKey = key
	c.APISecret = secret
	return c
}
