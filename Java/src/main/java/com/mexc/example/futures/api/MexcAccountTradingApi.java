package com.mexc.example.futures.api;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Complete MEXC Futures Account and Trading Endpoints
 */
public class MexcAccountTradingApi extends MexcOkHttpClient {

    public MexcAccountTradingApi(String apiKey, String secretKey) {
        super(apiKey, secretKey);
    }

    /**
     * Get All Account Assets
     * GET /api/v1/private/account/assets
     */
    public JsonObject getAllAccountAssets() throws IOException {
        System.out.println("\n=== Get All Account Assets ===");
        return getSigned("/api/v1/private/account/assets", null);
    }

    /**
     * 2. Get Single Currency Asset Information
     * GET /api/v1/private/account/asset/{currency}
     */
    public JsonObject getSingleAsset(String currency) throws IOException {
        System.out.println("\n=== Get Single Currency Asset: " + currency + " ===");
        return getSigned("/api/v1/private/account/asset/" + currency, null);
    }

    /**
     * Get Asset Transfer Records
     * GET /api/v1/private/account/transfer_record
     */
    public JsonObject getTransferRecord(String currency, String state, String type,
                                        int pageNum, int pageSize) throws IOException {
        System.out.println("\n=== Get Asset Transfer Records ===");
        Map<String, String> params = new HashMap<>();
        if (currency != null) params.put("currency", currency);
        if (state != null) params.put("state", state);
        if (type != null) params.put("type", type);
        params.put("page_num", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/account/transfer_record", params);
    }

    /**
     * View Personal Profit Rate
     * GET /api/v1/private/account/profit_rate/{type}
     */
    public JsonObject getProfitRate(int type) throws IOException {
        System.out.println("\n=== Get Personal Profit Rate (type=" + type + ") ===");
        return getSigned("/api/v1/private/account/profit_rate/" + type, null);
    }

    /**
     * Asset Analysis
     * GET /api/v1/private/account/asset/analysis/{type}
     */
    public JsonObject getAssetAnalysis(int type, String currency, Long startTime, Long endTime) throws IOException {
        System.out.println("\n=== Get Asset Analysis (type=" + type + ") ===");
        Map<String, String> params = new HashMap<>();
        params.put("currency", currency);
        if (startTime != null) params.put("startTime", String.valueOf(startTime));
        if (endTime != null) params.put("endTime", String.valueOf(endTime));
        return getSigned("/api/v1/private/account/asset/analysis/" + type, params);
    }

    /**
     * Get Fee Deduction Configurations
     * GET /api/v1/private/account/feeDeductConfigs
     */
    public JsonObject getFeeDeductConfigs() throws IOException {
        System.out.println("\n=== Get Fee Deduction Configurations ===");
        return getSigned("/api/v1/private/account/feeDeductConfigs", null);
    }

    /**
     * Get Yesterday's PnL
     * GET /api/v1/private/account/asset/analysis/yesterday_pnl
     */
    public JsonObject getYesterdayPnl() throws IOException {
        System.out.println("\n=== Get Yesterday's PnL ===");
        return getSigned("/api/v1/private/account/asset/analysis/yesterday_pnl", null);
    }

    /**
     * User Asset Analysis V3
     * POST /api/v1/private/account/asset/analysis/v3
     */
    public JsonObject postAssetAnalysisV3(long startTime, long endTime, Integer reverse,
                                          Integer includeUnrealisedPnl, String symbol) throws IOException {
        System.out.println("\n=== User Asset Analysis V3 ===");
        Map<String, String> params = new HashMap<>();
        params.put("startTime", String.valueOf(startTime));
        params.put("endTime", String.valueOf(endTime));
        if (reverse != null) params.put("reverse", String.valueOf(reverse));
        if (includeUnrealisedPnl != null) params.put("includeUnrealisedPnl", String.valueOf(includeUnrealisedPnl));
        if (symbol != null) params.put("symbol", symbol);
        return postSigned("/api/v1/private/account/asset/analysis/v3", params);
    }


    /**
     * Daily Calendar Analysis V3
     * POST /api/v1/private/account/asset/analysis/calendar/daily/v3
     */
    public JsonObject postCalendarDailyV3(long startTime, long endTime, Integer reverse,
                                          Integer includeUnrealisedPnl) throws IOException {
        System.out.println("\n=== Daily Calendar Analysis V3 ===");
        Map<String, String> params = new HashMap<>();
        params.put("startTime", String.valueOf(startTime));
        params.put("endTime", String.valueOf(endTime));
        if (reverse != null) params.put("reverse", String.valueOf(reverse));
        if (includeUnrealisedPnl != null) params.put("includeUnrealisedPnl", String.valueOf(includeUnrealisedPnl));
        return postSigned("/api/v1/private/account/asset/analysis/calendar/daily/v3", params);
    }

    /**
     * Monthly Calendar Analysis V3
     * POST /api/v1/private/account/asset/analysis/calendar/monthly/v3
     */
    public JsonObject postCalendarMonthlyV3(Integer reverse, Integer includeUnrealisedPnl) throws IOException {
        System.out.println("\n=== Monthly Calendar Analysis V3 ===");
        Map<String, String> params = new HashMap<>();
        if (reverse != null) params.put("reverse", String.valueOf(reverse));
        if (includeUnrealisedPnl != null) params.put("includeUnrealisedPnl", String.valueOf(includeUnrealisedPnl));
        return postSigned("/api/v1/private/account/asset/analysis/calendar/monthly/v3", params);
    }


    /**
     * Recent User Asset Analysis V3
     * POST /api/v1/private/account/asset/analysis/recent/v3
     */
    public JsonObject postRecentAnalysisV3(Integer reverse, Integer includeUnrealisedPnl, String symbol) throws IOException {
        System.out.println("\n=== Recent Asset Analysis V3 ===");
        Map<String, String> params = new HashMap<>();
        if (reverse != null) params.put("reverse", String.valueOf(reverse));
        if (includeUnrealisedPnl != null) params.put("includeUnrealisedPnl", String.valueOf(includeUnrealisedPnl));
        if (symbol != null) params.put("symbol", symbol);
        return postSigned("/api/v1/private/account/asset/analysis/recent/v3", params);
    }

    /**
     * Get Today's PnL
     * GET /api/v1/private/account/asset/analysis/today_pnl
     * <p>
     * Retrieves today's profit and loss information.
     */
    public JsonObject getTodayPnl() throws IOException {
        System.out.println("\n=== Get Today's PnL ===");
        return getSigned("/api/v1/private/account/asset/analysis/today_pnl", null);
    }


    /**
     * Get Account Configuration
     * GET /api/v1/private/account/config/contractFeeDiscountConfig
     */
    public JsonObject getAccountConfig() throws IOException {
        System.out.println("\n=== Get Account Configuration ===");
        return getSigned("/api/v1/private/account/config/contractFeeDiscountConfig", null);
    }


    /**
     * Get Contract Fee Discount Configuration
     * GET /api/v1/private/account/config/contractFeeDiscountConfig
     */
    public JsonObject getContractFeeDiscountConfig() throws IOException {
        System.out.println("\n=== Get Contract Fee Discount Configuration ===");
        return getSigned("/api/v1/private/account/config/contractFeeDiscountConfig", null);
    }

    /**
     * Get Order Fee Details
     * GET /api/v1/private/order/fee_details
     * <p>
     * Retrieves detailed fee information for a specific order.
     */
    public JsonObject getOrderFeeDetails(String symbol, String orderId) throws IOException {
        System.out.println("\n=== Get Order Fee Details: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        return getSigned("/api/v1/private/order/fee_details", params);
    }

    /**
     * Get Account Discount Type
     * GET /api/v1/private/account/discountType
     * <p>
     * Retrieves the discount type applied to the account.
     */
    public JsonObject getAccountDiscountType() throws IOException {
        System.out.println("\n=== Get Account Discount Type ===");
        return getSigned("/api/v1/private/account/discountType", null);
    }


    /**
     * Export Asset Analysis
     * GET /api/v1/private/account/asset/analysis/export
     */
    public JsonObject exportAssetAnalysis(long startTime, long endTime, String symbol,
                                          Integer reverse, Integer includeUnrealisedPnl) throws IOException {
        System.out.println("\n=== Export Asset Analysis ===");
        Map<String, String> params = new HashMap<>();
        params.put("startTime", String.valueOf(startTime));
        params.put("endTime", String.valueOf(endTime));
        if (symbol != null) params.put("symbol", symbol);
        if (reverse != null) params.put("reverse", String.valueOf(reverse));
        if (includeUnrealisedPnl != null) params.put("includeUnrealisedPnl", String.valueOf(includeUnrealisedPnl));
        return getSigned("/api/v1/private/account/asset/analysis/export", params);
    }


    /**
     * Get Total Order Deal Fee
     * GET /api/v1/private/account/asset_book/order_deal_fee/total
     */
    public JsonObject getTotalOrderDealFee(long startTime, long endTime, String symbol) throws IOException {
        System.out.println("\n=== Get Total Order Deal Fee ===");
        Map<String, String> params = new HashMap<>();
        params.put("startTime", String.valueOf(startTime));
        params.put("endTime", String.valueOf(endTime));
        if (symbol != null) params.put("symbol", symbol);
        return getSigned("/api/v1/private/account/asset_book/order_deal_fee/total", params);
    }

    /**
     * Get Contract Fee Rate
     * GET /api/v1/private/account/contract/fee_rate
     */
    public JsonObject getContractFeeRate(String symbol) throws IOException {
        System.out.println("\n=== Get Contract Fee Rate: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        return getSigned("/api/v1/private/account/contract/fee_rate", params);
    }

    /**
     * Get Zero Fee Rate Contracts
     * GET /api/v1/private/account/contract/zero_fee_rate
     * <p>
     * Retrieves list of contracts with zero fee rate.
     */
    public JsonObject getZeroFeeRateContracts() throws IOException {
        System.out.println("\n=== Get Zero Fee Rate Contracts ===");
        return getSigned("/api/v1/private/account/contract/zero_fee_rate", null);
    }


    /**
     * Get History Positions
     * GET /api/v1/private/position/list/history_positions
     * <p>
     * Retrieves historical position data.
     */
    public JsonObject getHistoryPositions(String symbol, Long startTime, Long endTime,
                                          Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get History Positions: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        if (startTime != null) params.put("startTime", String.valueOf(startTime));
        if (endTime != null) params.put("endTime", String.valueOf(endTime));
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/position/list/history_positions", params);
    }

    /**
     * Get Open Positions
     * GET /api/v1/private/position/open_positions
     */
    public JsonObject getOpenPositions(String symbol) throws IOException {
        System.out.println("\n=== Get Open Positions" + (symbol != null ? ": " + symbol : "") + " ===");
        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        return getSigned("/api/v1/private/position/open_positions", params);
    }

    /**
     * Get Funding Records
     * GET /api/v1/private/position/funding_records
     */
    public JsonObject getFundingRecords(String symbol, Long startTime, Long endTime,
                                        Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get Funding Records: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        if (startTime != null) params.put("startTime", String.valueOf(startTime));
        if (endTime != null) params.put("endTime", String.valueOf(endTime));
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/position/funding_records", params);
    }

    /**
     * Get Account Risk Limit
     * GET /api/v1/private/account/risk_limit
     */
    public JsonObject getAccountRiskLimit() throws IOException {
        System.out.println("\n=== Get Account Risk Limit ===");
        return getSigned("/api/v1/private/account/risk_limit", null);
    }

    /**
     * 8. Get User Tiered Fee Rate V2
     * GET /api/v1/private/account/tiered_fee_rate/v2
     */
    public JsonObject getTieredFeeRateV2() throws IOException {
        System.out.println("\n=== 8. Get Tiered Fee Rate V2 ===");
        return getSigned("/api/v1/private/account/tiered_fee_rate/v2", null);
    }


    /**
     * Change Position Margin
     * POST /api/v1/private/position/change_margin
     */
    public JsonObject changePositionMargin(String positionId, String amount, String type) throws IOException {
        System.out.println("\n=== Change Position Margin: " + positionId + " " + type + " " + amount + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("positionId", positionId);
        params.put("amount", amount);
        params.put("type", type);
        return postSigned("/api/v1/private/position/change_margin", params);
    }

    /**
     * Change Auto Add Margin
     * POST /api/v1/private/position/change_auto_add_im
     */
    public JsonObject changeAutoAddMargin(String positionId, boolean autoAdd) throws IOException {
        System.out.println("\n=== Change Auto Add Margin: " + positionId + " " + autoAdd + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("positionId", positionId);
        params.put("isEnabled", String.valueOf(autoAdd));
        return postSigned("/api/v1/private/position/change_auto_add_im", params);
    }

    /**
     * Get Position Leverage
     * GET /api/v1/private/position/leverage
     */
    public JsonObject getPositionLeverage(String symbol) throws IOException {
        System.out.println("\n=== Get Position Leverage: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        return getSigned("/api/v1/private/position/leverage", params);
    }

    /**
     * Change Leverage
     * POST /api/v1/private/position/change_leverage
     */
    public JsonObject changeLeverage(String positionId, String symbol, int leverage, String openType, String positionType) throws IOException {
        System.out.println("\n=== Change Leverage Enhanced: " + positionId + " " + leverage + "x ===");
        Map<String, String> params = new HashMap<>();
        if (positionId != null && !positionId.isEmpty()) {
            params.put("positionId", positionId);
        } else {
            params.put("symbol", symbol);
        }
        params.put("leverage", String.valueOf(leverage));
        params.put("openType", openType);
        params.put("positionType", positionType);
        return postSigned("/api/v1/private/position/change_leverage", params);
    }

    /**
     * Get Position Mode
     * GET /api/v1/private/position/position_mode
     */
    public JsonObject getPositionMode(String symbol) throws IOException {
        System.out.println("\n=== Get Position Mode" + (symbol != null ? ": " + symbol : "") + " ===");
        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        return getSigned("/api/v1/private/position/position_mode", params);
    }


    /**
     * Place Order
     * POST /api/v1/private/order/submit
     */
    public JsonObject placeOrder(String symbol, String type, String side, String openType,
                                 Integer leverage, String price, String vol,
                                 String positionMode, Boolean reduceOnly) throws IOException {
        System.out.println("\n=== Place Order: " + symbol + " " + side + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("type", type);
        params.put("side", side);
        params.put("openType", openType);
        params.put("leverage", String.valueOf(leverage));
        params.put("vol", vol);
        if (price != null) params.put("price", price);
        if (positionMode != null) params.put("positionMode", positionMode);
        if (reduceOnly != null) params.put("reduceOnly", String.valueOf(reduceOnly));
        return postSigned("/api/v1/private/order/submit", params);
    }

    /**
     * Place Batch Orders
     * POST /api/v1/private/order/submit_batch
     */
    public JsonObject placeBatchOrders(List<Object> orders) throws IOException {
        System.out.println("\n=== Place Batch Orders: " + orders.size() + " orders ===");
        return postSignedWithArrayBody("/api/v1/private/order/submit_batch", orders);
    }

    /**
     * Chase Limit Order
     * POST /api/v1/private/order/chase_limit_order
     */
    public JsonObject chaseLimitOrder(String orderId) throws IOException {
        System.out.println("\n=== Chase Limit Order: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        return postSigned("/api/v1/private/order/chase_limit_order", params);
    }


    /**
     * Change Limit Order
     * POST /api/v1/private/order/change_limit_order
     */
    public JsonObject changeLimitOrder(String orderId, String newPrice, String newVolume) throws IOException {
        System.out.println("\n=== Change Limit Order: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        if (newPrice != null) params.put("price", newPrice);
        if (newVolume != null) params.put("vol", newVolume);
        return postSigned("/api/v1/private/order/change_limit_order", params);
    }

    /**
     * Cancel Order
     * POST /api/v1/private/order/cancel
     */
    public JsonObject cancelOrder(List<Object> orderIds) throws IOException {
        System.out.println("\n=== Cancel Order: " + orderIds + " ===");
        return postSignedWithArrayBody("/api/v1/private/order/cancel", orderIds);
    }


    /**
     * Batch Cancel with External IDs
     * POST /api/v1/private/order/batch_cancel_with_external
     */
    public JsonObject batchCancelWithExternal(List<Object> params) throws IOException {
        System.out.println("\n=== Batch Cancel with External IDs: " + params + " orders ===");
        return postSignedWithArrayBody("/api/v1/private/order/batch_cancel_with_external", params);
    }

    /**
     * Cancel with External ID
     * POST /api/v1/private/order/cancel_with_external
     */
    public JsonObject cancelWithExternal(Map<String, String> params) throws IOException {
        System.out.println("\n=== Cancel with External ID: " + params + " ===");
        return postSigned("/api/v1/private/order/cancel_with_external", params);
    }

    /**
     * Cancel All Orders
     * POST /api/v1/private/order/cancel_all
     */
    public JsonObject cancelAllOrders(String symbol) throws IOException {
        System.out.println("\n=== Cancel All Orders ===");
        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        return postSigned("/api/v1/private/order/cancel_all", params);
    }


    /**
     * Reverse Position
     * POST /api/v1/private/position/reverse
     */
    public JsonObject reversePosition(String symbol, String positionId, String vol) throws IOException {
        System.out.println("\n=== Reverse Position: " + symbol + " " + positionId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("positionId", positionId);
        params.put("vol", vol);
        return postSigned("/api/v1/private/position/reverse", params);
    }

    /**
     * Close All Positions
     * POST /api/v1/private/position/close_all
     */
    public JsonObject closeAllPositions(String symbol) throws IOException {
        System.out.println("\n=== Close All Positions" + (symbol != null ? ": " + symbol : "") + " ===");
        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        return postSigned("/api/v1/private/position/close_all", params);
    }


    /**
     * Get Order by External ID
     * GET /api/v1/private/order/external/{symbol}/{external_oid}
     */
    public JsonObject getOrderByExternalId(String symbol, String externalOid) throws IOException {
        System.out.println("\n=== Get Order by External ID: " + externalOid + " ===");
        String endpoint = "/api/v1/private/order/external/" + symbol + "/" + externalOid;
        return getSigned(endpoint, null);
    }

    /**
     * Get Order by Order ID
     * GET /api/v1/private/order/get/{orderId}
     */
    public JsonObject getOrderById(String orderId) throws IOException {
        System.out.println("\n=== Get Order by ID: " + orderId + " ===");
        String endpoint = "/api/v1/private/order/get/" + orderId;
        return getSigned(endpoint, null);
    }

    /**
     * Batch Query Orders
     * GET /api/v1/private/order/batch_query
     */
    public JsonObject batchQueryOrders(String orderIds) throws IOException {
        System.out.println("\n=== Batch Query Orders ===");
        Map<String, String> params = new HashMap<>();
        params.put("order_ids", orderIds);
        return getSigned("/api/v1/private/order/batch_query", params);
    }

    /**
     * Batch Query Orders with External IDs
     * POST /api/v1/private/order/batch_query_with_external
     */
    public JsonObject batchQueryWithExternal(List<Object> params) throws IOException {
        System.out.println("\n=== Batch Query with External IDs ===");
        return postSignedWithArrayBody("/api/v1/private/order/batch_query_with_external", params);
    }

    /**
     * Get All Open Orders
     * GET /api/v1/private/order/list/open_orders
     */
    public JsonObject getListOpenOrders(String symbol) throws IOException {
        System.out.println("\n=== Get All Open Orders ===");
        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        return getSigned("/api/v1/private/order/list/open_orders", params);
    }

    /**
     * Get History Orders
     * GET /api/v1/private/order/list/history_orders
     */
    public JsonObject getHistoryOrders(String symbol, Long startTime, Long endTime,
                                       Integer pageNum, Integer pageSize, String side) throws IOException {
        System.out.println("\n=== Get History Orders ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        if (startTime != null) params.put("startTime", String.valueOf(startTime));
        if (endTime != null) params.put("endTime", String.valueOf(endTime));
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        if (side != null) params.put("side", side);
        return getSigned("/api/v1/private/order/list/history_orders", params);
    }

    /**
     * Get Order Deals V3
     * GET /api/v1/private/order/list/order_deals/v3
     */
    public JsonObject getOrderDealsV3(String symbol, String orderId, Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get Order Deals V3: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/order/list/order_deals/v3", params);
    }

    /**
     * Get Deal Details by Order ID
     * GET /api/v1/private/order/deal_details/{order_id}
     */
    public JsonObject getDealDetailsByOrderId(String orderId) throws IOException {
        System.out.println("\n=== Get Deal Details by Order ID: " + orderId + " ===");
        String endpoint = "/api/v1/private/order/deal_details/" + orderId;
        return getSigned(endpoint, null);
    }

    /**
     * Get Deal Details by Order ID (alternative path)
     * GET /api/v1/private/order/deal_details/{orderId}
     */
    public JsonObject getDealDetails(String orderId) throws IOException {
        System.out.println("\n=== Get Deal Details: " + orderId + " ===");
        String endpoint = "/api/v1/private/order/deal_details/" + orderId;
        return getSigned(endpoint, null);
    }

    /**
     * Get Close Orders
     * GET /api/v1/private/order/list/close_orders
     */
    public JsonObject getCloseOrders(String symbol, Long startTime, Long endTime,
                                     Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get Close Orders: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        if (startTime != null) params.put("startTime", String.valueOf(startTime));
        if (endTime != null) params.put("endTime", String.valueOf(endTime));
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/order/list/close_orders", params);
    }

    /**
     * Get Plan Orders
     * GET /api/v1/private/planorder/list/orders
     */
    public JsonObject getPlanOrders(String symbol, Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get Plan Orders: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/planorder/list/orders", params);
    }

    /**
     * Place Plan Order V2
     * POST /api/v1/private/planorder/place/v2
     */
    public JsonObject placePlanOrderV2(String symbol, String side, String openType, int leverage,
                                       String triggerType, String triggerPrice, String orderType,
                                       String price, String volume, String executeCycle,
                                       String trend) throws IOException {
        System.out.println("\n=== Place Plan Order V2: " + symbol + " " + side + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("side", side);
        params.put("openType", openType);
        params.put("leverage", String.valueOf(leverage));
        params.put("triggerType", triggerType);
        params.put("triggerPrice", triggerPrice);
        params.put("orderType", orderType);
        params.put("price", price);
        params.put("vol", volume);
        params.put("executeCycle", executeCycle);
        params.put("trend", trend);
        return postSigned("/api/v1/private/planorder/place/v2", params);
    }

    /**
     * Change Plan Order Price
     * POST /api/v1/private/planorder/change_price
     */
    public JsonObject changePlanOrderPrice(String symbol, String orderId, String newPrice, String triggerPrice, String orderType) throws IOException {
        System.out.println("\n=== Change Plan Order Price: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        params.put("price", newPrice);
        params.put("triggerPrice", triggerPrice);
        params.put("orderType", orderType);
        params.put("from", "1");
        params.put("trend", "1");
        return postSigned("/api/v1/private/planorder/change_price", params);
    }

    /**
     * Cancel Plan Order
     * POST /api/v1/private/planorder/cancel
     */
    public JsonObject cancelPlanOrder(List<Object> params) throws IOException {
        System.out.println("\n=== Cancel Plan Order: " + params + " ===");
        return postSignedWithArrayBody("/api/v1/private/planorder/cancel", params);
    }

    /**
     * Cancel All Plan Orders
     * POST /api/v1/private/planorder/cancel_all
     */
    public JsonObject cancelAllPlanOrders(String symbol) throws IOException {
        System.out.println("\n=== Cancel All Plan Orders: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        return postSigned("/api/v1/private/planorder/cancel_all", params);
    }


    /**
     * Place Stop Order
     * POST /api/v1/private/stoporder/place
     */
    public JsonObject placeStopOrder(String symbol, int profitTrend, int lossTrend,
                                     String positionId, int vol, int stopLossType,
                                     String stopLossOrderPrice, String stopLossPrice) throws IOException {
        System.out.println("\n=== Place Stop Order : " + symbol + " " + stopLossType + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("profitTrend", String.valueOf(profitTrend));
        params.put("lossTrend", String.valueOf(lossTrend));
        params.put("positionId", positionId);
        params.put("vol", String.valueOf(vol));
        params.put("stopLossType", String.valueOf(stopLossType));
        params.put("stopLossOrderPrice", stopLossOrderPrice);
        params.put("stopLossPrice", stopLossPrice);

        return postSigned("/api/v1/private/stoporder/place", params);
    }

    /**
     * Cancel Stop Order
     * POST /api/v1/private/stoporder/cancel
     */
    public JsonObject cancelStopOrder(String symbol, String orderId) throws IOException {
        System.out.println("\n=== Cancel Stop Order : " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        return postSigned("/api/v1/private/stoporder/cancel", params);
    }

    /**
     * Cancel All Stop Orders
     * POST /api/v1/private/stoporder/cancel_all
     */
    public JsonObject cancelAllStopOrdersEnhanced(String symbol) throws IOException {
        System.out.println("\n=== Cancel All Stop Orders Enhanced: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        return postSigned("/api/v1/private/stoporder/cancel_all", params);
    }

    /**
     * Change Stop Order Price
     * POST /api/v1/private/stoporder/change_price
     */
    public JsonObject changeStopOrderPrice(String orderId, String newStopPrice) throws IOException {
        System.out.println("\n=== Change Stop Order Price: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("takeProfitPrice", newStopPrice);
        return postSigned("/api/v1/private/stoporder/change_price", params);
    }

    /**
     * Change Stop Order Plan Price
     * POST /api/v1/private/stoporder/change_plan_price
     */
    public JsonObject changeStopOrderPlanPrice(String symbol, String orderId, String newPlanPrice) throws IOException {
        System.out.println("\n=== Change Stop Order Plan Price: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("stopPlanOrderId", orderId);
        params.put("stopLossPrice", newPlanPrice);
        return postSigned("/api/v1/private/stoporder/change_plan_price", params);
    }

    /**
     * Change Plan Order Stop Order
     * POST /api/v1/private/planorder/change_stop_order
     */
    public JsonObject changePlanOrderStopOrder(String symbol, String orderId, String newStopPrice) throws IOException {
        System.out.println("\n=== Change Plan Order Stop Order: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        if (newStopPrice != null) params.put("stopLossPrice", newStopPrice);
        return postSigned("/api/v1/private/planorder/change_stop_order", params);
    }

    /**
     * Get Stop Orders List
     * GET /api/v1/private/stoporder/list/orders
     */
    public JsonObject getStopOrdersList(String symbol, Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get Stop Orders List: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/stoporder/list/orders", params);
    }

    /**
     * Get Open Stop Orders
     * GET /api/v1/private/stoporder/open_orders
     */
    public JsonObject getOpenStopOrdersEnhanced(String symbol) throws IOException {
        System.out.println("\n=== Get Open Stop Orders Enhanced: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        return getSigned("/api/v1/private/stoporder/open_orders", params);
    }

    /**
     * GET /api/v1/private/trackorder/place
     */
    public JsonObject placeTrailingStopOrder(
            String symbol, int leverage, int side, String vol, int openType,
            int trend, String activePrice, int backType, String backValue,
            int positionMode, Boolean reduceOnly) throws IOException {

        System.out.println("\n=== Place Trailing Stop Order: " + symbol + " ===");
        System.out.println("Side: " + side + ", Volume: " + vol + ", Leverage: " + leverage);

        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("leverage", String.valueOf(leverage));
        params.put("side", String.valueOf(side));
        params.put("vol", vol);
        params.put("openType", String.valueOf(openType));
        params.put("trend", String.valueOf(trend));
        params.put("backType", String.valueOf(backType));
        params.put("backValue", backValue);
        params.put("positionMode", String.valueOf(positionMode));

        // 可选参数
        if (activePrice != null && !activePrice.isEmpty()) {
            params.put("activePrice", activePrice);
        }

        if (reduceOnly != null) {
            params.put("reduceOnly", String.valueOf(reduceOnly));
        }

        return postSigned("/api/v1/private/trackorder/place", params);
    }

    /**
     * Cancel Trailing Stop Order
     * POST /api/v1/private/trackorder/cancel
     */
    public JsonObject cancelTrailingStopOrder(String symbol, String orderId) throws IOException {
        System.out.println("\n=== Cancel Trailing Stop Order: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("orderId", orderId);
        return postSigned("/api/v1/private/trackorder/cancel", params);
    }

    /**
     * Change Trailing Stop Order
     * POST /api/v1/private/trackorder/change_order
     */
    public JsonObject changeTrailingStopOrder(String symbol, String orderId, String newActivationPrice) throws IOException {
        System.out.println("\n=== Change Trailing Stop Order: " + orderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("trackOrderId", orderId);
        params.put("trend", "1");
        params.put("backType", "1");
        params.put("backValue", "0.3");
        params.put("vol", "1");
        if (newActivationPrice != null)
            params.put("activePrice", newActivationPrice);
        return postSigned("/api/v1/private/trackorder/change_order", params);
    }

    /**
     * Get Trailing Stop Orders List
     * GET /api/v1/private/trackorder/list/orders
     */
    public JsonObject getTrailingStopOrdersList(String symbol, Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n=== Get Trailing Stop Orders List: " + symbol + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("states", "0");
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));
        return getSigned("/api/v1/private/trackorder/list/orders", params);
    }


    /**
     * Change Position Mode
     * POST /api/v1/private/position/change_position_mode
     */
    public JsonObject changePositionMode(String positionMode) throws IOException {
        System.out.println("\n=== Change Position Mode: " + positionMode + " " + positionMode + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("positionMode", positionMode);
        return postSigned("/api/v1/private/position/change_position_mode", params);
    }

    /**
     * Get Order Fee Details by Client Order ID
     * GET /api/v1/private/order/fee_details
     */
    public JsonObject getOrderFeeDetailsByClientId(String symbol, String clientOrderId) throws IOException {
        System.out.println("\n=== Get Order Fee Details by Client ID: " + clientOrderId + " ===");
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("clientOrderId", clientOrderId);
        return getSigned("/api/v1/private/order/fee_details", params);
    }
}