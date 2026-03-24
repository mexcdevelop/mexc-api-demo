using System.Text.Json;
namespace Mexc.Client
{
    /// <summary>
    /// MEXC Futures Market Data API
    /// All endpoints are public and do not require authentication.
    /// </summary>
    public class MexcMarketDataApi : MexcHttpClient
    {
        public MexcMarketDataApi(ILogger logger = null) : base(null, null, logger)
        {
        }

        /// <summary>
        /// Get Server Time (Ping)
        /// GET /api/v1/contract/ping
        /// </summary>
        public async Task<JsonDocument> PingAsync()
        {
            _logger?.LogInformation("\n=== Ping - Get Server Time ===");
            var response = await GetAsync("/api/v1/contract/ping");
            
            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    _logger?.LogInformation($"Server timestamp: {data}");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Contract Information
        /// GET /api/v1/contract/detail
        /// </summary>
        public async Task<JsonDocument> GetContractDetailAsync(string symbol = null)
        {
            _logger?.LogInformation($"\n=== Get Contract Information {(symbol ?? "(All)")} ===");

            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol))
            {
                paramsDict["symbol"] = symbol;
            }

            var response = await GetAsync("/api/v1/contract/detail", paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (!string.IsNullOrEmpty(symbol))
                {
                    if (response.RootElement.TryGetProperty("data", out var data))
                    {
                        _logger?.LogInformation($"Symbol: {data.GetProperty("symbol")}");
                        _logger?.LogInformation($"Base Coin: {data.GetProperty("baseCoin")}");
                        _logger?.LogInformation($"Quote Coin: {data.GetProperty("quoteCoin")}");
                        _logger?.LogInformation($"Contract Size: {data.GetProperty("contractSize")}");
                        _logger?.LogInformation($"Max Leverage: {data.GetProperty("maxLeverage")}");
                    }
                }
                else
                {
                    if (response.RootElement.TryGetProperty("data", out var data) && data.ValueKind == JsonValueKind.Array)
                    {
                        _logger?.LogInformation($"Retrieved {data.GetArrayLength()} contracts");
                    }
                }
            }
            return response;
        }

        /// <summary>
        /// Get Transferable Currencies
        /// GET /api/v1/contract/support_currencies
        /// </summary>
        public async Task<JsonDocument> GetSupportCurrenciesAsync()
        {
            _logger?.LogInformation("\n=== Get Transferable Currencies ===");
            var response = await GetAsync("/api/v1/contract/support_currencies");

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data) && data.ValueKind == JsonValueKind.Array)
                {
                    _logger?.LogInformation($"Supported currencies: {data.GetArrayLength()}");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Order Book Depth
        /// GET /api/v1/contract/depth/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetDepthAsync(string symbol, int? limit = null)
        {
            _logger?.LogInformation($"\n=== Get Order Book Depth: {symbol} ===");

            var endpoint = $"/api/v1/contract/depth/{symbol}";
            var paramsDict = new Dictionary<string, string>();
            if (limit.HasValue)
            {
                paramsDict["limit"] = limit.ToString();
            }

            var response = await GetAsync(endpoint, paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("version", out var version))
                        _logger?.LogInformation($"Order Book Version: {version}");
                    
                    if (data.TryGetProperty("timestamp", out var timestamp))
                        _logger?.LogInformation($"Timestamp: {timestamp}");
                    
                    if (data.TryGetProperty("bids", out var bids) && bids.ValueKind == JsonValueKind.Array)
                        _logger?.LogInformation($"Bids: {bids.GetArrayLength()} levels");
                    
                    if (data.TryGetProperty("asks", out var asks) && asks.ValueKind == JsonValueKind.Array)
                        _logger?.LogInformation($"Asks: {asks.GetArrayLength()} levels");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Last N Depth Snapshots
        /// GET /api/v1/contract/depth_commits/{symbol}/{limit}
        /// </summary>
        public async Task<JsonDocument> GetDepthCommitsAsync(string symbol, int limit)
        {
            _logger?.LogInformation($"\n=== Get Last {limit} Depth Snapshots: {symbol} ===");

            var endpoint = $"/api/v1/contract/depth_commits/{symbol}/{limit}";
            var response = await GetAsync(endpoint);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data) && data.ValueKind == JsonValueKind.Array)
                {
                    _logger?.LogInformation($"Retrieved {data.GetArrayLength()} snapshots");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Index Price
        /// GET /api/v1/contract/index_price/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetIndexPriceAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Index Price: {symbol} ===");

            var endpoint = $"/api/v1/contract/index_price/{symbol}";
            var response = await GetAsync(endpoint);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("indexPrice", out var indexPrice))
                        _logger?.LogInformation($"Index Price: {indexPrice}");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Fair Price
        /// GET /api/v1/contract/fair_price/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetFairPriceAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Fair Price: {symbol} ===");

            var endpoint = $"/api/v1/contract/fair_price/{symbol}";
            var response = await GetAsync(endpoint);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("fairPrice", out var fairPrice))
                        _logger?.LogInformation($"Fair Price: {fairPrice}");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Funding Rate
        /// GET /api/v1/contract/funding_rate/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetFundingRateAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Funding Rate: {symbol} ===");

            var endpoint = $"/api/v1/contract/funding_rate/{symbol}";
            var response = await GetAsync(endpoint);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("fundingRate", out var fundingRate))
                        _logger?.LogInformation($"Funding Rate: {fundingRate}");
                    
                    if (data.TryGetProperty("nextSettleTime", out var nextSettleTime))
                        _logger?.LogInformation($"Next Settlement: {nextSettleTime}");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Kline/Candlestick Data
        /// GET /api/v1/contract/kline/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetKlineAsync(string symbol, string interval, long? start = null, long? end = null, int? limit = null)
        {
            _logger?.LogInformation($"\n=== Get Kline Data: {symbol} {interval} ===");

            var endpoint = $"/api/v1/contract/kline/{symbol}";
            var paramsDict = new Dictionary<string, string> { { "interval", interval } };

            if (start.HasValue) paramsDict["start"] = start.ToString();
            if (end.HasValue) paramsDict["end"] = end.ToString();
            if (limit.HasValue) paramsDict["limit"] = limit.ToString();

            var response = await GetAsync(endpoint, paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var klineData))
                {
                    _logger?.LogInformation($"Retrieved kline records");
                    if (klineData.TryGetProperty("close", out var closeArray) && closeArray.ValueKind == JsonValueKind.Array)
                    {
                        if (closeArray.GetArrayLength() > 0)
                            _logger?.LogInformation($"First close: {closeArray[0]}");
                    }
                }
            }
            return response;
        }

        /// <summary>
        /// Get Index Price Kline Data
        /// GET /api/v1/contract/kline/index_price/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetIndexPriceKlineAsync(string symbol, string interval, long start, long end)
        {
            _logger?.LogInformation($"\n=== Get Index Price Kline: {symbol} {interval} ===");
            _logger?.LogInformation($"Time range: {start} to {end}");

            var endpoint = $"/api/v1/contract/kline/index_price/{symbol}";
            var paramsDict = new Dictionary<string, string>
            {
                { "interval", interval },
                { "start", start.ToString() },
                { "end", end.ToString() }
            };

            var response = await GetAsync(endpoint, paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("time", out var timeArray) && timeArray.ValueKind == JsonValueKind.Array)
                    {
                        _logger?.LogInformation($"Retrieved {timeArray.GetArrayLength()} index price kline records");
                    }
                }
            }
            return response;
        }

        /// <summary>
        /// Get Fair Price Kline Data
        /// GET /api/v1/contract/kline/fair_price/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetFairPriceKlineAsync(string symbol, string interval, long start, long end)
        {
            _logger?.LogInformation($"\n=== Get Fair Price Kline: {symbol} {interval} ===");
            _logger?.LogInformation($"Time range: {start} to {end}");

            var endpoint = $"/api/v1/contract/kline/fair_price/{symbol}";
            var paramsDict = new Dictionary<string, string>
            {
                { "interval", interval },
                { "start", start.ToString() },
                { "end", end.ToString() }
            };

            var response = await GetAsync(endpoint, paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("time", out var timeArray) && timeArray.ValueKind == JsonValueKind.Array)
                    {
                        _logger?.LogInformation($"Retrieved {timeArray.GetArrayLength()} fair price kline records");
                    }
                }
            }
            return response;
        }

        /// <summary>
        /// Get Recent Deals/Trades
        /// GET /api/v1/contract/deals/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetRecentDealsAsync(string symbol, int? limit = null)
        {
            _logger?.LogInformation($"\n=== Get Recent Deals: {symbol} ===");

            var endpoint = $"/api/v1/contract/deals/{symbol}";
            var paramsDict = new Dictionary<string, string>();
            if (limit.HasValue)
            {
                paramsDict["limit"] = limit.ToString();
            }

            var response = await GetAsync(endpoint, paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data) && data.ValueKind == JsonValueKind.Array)
                {
                    _logger?.LogInformation($"Retrieved {data.GetArrayLength()} recent deals");
                }
            }
            return response;
        }

        /// <summary>
        /// Get All Tickers
        /// GET /api/v1/contract/ticker
        /// </summary>
        public async Task<JsonDocument> GetAllTickersAsync()
        {
            _logger?.LogInformation("\n=== Get All Tickers ===");

            var response = await GetAsync("/api/v1/contract/ticker");

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data) && data.ValueKind == JsonValueKind.Array)
                {
                    _logger?.LogInformation($"Retrieved tickers for {data.GetArrayLength()} symbols");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Risk Reverse by Symbol
        /// GET /api/v1/contract/risk_reverse/{symbol}
        /// </summary>
        public async Task<JsonDocument> GetRiskReverseBySymbolAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Risk Reverse: {symbol} ===");

            var endpoint = $"/api/v1/contract/risk_reverse/{symbol}";
            var response = await GetAsync(endpoint);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("symbol", out var sym))
                        _logger?.LogInformation($"Symbol: {sym}");
                    
                    if (data.TryGetProperty("available", out var available))
                        _logger?.LogInformation($"Risk Reverse Amount: {available}");
                    
                    if (data.TryGetProperty("timestamp", out var timestamp))
                        _logger?.LogInformation($"Update Time: {timestamp}");
                }
            }
            return response;
        }

        /// <summary>
        /// Get Risk Reverse History
        /// GET /api/v1/contract/risk_reverse/history
        /// </summary>
        public async Task<JsonDocument> GetRiskReverseHistoryAsync(string symbol = null, int pageNum = 1, int pageSize = 20)
        {
            _logger?.LogInformation($"\n=== Get Risk Reverse History {(symbol ?? "")} ===");

            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol))
            {
                paramsDict["symbol"] = symbol;
            }
            paramsDict["page_num"] = pageNum.ToString();
            paramsDict["page_size"] = Math.Min(pageSize, 100).ToString();

            var response = await GetAsync("/api/v1/contract/risk_reverse/history", paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("totalCount", out var totalCount))
                        _logger?.LogInformation($"Total records: {totalCount}");
                    
                    if (data.TryGetProperty("currentPage", out var currentPage))
                        _logger?.LogInformation($"Current page: {currentPage}");
                    
                    if (data.TryGetProperty("pageSize", out var pageSizeProp))
                        _logger?.LogInformation($"Page size: {pageSizeProp}");

                    if (data.TryGetProperty("resultList", out var resultList) && resultList.ValueKind == JsonValueKind.Array)
                    {
                        _logger?.LogInformation($"Records in this page: {resultList.GetArrayLength()}");
                    }
                }
            }
            return response;
        }

        /// <summary>
        /// Get Funding Rate History
        /// GET /api/v1/contract/funding_rate/history
        /// </summary>
        public async Task<JsonDocument> GetFundingRateHistoryAsync(string symbol = null, int? limit = null)
        {
            _logger?.LogInformation($"\n=== Get Funding Rate History {(symbol ?? "(All)")} ===");

            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol))
            {
                paramsDict["symbol"] = symbol;
            }
            if (limit.HasValue)
            {
                paramsDict["limit"] = limit.ToString();
            }

            var response = await GetAsync("/api/v1/contract/funding_rate/history", paramsDict);

            if (response != null && response.RootElement.TryGetProperty("success", out var success) && success.GetBoolean())
            {
                if (response.RootElement.TryGetProperty("data", out var data))
                {
                    if (data.TryGetProperty("resultList", out var resultList) && resultList.ValueKind == JsonValueKind.Array)
                    {
                        _logger?.LogInformation($"Retrieved {resultList.GetArrayLength()} funding rate records");
                    }
                }
            }
            return response;
        }
    }
}