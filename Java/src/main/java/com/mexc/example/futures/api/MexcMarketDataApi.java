package com.mexc.example.futures.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MEXC Futures Market Data API
 * <p>
 * Implements all public market data endpoints
 * Inherits from MexcOkHttpClient base class
 * <p>
 * All endpoints are public and do not require authentication.
 */
public class MexcMarketDataApi extends MexcOkHttpClient {

    public MexcMarketDataApi() {
        super(); // Public client - no API keys needed
    }

    /**
     * Get Server Time (Ping)
     * GET /api/v1/contract/ping
     * <p>
     * Test connectivity and get server time
     */
    public JsonObject ping() throws IOException {
        System.out.println("\n===  Ping - Get Server Time ===");
        JsonObject response = get("/api/v1/contract/ping", null);
        if (response != null && response.get("success").getAsBoolean()) {
            System.out.println("Server timestamp: " + response.get("data"));
        }
        return response;
    }

    /**
     * Get Contract Information
     * GET /api/v1/contract/detail
     *
     * @param symbol Optional contract symbol (e.g., "BTC_USDT"). Returns all if null.
     */
    public JsonObject getContractDetail(String symbol) throws IOException {
        System.out.println("\n===  Get Contract Information" +
                (symbol != null ? ": " + symbol : " (All)") + " ===");

        Map<String, String> params = null;
        if (symbol != null) {
            params = new HashMap<>();
            params.put("symbol", symbol);
        }

        JsonObject response = get("/api/v1/contract/detail", params);

        if (response != null && response.get("success").getAsBoolean()) {
            if (symbol != null) {
                JsonObject data = response.getAsJsonObject("data");
                System.out.println("Symbol: " + data.get("symbol"));
                System.out.println("Base Coin: " + data.get("baseCoin"));
                System.out.println("Quote Coin: " + data.get("quoteCoin"));
                System.out.println("Contract Size: " + data.get("contractSize"));
                System.out.println("Max Leverage: " + data.get("maxLeverage"));
            } else {
                JsonArray data = response.getAsJsonArray("data");
                System.out.println("Retrieved " + data.size() + " contracts");
            }
        }
        return response;
    }

    /**
     * Get Transferable Currencies
     * GET /api/v1/contract/support_currencies
     */
    public JsonObject getSupportCurrencies() throws IOException {
        System.out.println("\n===  Get Transferable Currencies ===");
        JsonObject response = get("/api/v1/contract/support_currencies", null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonArray currencies = response.getAsJsonArray("data");
            System.out.println("Supported currencies: " + currencies.size());
        }
        return response;
    }

    /**
     * Get Order Book Depth
     * GET /api/v1/contract/depth/{symbol}
     *
     * @param symbol Contract symbol (required)
     * @param limit  Number of depth levels (optional, default 100)
     */
    public JsonObject getDepth(String symbol, Integer limit) throws IOException {
        System.out.println("\n===  Get Order Book Depth: " + symbol + " ===");

        String endpoint = "/api/v1/contract/depth/" + symbol;
        Map<String, String> params = null;
        if (limit != null) {
            params = new HashMap<>();
            params.put("limit", limit.toString());
        }

        JsonObject response = get(endpoint, params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Order Book Version: " + data.get("version"));
            System.out.println("Timestamp: " + data.get("timestamp"));
            System.out.println("Bids: " + data.getAsJsonArray("bids").size() + " levels");
            System.out.println("Asks: " + data.getAsJsonArray("asks").size() + " levels");
        }
        return response;
    }

    /**
     * Get Last N Depth Snapshots
     * GET /api/v1/contract/depth_commits/{symbol}/{limit}
     *
     * @param symbol Contract symbol (required)
     * @param limit  Number of snapshots to return (required)
     */
    public JsonObject getDepthCommits(String symbol, int limit) throws IOException {
        System.out.println("\n===  Get Last " + limit + " Depth Snapshots: " + symbol + " ===");

        String endpoint = "/api/v1/contract/depth_commits/" + symbol + "/" + limit;
        JsonObject response = get(endpoint, null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonArray snapshots = response.getAsJsonArray("data");
            System.out.println("Retrieved " + snapshots.size() + " snapshots");
        }
        return response;
    }

    /**
     * Get Index Price
     * GET /api/v1/contract/index_price/{symbol}
     *
     * @param symbol Contract symbol (required)
     */
    public JsonObject getIndexPrice(String symbol) throws IOException {
        System.out.println("\n=== Get Index Price: " + symbol + " ===");

        String endpoint = "/api/v1/contract/index_price/" + symbol;
        JsonObject response = get(endpoint, null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Index Price: " + data.get("indexPrice"));
        }
        return response;
    }

    /**
     * Get Fair Price
     * GET /api/v1/contract/fair_price/{symbol}
     *
     * @param symbol Contract symbol (required)
     */
    public JsonObject getFairPrice(String symbol) throws IOException {
        System.out.println("\n===  Get Fair Price: " + symbol + " ===");

        String endpoint = "/api/v1/contract/fair_price/" + symbol;
        JsonObject response = get(endpoint, null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Fair Price: " + data.get("fairPrice"));
        }
        return response;
    }

    /**
     * Get Funding Rate
     * GET /api/v1/contract/funding_rate/{symbol}
     *
     * @param symbol Contract symbol (required)
     */
    public JsonObject getFundingRate(String symbol) throws IOException {
        System.out.println("\n=== Get Funding Rate: " + symbol + " ===");

        String endpoint = "/api/v1/contract/funding_rate/" + symbol;
        JsonObject response = get(endpoint, null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Funding Rate: " + data.get("fundingRate"));
            System.out.println("Next Settlement: " + data.get("nextSettleTime"));
        }
        return response;
    }

    /**
     * Get Kline/Candlestick Data
     * GET /api/v1/contract/kline/{symbol}
     *
     * @param symbol   Contract symbol (required)
     * @param interval Kline interval: 1m,5m,15m,30m,1h,4h,1d,1w,1M
     * @param start    Optional start time in milliseconds
     * @param end      Optional end time in milliseconds
     * @param limit    Optional number of records (default 100, max 500)
     */
    public JsonObject getKline(String symbol, String interval, Long start, Long end, Integer limit) throws IOException {
        System.out.println("\n===  Get Kline Data: " + symbol + " " + interval + " ===");

        String endpoint = "/api/v1/contract/kline/" + symbol;
        Map<String, String> params = new HashMap<>();
        params.put("interval", interval);

        if (start != null) params.put("start", String.valueOf(start));
        if (end != null) params.put("end", String.valueOf(end));
        if (limit != null) params.put("limit", String.valueOf(limit));

        JsonObject response = get(endpoint, params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject klineData = response.getAsJsonObject("data");
            System.out.println("Retrieved " + klineData.size() + " kline records");
            if (!klineData.isEmpty()) {
                JsonArray jsonArray = klineData.getAsJsonArray("close");
                System.out.println("First: " + jsonArray.get(0));
            }
        }
        return response;
    }

    /**
     * Get Index Price Kline Data
     * GET /api/v1/contract/kline/index_price/{symbol}
     * <p>
     * Retrieves historical index price candlestick data.
     *
     * @param symbol   Contract symbol (required, e.g., "BTC_USDT")
     * @param interval Kline interval. Supported values:
     *                 Min1, Min5, Min15, Min30, Hour1, Hour4, Hour8, Day1, Week1, Month1
     * @param start    Start time in seconds (Unix timestamp, required)
     * @param end      End time in seconds (Unix timestamp, required)
     * @return JsonObject containing arrays of time, open, close, high, low, vol, amount, etc.
     */
    public JsonObject getIndexPriceKline(String symbol, String interval, long start, long end) throws IOException {
        System.out.println("\n===  Get Index Price Kline: " + symbol + " " + interval + " ===");
        System.out.println("Time range: " + start + " to " + end);

        String endpoint = "/api/v1/contract/kline/index_price/" + symbol;
        Map<String, String> params = new HashMap<>();
        params.put("interval", interval);
        params.put("start", String.valueOf(start));
        params.put("end", String.valueOf(end));

        JsonObject response = get(endpoint, params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            JsonArray timeArray = data.getAsJsonArray("time");
            System.out.println("Retrieved " + timeArray.size() + " index price kline records");
            // Optional: Print sample data
            if (!timeArray.isEmpty()) {
                System.out.println("First record - Time: " + timeArray.get(0) +
                        ", Open: " + data.getAsJsonArray("open").get(0) +
                        ", Close: " + data.getAsJsonArray("close").get(0));
            }
        }
        return response;
    }

    /**
     * Get Fair Price Kline Data
     * GET /api/v1/contract/kline/fair_price/{symbol}
     * <p>
     * Retrieves historical fair price candlestick data.
     *
     * @param symbol   Contract symbol (required, e.g., "BTC_USDT")
     * @param interval Kline interval. Supported values:
     *                 Min1, Min5, Min15, Min30, Hour1, Hour4, Hour8, Day1, Week1, Month1
     * @param start    Start time in seconds (Unix timestamp, required)
     * @param end      End time in seconds (Unix timestamp, required)
     * @return JsonObject containing arrays of time, open, close, high, low, vol, amount, etc.
     */
    public JsonObject getFairPriceKline(String symbol, String interval, long start, long end) throws IOException {
        System.out.println("\n===  Get Fair Price Kline: " + symbol + " " + interval + " ===");
        System.out.println("Time range: " + start + " to " + end);

        String endpoint = "/api/v1/contract/kline/fair_price/" + symbol;
        Map<String, String> params = new HashMap<>();
        params.put("interval", interval);
        params.put("start", String.valueOf(start));
        params.put("end", String.valueOf(end));

        JsonObject response = get(endpoint, params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            JsonArray timeArray = data.getAsJsonArray("time");
            System.out.println("Retrieved " + timeArray.size() + " fair price kline records");

            // Optional: Print sample data
            if (!timeArray.isEmpty()) {
                System.out.println("First record - Time: " + timeArray.get(0) +
                        ", Open: " + data.getAsJsonArray("open").get(0) +
                        ", Close: " + data.getAsJsonArray("close").get(0));
            }
        }
        return response;
    }

    /**
     * Get Recent Deals/Trades
     * GET /api/v1/contract/deals/{symbol}
     * <p>
     * Retrieves the most recent trades/deals for a specific symbol.
     * This is similar to recent trades but may include additional information.
     *
     * @param symbol Contract symbol (required)
     * @param limit  Number of deals to return (default 100, max 500)
     * @return JsonObject containing recent deals
     */
    public JsonObject getRecentDeals(String symbol, Integer limit) throws IOException {
        System.out.println("\n=== Get Recent Deals: " + symbol + " ===");

        String endpoint = "/api/v1/contract/deals/" + symbol;
        Map<String, String> params = null;
        if (limit != null) {
            params = new HashMap<>();
            params.put("limit", limit.toString());
        }

        JsonObject response = get(endpoint, params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonArray data = response.getAsJsonArray("data");
            System.out.println("Retrieved " + data.size() + " recent deals");

            if (!data.isEmpty()) {
                System.out.println("\nMost recent deals:");
                int count = Math.min(5, data.size());
                for (int i = 0; i < count; i++) {
                    JsonObject deal = data.get(i).getAsJsonObject();
                    // Format depends on API response - adjust as needed
                    System.out.println("  " + (i + 1) + ": " + deal);
                }

                // Calculate some statistics
                double totalVolume = 0;
                double totalAmount = 0;
                for (int i = 0; i < data.size(); i++) {
                    JsonObject deal = data.get(i).getAsJsonObject();
                    if (deal.size() >= 2) {
                        totalVolume += deal.get("v").getAsDouble();
                        totalAmount += deal.get("p").getAsDouble() * deal.get("v").getAsDouble();
                    }
                }
                System.out.printf("\nSummary - Total Volume: %.4f, Total Amount: %.2f%n",
                        totalVolume, totalAmount);
            }
        }
        return response;
    }

    /**
     * 23. Get All Tickers
     * GET /api/v1/contract/ticker
     * <p>
     * Retrieves all ticker information in a single call.
     * This is a comprehensive endpoint that returns price, volume, change, etc. for all symbols.
     *
     * @return JsonObject containing all ticker data
     */
    public JsonObject getAllTickers() throws IOException {
        System.out.println("\n===  Get All Tickers ===");

        JsonObject response = get("/api/v1/contract/ticker", null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonArray data = response.getAsJsonArray("data");
            System.out.println("Retrieved tickers for " + data.size() + " symbols");

            // Print summary for major symbols
            String[] majorSymbols = {"BTC_USDT", "ETH_USDT", "BNB_USDT"};
            for (String symbol : majorSymbols) {
                for (int i = 0; i < data.size(); i++) {
                    JsonObject ticker = data.get(i).getAsJsonObject();
                    if (symbol.equals(ticker.get("symbol").getAsString())) {
                        System.out.println("\n" + symbol + ":");
                        System.out.println("  Last: " + ticker.get("lastPrice"));
                        System.out.println("  riseFallRate: " + ticker.get("riseFallValue") +
                                " (" + ticker.get("riseFallRate") + "%)");
                        System.out.println("  Volume: " + ticker.get("volume24"));
                        break;
                    }
                }
            }
        }
        return response;
    }

    /**
     * Get Risk Reverse by Symbol
     * GET /api/v1/contract/risk_reverse/{symbol}
     * <p>
     * Retrieves current risk reverse (insurance fund) information for a specific symbol.
     *
     * @param symbol Contract symbol (required)
     * @return JsonObject containing risk reverse information
     */
    public JsonObject getRiskReverseBySymbol(String symbol) throws IOException {
        System.out.println("\n===  Get Risk Reverse: " + symbol + " ===");

        String endpoint = "/api/v1/contract/risk_reverse/" + symbol;
        JsonObject response = get(endpoint, null);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Symbol: " + data.get("symbol"));
            System.out.println("Risk Reverse Amount: " + data.get("available"));
            System.out.println("Update Time: " + data.get("timestamp"));
        }
        return response;
    }


    /**
     * Get Risk Reverse History
     * GET /api/v1/contract/risk_reverse/history
     * <p>
     * Retrieves historical risk reverse (insurance fund) data.
     *
     * @param symbol   Optional contract symbol
     * @param pageNum  Page number (default 1)
     * @param pageSize Page size (default 20, max 100)
     * @return JsonObject containing risk reverse history
     */
    public JsonObject getRiskReverseHistory(String symbol, Integer pageNum, Integer pageSize) throws IOException {
        System.out.println("\n===  Get Risk Reverse History" + (symbol != null ? ": " + symbol : "") + " ===");

        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        if (pageNum != null) params.put("page_num", String.valueOf(pageNum));
        if (pageSize != null) params.put("page_size", String.valueOf(pageSize));

        JsonObject response = get("/api/v1/contract/risk_reverse/history", params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Total records: " + data.get("totalCount"));
            System.out.println("Current page: " + data.get("currentPage"));
            System.out.println("Page size: " + data.get("pageSize"));

            JsonArray resultList = data.getAsJsonArray("resultList");
            System.out.println("Records in this page: " + resultList.size());

            if (!resultList.isEmpty()) {
                JsonObject first = resultList.get(0).getAsJsonObject();
                System.out.println("Sample - Symbol: " + first.get("symbol") +
                        ", Amount: " + first.get("amount") +
                        ", Time: " + first.get("createTime"));
            }
        }
        return response;
    }

    /**
     * Get Funding Rate History
     * GET /api/v1/contract/funding_rate/history
     * <p>
     * Retrieves historical funding rate data for all symbols or specific symbol.
     *
     * @param symbol Optional contract symbol (e.g., "BTC_USDT"). Returns all if null.
     * @param limit  Number of records (default 100, max 500)
     * @return JsonObject containing funding rate history
     */
    public JsonObject getFundingRateHistory(String symbol, Integer limit) throws IOException {
        System.out.println("\n===  Get Funding Rate History" + (symbol != null ? ": " + symbol : " (All)") + " ===");

        Map<String, String> params = new HashMap<>();
        if (symbol != null) params.put("symbol", symbol);
        if (limit != null) params.put("limit", String.valueOf(limit));

        JsonObject response = get("/api/v1/contract/funding_rate/history", params);

        if (response != null && response.get("success").getAsBoolean()) {
            JsonObject data = response.getAsJsonObject("data");
            System.out.println("Retrieved " + data.size() + " funding rate records");

            if (!data.isEmpty()) {
                JsonArray array = data.get("resultList").getAsJsonArray();
                JsonObject first = array.get(0).getAsJsonObject();
                System.out.println("Sample - Symbol: " + first.get("symbol") +
                        ", Rate: " + first.get("fundingRate") +
                        ", Time: " + first.get("settleTime"));
            }
        }
        return response;
    }
}