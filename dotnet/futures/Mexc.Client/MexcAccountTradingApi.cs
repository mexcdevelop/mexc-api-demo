using System.Text.Json;
namespace Mexc.Client
{
    /// <summary>
    /// MEXC Futures Account and Trading API
    /// All endpoints require authentication.
    /// </summary>
    public class MexcAccountTradingApi : MexcHttpClient
    {
        public MexcAccountTradingApi(string apiKey, string secretKey, ILogger logger = null) 
            : base(apiKey, secretKey, logger)
        {
        }

        #region Account Assets Endpoints

        /// <summary>
        /// Get All Account Assets
        /// GET /api/v1/private/account/assets
        /// </summary>
        public async Task<JsonDocument> GetAllAccountAssetsAsync()
        {
            _logger?.LogInformation("\n=== Get All Account Assets ===");
            return await GetSignedAsync("/api/v1/private/account/assets");
        }

        /// <summary>
        /// Get Single Currency Asset Information
        /// GET /api/v1/private/account/asset/{currency}
        /// </summary>
        public async Task<JsonDocument> GetSingleAssetAsync(string currency)
        {
            _logger?.LogInformation($"\n=== Get Single Currency Asset: {currency} ===");
            return await GetSignedAsync($"/api/v1/private/account/asset/{currency}");
        }

        /// <summary>
        /// Get Asset Transfer Records
        /// GET /api/v1/private/account/transfer_record
        /// </summary>
        public async Task<JsonDocument> GetTransferRecordAsync(string currency = null, string state = null,
            string type = null, int pageNum = 1, int pageSize = 20)
        {
            _logger?.LogInformation("\n=== Get Asset Transfer Records ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(currency)) paramsDict["currency"] = currency;
            if (!string.IsNullOrEmpty(state)) paramsDict["state"] = state;
            if (!string.IsNullOrEmpty(type)) paramsDict["type"] = type;
            paramsDict["page_num"] = pageNum.ToString();
            paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/account/transfer_record", paramsDict);
        }

        /// <summary>
        /// View Personal Profit Rate
        /// GET /api/v1/private/account/profit_rate/{type}
        /// </summary>
        /// <param name="type">1: Daily, 2: Weekly</param>
        public async Task<JsonDocument> GetProfitRateAsync(int type)
        {
            _logger?.LogInformation($"\n=== Get Personal Profit Rate (type={type}) ===");
            return await GetSignedAsync($"/api/v1/private/account/profit_rate/{type}");
        }

        /// <summary>
        /// Asset Analysis
        /// GET /api/v1/private/account/asset/analysis/{type}
        /// </summary>
        /// <param name="type">1: This week, 2: This month, 3: All, 4: Custom time range</param>
        public async Task<JsonDocument> GetAssetAnalysisAsync(int type, string currency, 
            long? startTime = null, long? endTime = null)
        {
            _logger?.LogInformation($"\n=== Get Asset Analysis (type={type}) ===");
            var paramsDict = new Dictionary<string, string> { { "currency", currency } };
            if (startTime.HasValue) paramsDict["startTime"] = startTime.ToString();
            if (endTime.HasValue) paramsDict["endTime"] = endTime.ToString();

            return await GetSignedAsync($"/api/v1/private/account/asset/analysis/{type}", paramsDict);
        }

        /// <summary>
        /// Get Fee Deduction Configurations
        /// GET /api/v1/private/account/feeDeductConfigs
        /// </summary>
        public async Task<JsonDocument> GetFeeDeductConfigsAsync()
        {
            _logger?.LogInformation("\n=== Get Fee Deduction Configurations ===");
            return await GetSignedAsync("/api/v1/private/account/feeDeductConfigs");
        }

        /// <summary>
        /// Get Yesterday's PnL
        /// GET /api/v1/private/account/asset/analysis/yesterday_pnl
        /// </summary>
        public async Task<JsonDocument> GetYesterdayPnlAsync()
        {
            _logger?.LogInformation("\n=== Get Yesterday's PnL ===");
            return await GetSignedAsync("/api/v1/private/account/asset/analysis/yesterday_pnl");
        }

        /// <summary>
        /// User Asset Analysis V3
        /// GET /api/v1/private/account/asset/analysis/v3
        /// </summary>
        public async Task<JsonDocument> GetAssetAnalysisV3Async(long startTime, long endTime, 
            int? reverse = null, int? includeUnrealisedPnl = null, string symbol = null)
        {
            _logger?.LogInformation("\n=== Get Asset Analysis V3 ===");
            var paramsDict = new Dictionary<string, string>(){
                { "reverse", reverse.ToString() },
                { "includeUnrealisedPnl", includeUnrealisedPnl.ToString() },
                { "symbol", symbol.ToString() },
                { "startTime", startTime.ToString() },
                { "endTime", endTime.ToString() }
            };
            return await PostSignedAsync("/api/v1/private/account/asset/analysis/v3", paramsDict);
        }

        /// <summary>
        /// Daily Calendar Analysis V3
        /// GET /api/v1/private/account/asset/analysis/calendar/daily/v3
        /// </summary>
        public async Task<JsonDocument> GetCalendarDailyV3Async(long startTime, long endTime,
            int? reverse = null, int? includeUnrealisedPnl = null)
        {
            _logger?.LogInformation("\n=== Get Daily Calendar Analysis V3 ===");
            var paramsDict = new Dictionary<string, string>(){
                { "reverse", reverse.ToString() },
                { "includeUnrealisedPnl", includeUnrealisedPnl.ToString() },
                { "startTime", startTime.ToString() },
                { "endTime", endTime.ToString() }
            };
            
            return await PostSignedAsync("/api/v1/private/account/asset/analysis/calendar/daily/v3", paramsDict);
        }

        /// <summary>
        /// Monthly Calendar Analysis V3
        /// GET /api/v1/private/account/asset/analysis/calendar/monthly/v3
        /// </summary>
        public async Task<JsonDocument> GetCalendarMonthlyV3Async(int? reverse = null, int? includeUnrealisedPnl = null)
        {
            _logger?.LogInformation("\n=== Get Monthly Calendar Analysis V3 ===");
           var paramsDict = new Dictionary<string, string>(){
                { "reverse", reverse.ToString() },
                { "includeUnrealisedPnl", includeUnrealisedPnl.ToString() }
            };
            
            return await PostSignedAsync("/api/v1/private/account/asset/analysis/calendar/monthly/v3", paramsDict);
        }

        /// <summary>
        /// Recent User Asset Analysis V3
        /// GET /api/v1/private/account/asset/analysis/recent/v3
        /// </summary>
        public async Task<JsonDocument> GetRecentAnalysisV3Async(int? reverse = null, 
            int? includeUnrealisedPnl = null, string symbol = null)
        {
            _logger?.LogInformation("\n=== Get Recent Asset Analysis V3 ===");
            var paramsDict = new Dictionary<string, string>(){
                { "reverse", reverse.ToString() },
                { "includeUnrealisedPnl", includeUnrealisedPnl.ToString() },
                { "symbol", symbol.ToString() }
            };

            return await PostSignedAsync("/api/v1/private/account/asset/analysis/recent/v3", paramsDict);
        }

        /// <summary>
        /// Get Today's PnL
        /// GET /api/v1/private/account/asset/analysis/today_pnl
        /// </summary>
        public async Task<JsonDocument> GetTodayPnlAsync()
        {
            _logger?.LogInformation("\n=== Get Today's PnL ===");
            return await GetSignedAsync("/api/v1/private/account/asset/analysis/today_pnl");
        }

        /// <summary>
        /// Get Account Configuration (Contract Fee Discount)
        /// GET /api/v1/private/account/config/contractFeeDiscountConfig
        /// </summary>
        public async Task<JsonDocument> GetAccountConfigAsync()
        {
            _logger?.LogInformation("\n=== Get Account Configuration ===");
            return await GetSignedAsync("/api/v1/private/account/config/contractFeeDiscountConfig");
        }

        /// <summary>
        /// Get Order Fee Details
        /// GET /api/v1/private/order/fee_details
        /// </summary>
        public async Task<JsonDocument> GetOrderFeeDetailsAsync(string symbol, string orderId)
        {
            _logger?.LogInformation($"\n=== Get Order Fee Details: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "orderId", orderId }
            };
            return await GetSignedAsync("/api/v1/private/order/fee_details", paramsDict);
        }

        /// <summary>
        /// Get Account Discount Type
        /// GET /api/v1/private/account/discountType
        /// </summary>
        public async Task<JsonDocument> GetAccountDiscountTypeAsync()
        {
            _logger?.LogInformation("\n=== Get Account Discount Type ===");
            return await GetSignedAsync("/api/v1/private/account/discountType");
        }

        /// <summary>
        /// Export Asset Analysis
        /// GET /api/v1/private/account/asset/analysis/export
        /// </summary>
        public async Task<JsonDocument> ExportAssetAnalysisAsync(long startTime, long endTime, 
            string symbol = null, int? reverse = null, int? includeUnrealisedPnl = null)
        {
            _logger?.LogInformation("\n=== Export Asset Analysis ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "startTime", startTime.ToString() },
                { "endTime", endTime.ToString() }
            };
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;
            if (reverse.HasValue) paramsDict["reverse"] = reverse.ToString();
            if (includeUnrealisedPnl.HasValue) paramsDict["includeUnrealisedPnl"] = includeUnrealisedPnl.ToString();

            return await GetSignedAsync("/api/v1/private/account/asset/analysis/export", paramsDict);
        }

        /// <summary>
        /// Get Total Order Deal Fee
        /// GET /api/v1/private/account/asset_book/order_deal_fee/total
        /// </summary>
        public async Task<JsonDocument> GetTotalOrderDealFeeAsync(long startTime, long endTime, string symbol = null)
        {
            _logger?.LogInformation("\n=== Get Total Order Deal Fee ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "startTime", startTime.ToString() },
                { "endTime", endTime.ToString() }
            };
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;

            return await GetSignedAsync("/api/v1/private/account/asset_book/order_deal_fee/total", paramsDict);
        }

        /// <summary>
        /// Get Contract Fee Rate
        /// GET /api/v1/private/account/contract/fee_rate
        /// </summary>
        public async Task<JsonDocument> GetContractFeeRateAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Contract Fee Rate: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            return await GetSignedAsync("/api/v1/private/account/contract/fee_rate", paramsDict);
        }

        /// <summary>
        /// Get Zero Fee Rate Contracts
        /// GET /api/v1/private/account/contract/zero_fee_rate
        /// </summary>
        public async Task<JsonDocument> GetZeroFeeRateContractsAsync()
        {
            _logger?.LogInformation("\n=== Get Zero Fee Rate Contracts ===");
            return await GetSignedAsync("/api/v1/private/account/contract/zero_fee_rate");
        }

        #endregion

        #region Position Endpoints

        /// <summary>
        /// Get History Positions
        /// GET /api/v1/private/position/list/history_positions
        /// </summary>
        public async Task<JsonDocument> GetHistoryPositionsAsync(string symbol, long? startTime = null, 
            long? endTime = null, int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get History Positions: {symbol} ===");
            var paramsDict = new Dictionary<string, string>();
            if (startTime.HasValue) paramsDict["startTime"] = startTime.ToString();
            if (endTime.HasValue) paramsDict["endTime"] = endTime.ToString();
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/position/list/history_positions", paramsDict);
        }

        /// <summary>
        /// Get Open Positions
        /// GET /api/v1/private/position/open_positions
        /// </summary>
        public async Task<JsonDocument> GetOpenPositionsAsync(string symbol = null)
        {
            _logger?.LogInformation($"\n=== Get Open Positions {(symbol ?? "")} ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;

            return await GetSignedAsync("/api/v1/private/position/open_positions", paramsDict);
        }

        /// <summary>
        /// Get Funding Records
        /// GET /api/v1/private/position/funding_records
        /// </summary>
        public async Task<JsonDocument> GetFundingRecordsAsync(string symbol, long? startTime = null,
            long? endTime = null, int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get Funding Records: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            if (startTime.HasValue) paramsDict["startTime"] = startTime.ToString();
            if (endTime.HasValue) paramsDict["endTime"] = endTime.ToString();
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/position/funding_records", paramsDict);
        }

        /// <summary>
        /// Get Account Risk Limit
        /// GET /api/v1/private/account/risk_limit
        /// </summary>
        public async Task<JsonDocument> GetAccountRiskLimitAsync()
        {
            _logger?.LogInformation("\n=== Get Account Risk Limit ===");
            return await GetSignedAsync("/api/v1/private/account/risk_limit");
        }

        /// <summary>
        /// Get User Tiered Fee Rate V2
        /// GET /api/v1/private/account/tiered_fee_rate/v2
        /// </summary>
        public async Task<JsonDocument> GetTieredFeeRateV2Async()
        {
            _logger?.LogInformation("\n=== Get Tiered Fee Rate V2 ===");
            return await GetSignedAsync("/api/v1/private/account/tiered_fee_rate/v2");
        }

        /// <summary>
        /// Change Position Margin
        /// POST /api/v1/private/position/change_margin
        /// </summary>
        public async Task<JsonDocument> ChangePositionMarginAsync(string positionId, string amount, string type)
        {
            _logger?.LogInformation($"\n=== Change Position Margin: {positionId} {type} {amount} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "positionId", positionId },
                { "amount", amount },
                { "type", type }
            };
            return await PostSignedAsync("/api/v1/private/position/change_margin", paramsDict);
        }

        /// <summary>
        /// Change Auto Add Margin
        /// POST /api/v1/private/position/change_auto_add_im
        /// </summary>
        public async Task<JsonDocument> ChangeAutoAddMarginAsync(string positionId, bool autoAdd)
        {
            _logger?.LogInformation($"\n=== Change Auto Add Margin: {positionId} {autoAdd} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "positionId", positionId },
                { "isEnabled", autoAdd.ToString().ToLower() }
            };
            return await PostSignedAsync("/api/v1/private/position/change_auto_add_im", paramsDict);
        }

        /// <summary>
        /// Get Position Leverage
        /// GET /api/v1/private/position/leverage
        /// </summary>
        public async Task<JsonDocument> GetPositionLeverageAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Position Leverage: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            return await GetSignedAsync("/api/v1/private/position/leverage", paramsDict);
        }

        /// <summary>
        /// Change Leverage
        /// POST /api/v1/private/position/change_leverage
        /// </summary>
        public async Task<JsonDocument> ChangeLeverageAsync(string positionId = null, string symbol = null,
            int leverage = 10, string openType = "2", string positionType = "2")
        {
            _logger?.LogInformation($"\n=== Change Leverage: {positionId ?? symbol} {leverage}x ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(positionId)) paramsDict["positionId"] = positionId;
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;
            paramsDict["leverage"] = leverage.ToString();
            paramsDict["openType"] = openType;
            paramsDict["positionType"] = positionType;

            return await PostSignedAsync("/api/v1/private/position/change_leverage", paramsDict);
        }

        /// <summary>
        /// Get Position Mode
        /// GET /api/v1/private/position/position_mode
        /// </summary>
        public async Task<JsonDocument> GetPositionModeAsync(string symbol = null)
        {
            _logger?.LogInformation($"\n=== Get Position Mode {(symbol ?? "")} ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;

            return await GetSignedAsync("/api/v1/private/position/position_mode", paramsDict);
        }

        /// <summary>
        /// Change Position Mode
        /// POST /api/v1/private/position/change_position_mode
        /// </summary>
        public async Task<JsonDocument> ChangePositionModeAsync(string positionMode)
        {
            _logger?.LogInformation($"\n=== Change Position Mode: {positionMode} ===");
            var paramsDict = new Dictionary<string, string> { { "positionMode", positionMode } };
            return await PostSignedAsync("/api/v1/private/position/change_position_mode", paramsDict);
        }

        /// <summary>
        /// Reverse Position
        /// POST /api/v1/private/position/reverse
        /// </summary>
        public async Task<JsonDocument> ReversePositionAsync(string symbol, string positionId, string vol)
        {
            _logger?.LogInformation($"\n=== Reverse Position: {symbol} {positionId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "positionId", positionId },
                { "vol", vol }
            };
            return await PostSignedAsync("/api/v1/private/position/reverse", paramsDict);
        }

        /// <summary>
        /// Close All Positions
        /// POST /api/v1/private/position/close_all
        /// </summary>
        public async Task<JsonDocument> CloseAllPositionsAsync(string symbol = null)
        {
            _logger?.LogInformation($"\n=== Close All Positions {(symbol ?? "")} ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;

            return await PostSignedAsync("/api/v1/private/position/close_all", paramsDict);
        }

        #endregion

        #region Order Placement Endpoints

        /// <summary>
        /// Place Order
        /// POST /api/v1/private/order/submit
        /// </summary>
        public async Task<JsonDocument> PlaceOrderAsync(string symbol, string type, string side, string openType,
            int leverage, string price = null, string vol = "1", string positionMode = null, bool? reduceOnly = null)
        {
            _logger?.LogInformation($"\n=== Place Order: {symbol} {side} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "type", type },
                { "side", side },
                { "openType", openType },
                { "leverage", leverage.ToString() },
                { "vol", vol }
            };
            if (!string.IsNullOrEmpty(price)) paramsDict["price"] = price;
            if (!string.IsNullOrEmpty(positionMode)) paramsDict["positionMode"] = positionMode;
            if (reduceOnly.HasValue) paramsDict["reduceOnly"] = reduceOnly.Value.ToString().ToLower();

            return await PostSignedAsync("/api/v1/private/order/submit", paramsDict);
        }

        /// <summary>
        /// Place Batch Orders
        /// POST /api/v1/private/order/submit_batch
        /// </summary>
        public async Task<JsonDocument> PlaceBatchOrdersAsync(List<object> orders)
        {
            _logger?.LogInformation($"\n=== Place Batch Orders: {orders.Count} orders ===");
            return await PostSignedWithArrayBodyAsync("/api/v1/private/order/submit_batch", orders);
        }

        /// <summary>
        /// Chase Limit Order
        /// POST /api/v1/private/order/chase_limit_order
        /// </summary>
        public async Task<JsonDocument> ChaseLimitOrderAsync(string orderId)
        {
            _logger?.LogInformation($"\n=== Chase Limit Order: {orderId} ===");
            var paramsDict = new Dictionary<string, string> { { "orderId", orderId } };
            return await PostSignedAsync("/api/v1/private/order/chase_limit_order", paramsDict);
        }

        /// <summary>
        /// Change Limit Order
        /// POST /api/v1/private/order/change_limit_order
        /// </summary>
        public async Task<JsonDocument> ChangeLimitOrderAsync(string orderId, string newPrice = null, string newVolume = null)
        {
            _logger?.LogInformation($"\n=== Change Limit Order: {orderId} ===");
            var paramsDict = new Dictionary<string, string> { { "orderId", orderId } };
            if (!string.IsNullOrEmpty(newPrice)) paramsDict["price"] = newPrice;
            if (!string.IsNullOrEmpty(newVolume)) paramsDict["vol"] = newVolume;

            return await PostSignedAsync("/api/v1/private/order/change_limit_order", paramsDict);
        }

        #endregion

        #region Order Cancellation Endpoints

        /// <summary>
        /// Cancel Order
        /// POST /api/v1/private/order/cancel
        /// </summary>
        public async Task<JsonDocument> CancelOrderAsync(List<object> orderIds)
        {
            _logger?.LogInformation($"\n=== Cancel Order: {orderIds} ===");
            return await PostSignedWithArrayBodyAsync("/api/v1/private/order/cancel", orderIds);
        }

        /// <summary>
        /// Batch Cancel with External IDs
        /// POST /api/v1/private/order/batch_cancel_with_external
        /// </summary>
        public async Task<JsonDocument> BatchCancelWithExternalAsync(List<object> parameters)
        {
            _logger?.LogInformation($"\n=== Batch Cancel with External IDs ===");
            return await PostSignedWithArrayBodyAsync("/api/v1/private/order/batch_cancel_with_external", parameters);
        }

        /// <summary>
        /// Cancel with External ID
        /// POST /api/v1/private/order/cancel_with_external
        /// </summary>
        public async Task<JsonDocument> CancelWithExternalAsync(Dictionary<string, string> parameters)
        {
            _logger?.LogInformation($"\n=== Cancel with External ID ===");
            return await PostSignedAsync("/api/v1/private/order/cancel_with_external", parameters);
        }

        /// <summary>
        /// Cancel All Orders
        /// POST /api/v1/private/order/cancel_all
        /// </summary>
        public async Task<JsonDocument> CancelAllOrdersAsync(string symbol = null)
        {
            _logger?.LogInformation("\n=== Cancel All Orders ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;

            return await PostSignedAsync("/api/v1/private/order/cancel_all", paramsDict);
        }

        #endregion

        #region Order Query Endpoints

        /// <summary>
        /// Get Order by External ID
        /// GET /api/v1/private/order/external/{symbol}/{externalOid}
        /// </summary>
        public async Task<JsonDocument> GetOrderByExternalIdAsync(string symbol, string externalOid)
        {
            _logger?.LogInformation($"\n=== Get Order by External ID: {externalOid} ===");
            return await GetSignedAsync($"/api/v1/private/order/external/{symbol}/{externalOid}");
        }

        /// <summary>
        /// Get Order by ID
        /// GET /api/v1/private/order/get/{orderId}
        /// </summary>
        public async Task<JsonDocument> GetOrderByIdAsync(string orderId)
        {
            _logger?.LogInformation($"\n=== Get Order by ID: {orderId} ===");
            return await GetSignedAsync($"/api/v1/private/order/get/{orderId}");
        }

        /// <summary>
        /// Batch Query Orders
        /// GET /api/v1/private/order/batch_query
        /// </summary>
        public async Task<JsonDocument> BatchQueryOrdersAsync(string orderIds)
        {
            _logger?.LogInformation("\n=== Batch Query Orders ===");
            var paramsDict = new Dictionary<string, string> { { "order_ids", orderIds } };
            return await GetSignedAsync("/api/v1/private/order/batch_query", paramsDict);
        }

        /// <summary>
        /// Batch Query Orders with External IDs
        /// POST /api/v1/private/order/batch_query_with_external
        /// </summary>
        public async Task<JsonDocument> BatchQueryWithExternalAsync(List<object> parameters)
        {
            _logger?.LogInformation("\n=== Batch Query with External IDs ===");
            return await PostSignedWithArrayBodyAsync("/api/v1/private/order/batch_query_with_external", parameters);
        }

        /// <summary>
        /// Get All Open Orders
        /// GET /api/v1/private/order/list/open_orders
        /// </summary>
        public async Task<JsonDocument> GetListOpenOrdersAsync(string symbol = null)
        {
            _logger?.LogInformation("\n=== Get All Open Orders ===");
            var paramsDict = new Dictionary<string, string>();
            if (!string.IsNullOrEmpty(symbol)) paramsDict["symbol"] = symbol;

            return await GetSignedAsync("/api/v1/private/order/list/open_orders", paramsDict);
        }

        /// <summary>
        /// Get History Orders
        /// GET /api/v1/private/order/list/history_orders
        /// </summary>
        public async Task<JsonDocument> GetHistoryOrdersAsync(string symbol, long? startTime = null,
            long? endTime = null, int? pageNum = null, int? pageSize = null, string side = null)
        {
            _logger?.LogInformation("\n=== Get History Orders ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            if (startTime.HasValue) paramsDict["startTime"] = startTime.ToString();
            if (endTime.HasValue) paramsDict["endTime"] = endTime.ToString();
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();
            if (!string.IsNullOrEmpty(side)) paramsDict["side"] = side;

            return await GetSignedAsync("/api/v1/private/order/list/history_orders", paramsDict);
        }

        /// <summary>
        /// Get Order Deals V3
        /// GET /api/v1/private/order/list/order_deals/v3
        /// </summary>
        public async Task<JsonDocument> GetOrderDealsV3Async(string symbol, string orderId, 
            int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get Order Deals V3: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "orderId", orderId }
            };
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/order/list/order_deals/v3", paramsDict);
        }

        /// <summary>
        /// Get Deal Details by Order ID
        /// GET /api/v1/private/order/deal_details/{orderId}
        /// </summary>
        public async Task<JsonDocument> GetDealDetailsAsync(string orderId)
        {
            _logger?.LogInformation($"\n=== Get Deal Details: {orderId} ===");
            return await GetSignedAsync($"/api/v1/private/order/deal_details/{orderId}");
        }

        /// <summary>
        /// Get Close Orders
        /// GET /api/v1/private/order/list/close_orders
        /// </summary>
        public async Task<JsonDocument> GetCloseOrdersAsync(string symbol, long? startTime = null,
            long? endTime = null, int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get Close Orders: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            if (startTime.HasValue) paramsDict["startTime"] = startTime.ToString();
            if (endTime.HasValue) paramsDict["endTime"] = endTime.ToString();
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/order/list/close_orders", paramsDict);
        }

        #endregion

        #region Plan Order Endpoints

        /// <summary>
        /// Get Plan Orders
        /// GET /api/v1/private/planorder/list/orders
        /// </summary>
        public async Task<JsonDocument> GetPlanOrdersAsync(string symbol, int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get Plan Orders: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/planorder/list/orders", paramsDict);
        }

        /// <summary>
        /// Place Plan Order V2
        /// POST /api/v1/private/planorder/place/v2
        /// </summary>
        public async Task<JsonDocument> PlacePlanOrderV2Async(string symbol, string side, string openType, int leverage,
            string triggerType, string triggerPrice, string orderType, string price, string volume,
            string executeCycle, string trend)
        {
            _logger?.LogInformation($"\n=== Place Plan Order V2: {symbol} {side} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "side", side },
                { "openType", openType },
                { "leverage", leverage.ToString() },
                { "triggerType", triggerType },
                { "triggerPrice", triggerPrice },
                { "orderType", orderType },
                { "price", price },
                { "vol", volume },
                { "executeCycle", executeCycle },
                { "trend", trend }
            };
            return await PostSignedAsync("/api/v1/private/planorder/place/v2", paramsDict);
        }

        /// <summary>
        /// Change Plan Order Price
        /// POST /api/v1/private/planorder/change_price
        /// </summary>
        public async Task<JsonDocument> ChangePlanOrderPriceAsync(string symbol, string orderId, 
            string newPrice, string triggerPrice, string orderType)
        {
            _logger?.LogInformation($"\n=== Change Plan Order Price: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "orderId", orderId },
                { "price", newPrice },
                { "triggerPrice", triggerPrice },
                { "orderType", orderType },
                { "from", "1" },
                { "trend", "1" }
            };
            return await PostSignedAsync("/api/v1/private/planorder/change_price", paramsDict);
        }

        /// <summary>
        /// Cancel Plan Order
        /// POST /api/v1/private/planorder/cancel
        /// </summary>
        public async Task<JsonDocument> CancelPlanOrderAsync(List<object> parameters)
        {
            _logger?.LogInformation($"\n=== Cancel Plan Order ===");
            return await PostSignedWithArrayBodyAsync("/api/v1/private/planorder/cancel", parameters);
        }

        /// <summary>
        /// Cancel All Plan Orders
        /// POST /api/v1/private/planorder/cancel_all
        /// </summary>
        public async Task<JsonDocument> CancelAllPlanOrdersAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Cancel All Plan Orders: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            return await PostSignedAsync("/api/v1/private/planorder/cancel_all", paramsDict);
        }

        #endregion

        #region Stop Order Endpoints

        /// <summary>
        /// Place Stop Order
        /// POST /api/v1/private/stoporder/place
        /// </summary>
        public async Task<JsonDocument> PlaceStopOrderAsync(string symbol, int profitTrend, int lossTrend,
            string positionId, int vol, int stopLossType, string stopLossOrderPrice, string stopLossPrice)
        {
            _logger?.LogInformation($"\n=== Place Stop Order: {symbol} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "profitTrend", profitTrend.ToString() },
                { "lossTrend", lossTrend.ToString() },
                { "positionId", positionId },
                { "vol", vol.ToString() },
                { "stopLossType", stopLossType.ToString() },
                { "stopLossOrderPrice", stopLossOrderPrice },
                { "stopLossPrice", stopLossPrice }
            };
            return await PostSignedAsync("/api/v1/private/stoporder/place", paramsDict);
        }

        /// <summary>
        /// Cancel Stop Order
        /// POST /api/v1/private/stoporder/cancel
        /// </summary>
        public async Task<JsonDocument> CancelStopOrderAsync(string symbol, string orderId)
        {
            _logger?.LogInformation($"\n=== Cancel Stop Order: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "orderId", orderId }
            };
            return await PostSignedAsync("/api/v1/private/stoporder/cancel", paramsDict);
        }

        /// <summary>
        /// Cancel All Stop Orders
        /// POST /api/v1/private/stoporder/cancel_all
        /// </summary>
        public async Task<JsonDocument> CancelAllStopOrdersAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Cancel All Stop Orders: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            return await PostSignedAsync("/api/v1/private/stoporder/cancel_all", paramsDict);
        }

        /// <summary>
        /// Change Stop Order Price
        /// POST /api/v1/private/stoporder/change_price
        /// </summary>
        public async Task<JsonDocument> ChangeStopOrderPriceAsync(string orderId, string newStopPrice)
        {
            _logger?.LogInformation($"\n=== Change Stop Order Price: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "orderId", orderId },
                { "takeProfitPrice", newStopPrice }
            };
            return await PostSignedAsync("/api/v1/private/stoporder/change_price", paramsDict);
        }

        /// <summary>
        /// Change Stop Order Plan Price
        /// POST /api/v1/private/stoporder/change_plan_price
        /// </summary>
        public async Task<JsonDocument> ChangeStopOrderPlanPriceAsync(string orderId, string newPlanPrice)
        {
            _logger?.LogInformation($"\n=== Change Stop Order Plan Price: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "stopPlanOrderId", orderId },
                { "stopLossPrice", newPlanPrice }
            };
            return await PostSignedAsync("/api/v1/private/stoporder/change_plan_price", paramsDict);
        }

        /// <summary>
        /// Change Plan Order Stop Order
        /// POST /api/v1/private/planorder/change_stop_order
        /// </summary>
        public async Task<JsonDocument> ChangePlanOrderStopOrderAsync(string symbol, string orderId, string newStopPrice)
        {
            _logger?.LogInformation($"\n=== Change Plan Order Stop Order: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "orderId", orderId }
            };
            if (!string.IsNullOrEmpty(newStopPrice)) paramsDict["stopLossPrice"] = newStopPrice;

            return await PostSignedAsync("/api/v1/private/planorder/change_stop_order", paramsDict);
        }

        /// <summary>
        /// Get Stop Orders List
        /// GET /api/v1/private/stoporder/list/orders
        /// </summary>
        public async Task<JsonDocument> GetStopOrdersListAsync(string symbol, int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get Stop Orders List: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/stoporder/list/orders", paramsDict);
        }

        /// <summary>
        /// Get Open Stop Orders
        /// GET /api/v1/private/stoporder/open_orders
        /// </summary>
        public async Task<JsonDocument> GetOpenStopOrdersAsync(string symbol)
        {
            _logger?.LogInformation($"\n=== Get Open Stop Orders: {symbol} ===");
            var paramsDict = new Dictionary<string, string> { { "symbol", symbol } };
            return await GetSignedAsync("/api/v1/private/stoporder/open_orders", paramsDict);
        }

        #endregion

        #region Trailing Stop Order Endpoints

        /// <summary>
        /// Place Trailing Stop Order
        /// POST /api/v1/private/trackorder/place
        /// </summary>
        public async Task<JsonDocument> PlaceTrailingStopOrderAsync(string symbol, int leverage, int side, string vol,
            int openType, int trend, string activePrice, int backType, string backValue,
            int positionMode, bool? reduceOnly = null)
        {
            _logger?.LogInformation($"\n=== Place Trailing Stop Order: {symbol} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "leverage", leverage.ToString() },
                { "side", side.ToString() },
                { "vol", vol },
                { "openType", openType.ToString() },
                { "trend", trend.ToString() },
                { "backType", backType.ToString() },
                { "backValue", backValue },
                { "positionMode", positionMode.ToString() }
            };
            if (!string.IsNullOrEmpty(activePrice)) paramsDict["activePrice"] = activePrice;
            if (reduceOnly.HasValue) paramsDict["reduceOnly"] = reduceOnly.Value.ToString().ToLower();

            return await PostSignedAsync("/api/v1/private/trackorder/place", paramsDict);
        }

        /// <summary>
        /// Cancel Trailing Stop Order
        /// POST /api/v1/private/trackorder/cancel
        /// </summary>
        public async Task<JsonDocument> CancelTrailingStopOrderAsync(string symbol, string orderId)
        {
            _logger?.LogInformation($"\n=== Cancel Trailing Stop Order: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "orderId", orderId }
            };
            return await PostSignedAsync("/api/v1/private/trackorder/cancel", paramsDict);
        }

        /// <summary>
        /// Change Trailing Stop Order
        /// POST /api/v1/private/trackorder/change_order
        /// </summary>
        public async Task<JsonDocument> ChangeTrailingStopOrderAsync(string symbol, string orderId, string newActivationPrice)
        {
            _logger?.LogInformation($"\n=== Change Trailing Stop Order: {orderId} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "trackOrderId", orderId },
                { "trend", "1" },
                { "backType", "1" },
                { "backValue", "0.3" },
                { "vol", "1" }
            };
            if (!string.IsNullOrEmpty(newActivationPrice)) paramsDict["activePrice"] = newActivationPrice;

            return await PostSignedAsync("/api/v1/private/trackorder/change_order", paramsDict);
        }

        /// <summary>
        /// Get Trailing Stop Orders List
        /// GET /api/v1/private/trackorder/list/orders
        /// </summary>
        public async Task<JsonDocument> GetTrailingStopOrdersListAsync(string symbol, int? pageNum = null, int? pageSize = null)
        {
            _logger?.LogInformation($"\n=== Get Trailing Stop Orders List: {symbol} ===");
            var paramsDict = new Dictionary<string, string>
            {
                { "symbol", symbol },
                { "states", "0" }
            };
            if (pageNum.HasValue) paramsDict["page_num"] = pageNum.ToString();
            if (pageSize.HasValue) paramsDict["page_size"] = pageSize.ToString();

            return await GetSignedAsync("/api/v1/private/trackorder/list/orders", paramsDict);
        }

        #endregion
    }
}