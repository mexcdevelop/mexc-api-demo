package mexcfutures

import (
	"bytes"
	"compress/gzip"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net"
	"net/http"
	"sync"
	"time"

	"github.com/gorilla/websocket"
)

// WSClient is a lightweight WebSocket client for MEXC Futures.
// It mirrors the behavior of the Node.js MexcFuturesWsClient for practical usage.
type WSClient struct {
	cfg Config

	dialer *websocket.Dialer

	mu                sync.Mutex
	writeMu           sync.Mutex
	conn              *websocket.Conn
	manualDisconnect  bool
	reconnectAttempts int
	reconnecting      bool

	pingTicker *time.Ticker

	onOpen      []func()
	onClose     []func(code int, text string)
	onError     []func(error)
	onMessage   []func(raw string, msg map[string]any)
	onReconnect []func()
	onPong      []func(data any)
}

// NewWSClient creates a new WebSocket client using the given config.
func NewWSClient(cfg Config) *WSClient {
	return &WSClient{
		cfg: cfg,
		dialer: &websocket.Dialer{
			Proxy:            http.ProxyFromEnvironment,
			HandshakeTimeout: 10 * time.Second,
			NetDialContext:   (&net.Dialer{Timeout: 10 * time.Second}).DialContext,
		},
	}
}

// Connect establishes a WebSocket connection and starts the read loop.
// It is strictly controlled by the provided context and returns when ctx completes or dial finishes.
func (c *WSClient) Connect(ctx context.Context) error {
	// Fast path: if already connected, do nothing.
	c.mu.Lock()
	if c.conn != nil {
		c.mu.Unlock()
		return nil
	}
	wsURL := c.cfg.WSURL
	if wsURL == "" {
		wsURL = DefaultWSURL
	}
	c.mu.Unlock()

	type dialResult struct {
		conn *websocket.Conn
		resp *http.Response
		err  error
	}
	done := make(chan dialResult, 1)
	go func() {
		fmt.Println("ws connect: dial start", wsURL)
		header := http.Header{}
		header.Set("Origin", "https://contract.mexc.com")
		header.Set("User-Agent", "mexc-futures-go")
		conn, resp, err := c.dialer.DialContext(ctx, wsURL, header)
		if err != nil {
			fmt.Println("ws connect: dial error", err)
		} else {
			fmt.Println("ws connect: dial success")
		}
		done <- dialResult{conn: conn, resp: resp, err: err}
	}()

	var conn *websocket.Conn
	select {
	case r := <-done:
		if r.err != nil {
			return r.err
		}
		conn = r.conn
	case <-ctx.Done():
		fmt.Println("ws connect: ctx.Done()", ctx.Err())
		return fmt.Errorf("ws connect timeout: %w", ctx.Err())
	}

	// Save connection state under lock.
	c.mu.Lock()
	// If another goroutine connected first while we were dialing, close this conn and return.
	if c.conn != nil {
		c.mu.Unlock()
		_ = conn.Close()
		return nil
	}
	c.conn = conn
	c.manualDisconnect = false

	// Reset reconnect attempts on successful connect.
	c.reconnectAttempts = 0

	c.startPingLocked()
	c.mu.Unlock()

	fmt.Println("ws connect: start reader")
	go c.readLoop(conn)

	fmt.Println("ws connect: onOpen fired")
	c.emitOpen()

	return nil
}

// Disconnect closes the WebSocket connection and stops reconnect attempts.
func (c *WSClient) Disconnect() {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.manualDisconnect = true
	c.stopPingLocked()
	if c.conn != nil {
		_ = c.conn.Close()
		c.conn = nil
	}
}

// Send sends a value as JSON or string (if already string).
func (c *WSClient) Send(v any) error {
	c.mu.Lock()
	conn := c.conn
	c.mu.Unlock()

	if conn == nil {
		return fmt.Errorf("websocket not connected")
	}

	c.writeMu.Lock()
	defer c.writeMu.Unlock()

	var msg string
	switch x := v.(type) {
	case string:
		msg = x
	default:
		b, err := json.Marshal(v)
		if err != nil {
			return err
		}
		msg = string(b)
	}
	return conn.WriteMessage(websocket.TextMessage, []byte(msg))
}

// SendText sends a raw text message.
func (c *WSClient) SendText(s string) error {
	return c.Send(s)
}

// Ping sends a ping message (JSON ping used by MEXC Futures).
func (c *WSClient) Ping() error {
	return c.Send(map[string]any{"method": "ping"})
}

// Subscribe sends a generic subscribe message.
func (c *WSClient) Subscribe(method string, param map[string]any) error {
	if param == nil {
		param = map[string]any{}
	}
	return c.Send(map[string]any{"method": method, "param": param})
}

// Unsubscribe sends a generic unsubscribe message.
func (c *WSClient) Unsubscribe(method string, param map[string]any) error {
	if param == nil {
		param = map[string]any{}
	}
	return c.Send(map[string]any{"method": method, "param": param})
}

// --- public channel helpers ---

// SubTickers subscribes to all tickers.
// @wsChannel sub.tickers
func (c *WSClient) SubTickers(params map[string]any) error {
	return c.Subscribe("sub.tickers", params)
}

// UnsubTickers unsubscribes from all tickers.
// @wsChannel unsub.tickers
func (c *WSClient) UnsubTickers(params map[string]any) error {
	return c.Unsubscribe("unsub.tickers", params)
}

// SubTicker subscribes to a single ticker.
// @wsChannel sub.ticker
func (c *WSClient) SubTicker(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.ticker", p)
}

// UnsubTicker unsubscribes from a single ticker.
// @wsChannel unsub.ticker
func (c *WSClient) UnsubTicker(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.ticker", p)
}

// SubDeal subscribes to deals.
// @wsChannel sub.deal
func (c *WSClient) SubDeal(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.deal", p)
}

// UnsubDeal unsubscribes from deals.
// @wsChannel unsub.deal
func (c *WSClient) UnsubDeal(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.deal", p)
}

// SubDepth subscribes to incremental depth.
// @wsChannel sub.depth
func (c *WSClient) SubDepth(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.depth", p)
}

// UnsubDepth unsubscribes from incremental depth.
// @wsChannel unsub.depth
func (c *WSClient) UnsubDepth(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.depth", p)
}

// SubDepthFull subscribes to full depth.
// @wsChannel sub.depth.full
func (c *WSClient) SubDepthFull(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.depth.full", p)
}

// UnsubDepthFull unsubscribes from full depth.
// @wsChannel unsub.depth.full
func (c *WSClient) UnsubDepthFull(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.depth.full", p)
}

// SubKline subscribes to K-line.
// @wsChannel sub.kline
func (c *WSClient) SubKline(params map[string]any) error {
	p := toSymbolParam(params)
	if getStringFromAny(p["interval"]) == "" {
		return fmt.Errorf("params.interval required for SubKline")
	}
	return c.Subscribe("sub.kline", p)
}

// UnsubKline unsubscribes from K-line.
// @wsChannel unsub.kline
func (c *WSClient) UnsubKline(params map[string]any) error {
	p := toSymbolParam(params)
	if getStringFromAny(p["interval"]) == "" {
		return fmt.Errorf("params.interval required for UnsubKline")
	}
	return c.Unsubscribe("unsub.kline", p)
}

// SubFundingRate subscribes to funding rate.
// @wsChannel sub.funding.rate
func (c *WSClient) SubFundingRate(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.funding.rate", p)
}

// UnsubFundingRate unsubscribes from funding rate.
// @wsChannel unsub.funding.rate
func (c *WSClient) UnsubFundingRate(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.funding.rate", p)
}

// SubIndexPrice subscribes to index price.
// @wsChannel sub.index.price
func (c *WSClient) SubIndexPrice(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.index.price", p)
}

// UnsubIndexPrice unsubscribes from index price.
// @wsChannel unsub.index.price
func (c *WSClient) UnsubIndexPrice(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.index.price", p)
}

// SubFairPrice subscribes to fair price.
// @wsChannel sub.fair.price
func (c *WSClient) SubFairPrice(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Subscribe("sub.fair.price", p)
}

// UnsubFairPrice unsubscribes from fair price.
// @wsChannel unsub.fair.price
func (c *WSClient) UnsubFairPrice(params map[string]any) error {
	p := toSymbolParam(params)
	return c.Unsubscribe("unsub.fair.price", p)
}

// SubContract subscribes to contract data.
// @wsChannel sub.contract
func (c *WSClient) SubContract() error {
	return c.Subscribe("sub.contract", map[string]any{})
}

// UnsubContract unsubscribes from contract data.
// @wsChannel unsub.contract
func (c *WSClient) UnsubContract() error {
	return c.Unsubscribe("unsub.contract", map[string]any{})
}

// SubEventContract subscribes to event contract channel.
// @wsChannel sub.event.contract
func (c *WSClient) SubEventContract() error {
	return c.Subscribe("sub.event.contract", map[string]any{})
}

// UnsubEventContract unsubscribes from event contract channel.
// @wsChannel unsub.event.contract
func (c *WSClient) UnsubEventContract() error {
	return c.Unsubscribe("unsub.event.contract", map[string]any{})
}

// --- private login ---

// Login sends a private login message using REST signing rules.
// When subscribe is false, personal.filter is used to control private pushes.
// @wsChannel login
func (c *WSClient) Login(subscribe bool) error {
	if c.cfg.APIKey == "" || c.cfg.APISecret == "" {
		return fmt.Errorf("apiKey and apiSecret required for login")
	}
	reqTime := fmt.Sprintf("%d", time.Now().UnixMilli())
	// Futures WS login signs the same way as REST GET with empty params.
	sig, err := Sign(c.cfg.APIKey, c.cfg.APISecret, reqTime, "GET", map[string]interface{}{})
	if err != nil {
		return err
	}
	msg := map[string]any{
		"method": "login",
		"param": map[string]any{
			"apiKey":    c.cfg.APIKey,
			"reqTime":   reqTime,
			"signature": sig,
		},
	}
	if !subscribe {
		msg["subscribe"] = false
	}
	return c.Send(msg)
}

// --- personal.filter helpers ---

// FilterAssets enables asset-only pushes via personal.filter.
// @wsChannel personal.filter
func (c *WSClient) FilterAssets() error {
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param": map[string]any{
			"filters": []map[string]any{
				{"filter": "asset"},
			},
		},
	})
}

// FilterOrders enables order pushes with optional symbol rules.
// @wsChannel personal.filter
func (c *WSClient) FilterOrders(params map[string]any) error {
	rules, err := normalizeSymbolsParam(params)
	if err != nil {
		return err
	}
	f := map[string]any{"filter": "order"}
	if len(rules) > 0 {
		f["rules"] = rules
	}
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []map[string]any{f}},
	})
}

// FilterOrderDeals enables order deal pushes with optional symbol rules.
// @wsChannel personal.filter
func (c *WSClient) FilterOrderDeals(params map[string]any) error {
	rules, err := normalizeSymbolsParam(params)
	if err != nil {
		return err
	}
	f := map[string]any{"filter": "order.deal"}
	if len(rules) > 0 {
		f["rules"] = rules
	}
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []map[string]any{f}},
	})
}

// FilterPositions enables position pushes with optional symbol rules.
// @wsChannel personal.filter
func (c *WSClient) FilterPositions(params map[string]any) error {
	rules, err := normalizeSymbolsParam(params)
	if err != nil {
		return err
	}
	f := map[string]any{"filter": "position"}
	if len(rules) > 0 {
		f["rules"] = rules
	}
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []map[string]any{f}},
	})
}

// FilterPlanOrders enables plan order pushes with optional symbol rules.
// @wsChannel personal.filter
func (c *WSClient) FilterPlanOrders(params map[string]any) error {
	rules, err := normalizeSymbolsParam(params)
	if err != nil {
		return err
	}
	f := map[string]any{"filter": "plan.order"}
	if len(rules) > 0 {
		f["rules"] = rules
	}
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []map[string]any{f}},
	})
}

// FilterStopOrders enables stop order pushes with optional symbol rules.
// @wsChannel personal.filter
func (c *WSClient) FilterStopOrders(params map[string]any) error {
	rules, err := normalizeSymbolsParam(params)
	if err != nil {
		return err
	}
	f := map[string]any{"filter": "stop.order"}
	if len(rules) > 0 {
		f["rules"] = rules
	}
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []map[string]any{f}},
	})
}

// FilterStopPlanOrders enables stop-planorder pushes with optional symbol rules.
// @wsChannel personal.filter
func (c *WSClient) FilterStopPlanOrders(params map[string]any) error {
	rules, err := normalizeSymbolsParam(params)
	if err != nil {
		return err
	}
	f := map[string]any{"filter": "stop.planorder"}
	if len(rules) > 0 {
		f["rules"] = rules
	}
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []map[string]any{f}},
	})
}

// FilterRiskLimit enables risk limit pushes.
// @wsChannel personal.filter
func (c *WSClient) FilterRiskLimit() error {
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param": map[string]any{
			"filters": []map[string]any{
				{"filter": "risk.limit"},
			},
		},
	})
}

// FilterAdlLevel enables ADL level pushes.
// @wsChannel personal.filter
func (c *WSClient) FilterAdlLevel() error {
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param": map[string]any{
			"filters": []map[string]any{
				{"filter": "adl.level"},
			},
		},
	})
}

// ResetPersonalFilters resets personal.filter to restore all private pushes.
// @wsChannel personal.filter
func (c *WSClient) ResetPersonalFilters() error {
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  map[string]any{"filters": []any{}},
	})
}

// FilterCustom sends custom personal.filter rules.
// @wsChannel personal.filter
func (c *WSClient) FilterCustom(params map[string]any) error {
	return c.Send(map[string]any{
		"method": "personal.filter",
		"param":  params,
	})
}

// --- event handlers registration ---

// OnOpen registers a callback for successful connection open.
func (c *WSClient) OnOpen(fn func()) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.onOpen = append(c.onOpen, fn)
}

// OnClose registers a callback for close events.
func (c *WSClient) OnClose(fn func(code int, text string)) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.onClose = append(c.onClose, fn)
}

// OnError registers a callback for error events.
func (c *WSClient) OnError(fn func(error)) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.onError = append(c.onError, fn)
}

// OnMessage registers a callback for incoming messages.
func (c *WSClient) OnMessage(fn func(raw string, msg map[string]any)) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.onMessage = append(c.onMessage, fn)
}

// OnReconnect registers a callback for reconnect attempts.
func (c *WSClient) OnReconnect(fn func()) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.onReconnect = append(c.onReconnect, fn)
}

// OnPong registers a callback for pong events.
func (c *WSClient) OnPong(fn func(data any)) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.onPong = append(c.onPong, fn)
}

// --- internal loops and helpers ---

func (c *WSClient) readLoop(conn *websocket.Conn) {
	for {
		mt, data, err := conn.ReadMessage()
		if err != nil {
			c.handleReadError(err)
			return
		}
		raw := ""
		switch mt {
		case websocket.TextMessage:
			raw = string(data)
		case websocket.BinaryMessage:
			// Futures WS may compress some channels with gzip.
			// TODO: Confirm compression format and update if necessary.
			if s, err := gunzip(data); err == nil {
				raw = s
			} else {
				raw = string(data)
			}
		default:
			continue
		}

		var obj map[string]any
		if err := json.Unmarshal([]byte(raw), &obj); err != nil {
			c.emitError(fmt.Errorf("websocket message parse error: %w", err))
			continue
		}

		channel, _ := obj["channel"].(string)
		if channel == "pong" {
			c.emitPong(obj["data"])
			continue
		}
		if channel == "rs.login" || channel == "rs.error" {
			c.emitMessage(raw, obj)
			if channel == "rs.error" {
				c.emitError(fmt.Errorf("ws error: %v", obj["data"]))
			}
			continue
		}

		c.emitMessage(raw, obj)
	}
}

func (c *WSClient) handleReadError(err error) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.stopPingLocked()
	if c.conn != nil {
		_ = c.conn.Close()
		c.conn = nil
	}
	c.emitError(err)
	c.emitClose(0, err.Error())
	c.scheduleReconnectLocked()
}

func (c *WSClient) startPingLocked() {
	c.stopPingLocked()
	interval := c.cfg.WSPingInterval
	if interval <= 0 {
		interval = DefaultWSPingInterval
	}
	c.pingTicker = time.NewTicker(interval)
	go func() {
		for range c.pingTicker.C {
			_ = c.Ping()
		}
	}()
}

func (c *WSClient) stopPingLocked() {
	if c.pingTicker != nil {
		c.pingTicker.Stop()
		c.pingTicker = nil
	}
}

func (c *WSClient) scheduleReconnectLocked() {
	if c.manualDisconnect || !c.cfg.WSReconnect {
		return
	}
	if c.reconnecting {
		return
	}
	c.reconnecting = true
	attempt := c.reconnectAttempts
	c.reconnectAttempts++

	initial := c.cfg.WSReconnectInitialDelay
	if initial <= 0 {
		initial = DefaultWSReconnectInitialDelay
	}
	maxDelay := c.cfg.WSReconnectMaxDelay
	if maxDelay <= 0 {
		maxDelay = DefaultWSReconnectMaxDelay
	}
	delay := initial * time.Duration(1<<attempt)
	if delay > maxDelay {
		delay = maxDelay
	}

	go func() {
		time.Sleep(delay)
		c.mu.Lock()
		c.reconnecting = false
		if c.manualDisconnect {
			c.mu.Unlock()
			return
		}
		c.mu.Unlock()

		c.emitReconnect()
		ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
		defer cancel()
		_ = c.Connect(ctx)
	}()
}

func (c *WSClient) emitOpen() {
	c.mu.Lock()
	handlers := append([]func(){}, c.onOpen...)
	c.mu.Unlock()
	for _, h := range handlers {
		h()
	}
}

func (c *WSClient) emitClose(code int, text string) {
	c.mu.Lock()
	handlers := append([]func(int, string){}, c.onClose...)
	c.mu.Unlock()
	for _, h := range handlers {
		h(code, text)
	}
}

func (c *WSClient) emitError(err error) {
	c.mu.Lock()
	handlers := append([]func(error){}, c.onError...)
	c.mu.Unlock()
	for _, h := range handlers {
		h(err)
	}
}

func (c *WSClient) emitMessage(raw string, msg map[string]any) {
	c.mu.Lock()
	handlers := append([]func(string, map[string]any){}, c.onMessage...)
	c.mu.Unlock()
	for _, h := range handlers {
		h(raw, msg)
	}
}

func (c *WSClient) emitReconnect() {
	c.mu.Lock()
	handlers := append([]func(){}, c.onReconnect...)
	c.mu.Unlock()
	for _, h := range handlers {
		h()
	}
}

func (c *WSClient) emitPong(data any) {
	c.mu.Lock()
	handlers := append([]func(any){}, c.onPong...)
	c.mu.Unlock()
	for _, h := range handlers {
		h(data)
	}
}

// --- small helpers for params and decoding ---

func toSymbolParam(params map[string]any) map[string]any {
	if params == nil {
		return map[string]any{}
	}
	if s, ok := params["symbol"].(string); ok && s != "" {
		return params
	}
	return params
}

func getStringFromAny(v any) string {
	if v == nil {
		return ""
	}
	if s, ok := v.(string); ok {
		return s
	}
	return fmt.Sprintf("%v", v)
}

func normalizeSymbolsParam(params map[string]any) ([]string, error) {
	if params == nil {
		return nil, nil
	}
	if v, ok := params["symbols"]; ok {
		switch x := v.(type) {
		case []string:
			return normalizeSymbols(x)
		case []any:
			out := make([]string, 0, len(x))
			for _, item := range x {
				if s, ok := item.(string); ok && s != "" {
					out = append(out, s)
				}
			}
			return normalizeSymbols(out)
		default:
			return nil, fmt.Errorf("symbols must be string array")
		}
	}
	return nil, nil
}

func normalizeSymbols(symbols []string) ([]string, error) {
	if symbols == nil {
		return nil, nil
	}
	out := make([]string, 0, len(symbols))
	for _, s := range symbols {
		if s == "" {
			continue
		}
		out = append(out, s)
	}
	if len(out) == 0 {
		return nil, nil
	}
	for _, s := range out {
		if s == "" {
			return nil, fmt.Errorf("symbols must be non-empty strings")
		}
	}
	return out, nil
}

func gunzip(data []byte) (string, error) {
	r, err := gzip.NewReader(bytes.NewReader(data))
	if err != nil {
		return "", err
	}
	defer r.Close()
	b, err := io.ReadAll(r)
	if err != nil {
		return "", err
	}
	return string(b), nil
}
