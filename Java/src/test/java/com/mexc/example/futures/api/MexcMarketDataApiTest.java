package com.mexc.example.futures.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit Test Class for MexcMarketDataApi
 * 
 * Tests all public market data endpoints:
 * 1. Basic Market Endpoints
 * 2. Order Book Endpoints
 * 3. Price Index Endpoints
 * 4. Kline Endpoints
 * 5. Trade Endpoints
 * 6. Ticker Endpoints
 * 7. Risk Reverse Endpoints
 * 8. Funding Rate Endpoints
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MexcMarketDataApiTest {

    private MexcMarketDataApi client;

    private static final String TEST_SYMBOL = "BTC_USDT";
    private static final String TEST_INTERVAL = "Min15";
    private static final int TEST_PAGE_NUM = 1;
    private static final int TEST_PAGE_SIZE = 20;
    private static final int TEST_LIMIT = 10;

    @BeforeEach
    void setUp() {
        client = new MexcMarketDataApi();
    }

    // =========================================================================
    // 1. BASIC MARKET ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Basic Market Endpoints Tests")
    class BasicMarketTests {

        @Test
        @Order(1)
        @DisplayName("Test Ping - Get Server Time")
        void testPing() throws IOException {
            JsonObject response = client.ping();
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            assertThat(response.get("data")).isNotNull();
        }

        @Test
        @Order(2)
        @DisplayName("Test Get Contract Detail - All Contracts")
        void testGetContractDetailAll() throws IOException {
            JsonObject response = client.getContractDetail(null);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonArray data = response.getAsJsonArray("data");
            assertThat(data).isNotNull();
            assertThat(data.size()).isGreaterThan(0);
        }

        @Test
        @Order(3)
        @DisplayName("Test Get Contract Detail - Single Symbol")
        void testGetContractDetailBySymbol() throws IOException {
            JsonObject response = client.getContractDetail(TEST_SYMBOL);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol").getAsString()).isEqualTo(TEST_SYMBOL);
            assertThat(data.get("baseCoin")).isNotNull();
            assertThat(data.get("quoteCoin")).isNotNull();
            assertThat(data.get("maxLeverage")).isNotNull();
        }

        @Test
        @Order(4)
        @DisplayName("Test Get Transferable Currencies")
        void testGetSupportCurrencies() throws IOException {
            JsonObject response = client.getSupportCurrencies();
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonArray currencies = response.getAsJsonArray("data");
            assertThat(currencies).isNotNull();
            assertThat(currencies.size()).isGreaterThan(0);
        }
    }

    // =========================================================================
    // 2. ORDER BOOK ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Order Book Endpoints Tests")
    class OrderBookTests {

        @Test
        @Order(5)
        @DisplayName("Test Get Depth - Default Limit")
        void testGetDepthDefault() throws IOException {
            JsonObject response = client.getDepth(TEST_SYMBOL, null);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("version")).isNotNull();
            assertThat(data.get("timestamp")).isNotNull();
            assertThat(data.getAsJsonArray("bids")).isNotNull();
            assertThat(data.getAsJsonArray("asks")).isNotNull();
        }

        @Test
        @Order(6)
        @DisplayName("Test Get Depth - With Limit")
        void testGetDepthWithLimit() throws IOException {
            JsonObject response = client.getDepth(TEST_SYMBOL, TEST_LIMIT);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.getAsJsonArray("bids").size()).isLessThanOrEqualTo(TEST_LIMIT);
            assertThat(data.getAsJsonArray("asks").size()).isLessThanOrEqualTo(TEST_LIMIT);
        }

        @Test
        @Order(7)
        @DisplayName("Test Get Depth Commits")
        void testGetDepthCommits() throws IOException {
            int limit = 5;
            JsonObject response = client.getDepthCommits(TEST_SYMBOL, limit);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonArray snapshots = response.getAsJsonArray("data");
            assertThat(snapshots).isNotNull();
            assertThat(snapshots.size()).isLessThanOrEqualTo(limit);
        }
    }

    // =========================================================================
    // 3. PRICE INDEX ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Price Index Endpoints Tests")
    class PriceIndexTests {

        @Test
        @Order(8)
        @DisplayName("Test Get Index Price")
        void testGetIndexPrice() throws IOException {
            JsonObject response = client.getIndexPrice(TEST_SYMBOL);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol").getAsString()).isEqualTo(TEST_SYMBOL);
            assertThat(data.get("indexPrice")).isNotNull();
            assertThat(data.get("timestamp")).isNotNull();
        }

        @Test
        @Order(9)
        @DisplayName("Test Get Fair Price")
        void testGetFairPrice() throws IOException {
            JsonObject response = client.getFairPrice(TEST_SYMBOL);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol").getAsString()).isEqualTo(TEST_SYMBOL);
            assertThat(data.get("fairPrice")).isNotNull();
            assertThat(data.get("timestamp")).isNotNull();
        }
    }

    // =========================================================================
    // 4. FUNDING RATE ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Funding Rate Endpoints Tests")
    class FundingRateTests {

        @Test
        @Order(10)
        @DisplayName("Test Get Funding Rate")
        void testGetFundingRate() throws IOException {
            JsonObject response = client.getFundingRate(TEST_SYMBOL);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol").getAsString()).isEqualTo(TEST_SYMBOL);
            assertThat(data.get("fundingRate")).isNotNull();
            assertThat(data.get("nextSettleTime")).isNotNull();
        }

        @Test
        @Order(11)
        @DisplayName("Test Get Funding Rate History")
        void testGetFundingRateHistory() throws IOException {
            JsonObject response = client.getFundingRateHistory(TEST_SYMBOL, 10);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 5. KLINE ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Kline Endpoints Tests")
    class KlineTests {

        private long endTime;
        private long startTime;

        @BeforeEach
        void setUp() {
            endTime = System.currentTimeMillis() / 1000;
            startTime = endTime - 7L * 24 * 60 * 60;
        }

        @Test
        @Order(12)
        @DisplayName("Test Get Kline Data")
        void testGetKline() throws IOException {
            JsonObject response = client.getKline(TEST_SYMBOL, TEST_INTERVAL, startTime, endTime, 100);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
        }

        @Test
        @Order(13)
        @DisplayName("Test Get Index Price Kline")
        void testGetIndexPriceKline() throws IOException {
            JsonObject response = client.getIndexPriceKline(TEST_SYMBOL, TEST_INTERVAL, startTime, endTime);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            
            JsonArray timeArray = data.getAsJsonArray("time");
            assertThat(timeArray).isNotNull();
        }

        @Test
        @Order(14)
        @DisplayName("Test Get Fair Price Kline")
        void testGetFairPriceKline() throws IOException {
            JsonObject response = client.getFairPriceKline(TEST_SYMBOL, TEST_INTERVAL, startTime, endTime);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            
            JsonArray timeArray = data.getAsJsonArray("time");
            assertThat(timeArray).isNotNull();
        }

        @ParameterizedTest
        @Order(15)
        @DisplayName("Test Get Kline with Different Intervals")
        @ValueSource(strings = {"Min1", "Min5", "Min15", "Hour4", "Day1"})
        void testGetKlineWithDifferentIntervals(String interval) throws IOException {
            JsonObject response = client.getKline(TEST_SYMBOL, interval, startTime, endTime, 10);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 6. TRADE ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Trade Endpoints Tests")
    class TradeTests {

        @Test
        @Order(16)
        @DisplayName("Test Get Recent Deals")
        void testGetRecentDeals() throws IOException {
            JsonObject response = client.getRecentDeals(TEST_SYMBOL, 20);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonArray data = response.getAsJsonArray("data");
            assertThat(data).isNotNull();
        }

        @Test
        @Order(17)
        @DisplayName("Test Get Recent Deals - Default Limit")
        void testGetRecentDealsDefault() throws IOException {
            JsonObject response = client.getRecentDeals(TEST_SYMBOL, null);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 7. TICKER ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Ticker Endpoints Tests")
    class TickerTests {

        @Test
        @Order(18)
        @DisplayName("Test Get All Tickers")
        void testGetAllTickers() throws IOException {
            JsonObject response = client.getAllTickers();
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonArray data = response.getAsJsonArray("data");
            assertThat(data).isNotNull();
            assertThat(data.size()).isGreaterThan(0);
        }
    }

    // =========================================================================
    // 8. RISK REVERSE ENDPOINTS
    // =========================================================================

    @Nested
    @DisplayName("Risk Reverse Endpoints Tests")
    class RiskReverseTests {

        @Test
        @Order(19)
        @DisplayName("Test Get Risk Reverse by Symbol")
        void testGetRiskReverseBySymbol() throws IOException {
            JsonObject response = client.getRiskReverseBySymbol(TEST_SYMBOL);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol")).isNotNull();
        }

        @Test
        @Order(20)
        @DisplayName("Test Get Risk Reverse History")
        void testGetRiskReverseHistory() throws IOException {
            JsonObject response = client.getRiskReverseHistory(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("totalCount")).isNotNull();
            assertThat(data.get("currentPage")).isNotNull();
        }

    }

    // =========================================================================
    // 9. PARAMETERIZED TESTS
    // =========================================================================

    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedTests {

        @ParameterizedTest
        @Order(21)
        @DisplayName("Test Get Contract Detail for Different Symbols")
        @ValueSource(strings = {"BTC_USDT", "ETH_USDT", "BNB_USDT"})
        void testGetContractDetailForDifferentSymbols(String symbol) throws IOException {
            JsonObject response = client.getContractDetail(symbol);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol").getAsString()).isEqualTo(symbol);
        }

        @ParameterizedTest
        @Order(22)
        @DisplayName("Test Get Depth with Different Limits")
        @ValueSource(ints = {5, 10, 20, 50})
        void testGetDepthWithDifferentLimits(int limit) throws IOException {
            JsonObject response = client.getDepth(TEST_SYMBOL, limit);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @ParameterizedTest
        @Order(23)
        @DisplayName("Test Get Index Price for Different Symbols")
        @ValueSource(strings = {"BTC_USDT", "ETH_USDT", "BNB_USDT"})
        void testGetIndexPriceForDifferentSymbols(String symbol) throws IOException {
            JsonObject response = client.getIndexPrice(symbol);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            
            JsonObject data = response.getAsJsonObject("data");
            assertThat(data).isNotNull();
            assertThat(data.get("symbol").getAsString()).isEqualTo(symbol);
        }

        @ParameterizedTest
        @Order(24)
        @DisplayName("Test Get Funding Rate for Different Symbols")
        @ValueSource(strings = {"BTC_USDT", "ETH_USDT", "BNB_USDT"})
        void testGetFundingRateForDifferentSymbols(String symbol) throws IOException {
            JsonObject response = client.getFundingRate(symbol);
            
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 10. ERROR HANDLING TESTS
    // =========================================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @Order(25)
        @DisplayName("Test Get Contract Detail with Invalid Symbol")
        void testGetContractDetailInvalidSymbol() throws IOException {
            JsonObject response = client.getContractDetail("INVALID_SYMBOL");
            
            assertThat(response).isNotNull();
            // Should return success=false with error code
        }

        @Test
        @Order(26)
        @DisplayName("Test Get Depth with Invalid Symbol")
        void testGetDepthInvalidSymbol() throws IOException {
            JsonObject response = client.getDepth("INVALID_SYMBOL", 10);
            
            assertThat(response).isNotNull();
        }

        @Test
        @Order(27)
        @DisplayName("Test Get Index Price with Invalid Symbol")
        void testGetIndexPriceInvalidSymbol() throws IOException {
            JsonObject response = client.getIndexPrice("INVALID_SYMBOL");
            
            assertThat(response).isNotNull();
        }

        @Test
        @Order(28)
        @DisplayName("Test Get Funding Rate with Invalid Symbol")
        void testGetFundingRateInvalidSymbol() throws IOException {
            JsonObject response = client.getFundingRate("INVALID_SYMBOL");
            
            assertThat(response).isNotNull();
        }

        @Test
        @Order(29)
        @DisplayName("Test Get Kline with Invalid Interval")
        void testGetKlineInvalidInterval() throws IOException {
            long endTime = System.currentTimeMillis() / 1000;
            long startTime = endTime - 7L * 24 * 60 * 60;
            
            JsonObject response = client.getKline(TEST_SYMBOL, "INVALID_INTERVAL", startTime, endTime, 10);
            
            assertThat(response).isNotNull();
        }
    }

    // =========================================================================
    // 11. EDGE CASE TESTS
    // =========================================================================

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @Order(30)
        @DisplayName("Test Get Depth Commits with Zero Limit")
        void testGetDepthCommitsZeroLimit() throws IOException {
            JsonObject response = client.getDepthCommits(TEST_SYMBOL, 0);
            
            assertThat(response).isNotNull();
        }

        @Test
        @Order(31)
        @DisplayName("Test Get Recent Deals with Large Limit")
        void testGetRecentDealsLargeLimit() throws IOException {
            JsonObject response = client.getRecentDeals(TEST_SYMBOL, 1000); // Max should be 500
            
            assertThat(response).isNotNull();
        }

        @Test
        @Order(32)
        @DisplayName("Test Get Risk Reverse History with Large Page Size")
        void testGetRiskReverseHistoryLargePageSize() throws IOException {
            JsonObject response = client.getRiskReverseHistory(TEST_SYMBOL, 1, 200); // Max should be 100
            
            assertThat(response).isNotNull();
        }
    }
}