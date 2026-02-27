package mexcfutures

import (
	"errors"
	"fmt"
)

// ErrSymbolRequired is returned when a required symbol parameter is missing or empty.
var ErrSymbolRequired = errors.New("symbol required and must be non-empty string")

// APIError represents an error response from the MEXC Futures API.
// When success=false or code!=0, the client returns this so callers can inspect HTTP status, code, message and raw body.
type APIError struct {
	HTTPStatus int    // HTTP response status code
	Code       int    // API response code (e.g. 600 Parameter error)
	Message    string // API message if present
	RawBody    []byte // Full response body for debugging
}

func (e *APIError) Error() string {
	if e.Message != "" {
		return fmt.Sprintf("mexc futures api: http=%d code=%d message=%s", e.HTTPStatus, e.Code, e.Message)
	}
	return fmt.Sprintf("mexc futures api: http=%d code=%d", e.HTTPStatus, e.Code)
}
