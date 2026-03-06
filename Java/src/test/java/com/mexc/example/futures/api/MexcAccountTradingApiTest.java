package com.mexc.example.futures.api;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive JUnit Test Class for MexcAccountTradingApi
 * <p>
 * Tests are organized by business modules based on existing test methods:
 * 1. Account Information & Assets
 * 2. Asset Analysis & PnL
 * 3. Fee & Rate Information
 * 4. Position Management
 * 5. Order Placement
 * 6. Order Cancellation
 * 7. Order Query
 * 8. Plan Orders
 * 9. Stop Orders
 * 10. Trailing Stop Orders
 * 11. Parameterized Tests
 * 12. Error Handling Tests
 * 13. Null Parameter Tests
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MexcAccountTradingApiTest {

    private MexcAccountTradingApi client;

    private static final String TEST_SYMBOL = "BTC_USDT";
    private static final String TEST_CURRENCY = "USDT";
    private static final String TEST_ORDER_ID = "781607202292061184";
    private static final String TEST_CLIENT_ORDER_ID = "test-order-123";
    private static final String TEST_POSITION_ID = "1283140099";
    private static final String TEST_EXTERNAL_OID = "_m_e9bce4c3c663433e878a216b126341e5";
    private static final String TEST_BLACKLIST_CONFIG_NAME = "test-blacklist-config";
    private static final String TEST_MEMO = "Test blacklist entry";
    private static final int TEST_PAGE_NUM = 1;
    private static final int TEST_PAGE_SIZE = 20;
    private static final int TEST_LEVERAGE = 10;
    private static final String TEST_PRICE = "50000";
    private static final String TEST_VOLUME = "1";
    private static final String TEST_AMOUNT = "1";

    @BeforeEach
    void setUp() {
        String apiKey = System.getenv("MEXC_API_KEY");
        String secretKey = System.getenv("MEXC_SECRET_KEY");

        if (apiKey != null && secretKey != null) {
            client = new MexcAccountTradingApi(apiKey, secretKey);
        } else {
            client = new MexcAccountTradingApi("mock-api-key", "mock-secret-key");
        }
    }

    // =========================================================================
    // 1. ACCOUNT INFORMATION & ASSETS
    // =========================================================================

    @Nested
    @DisplayName("Account Information & Assets Tests")
    class AccountInfoTests {

        @Test
        @Order(1)
        @DisplayName("Test Get All Account Assets")
        void testGetAllAccountAssets() throws IOException {
            JsonObject response = client.getAllAccountAssets();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(2)
        @DisplayName("Test Get Single Currency Asset")
        void testGetSingleAsset() throws IOException {
            JsonObject response = client.getSingleAsset(TEST_CURRENCY);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(3)
        @DisplayName("Test Get Asset Transfer Records")
        void testGetTransferRecord() throws IOException {
            JsonObject response = client.getTransferRecord(null, null, null, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(4)
        @DisplayName("Test Get Transfer Records with Filters")
        void testGetTransferRecordWithFilters() throws IOException {
            JsonObject response = client.getTransferRecord(TEST_CURRENCY, "SUCCESS", "IN", TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(5)
        @DisplayName("Test Get Personal Profit Rate - Daily")
        void testGetProfitRateDaily() throws IOException {
            JsonObject response = client.getProfitRate(1);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(6)
        @DisplayName("Test Get Personal Profit Rate - Weekly")
        void testGetProfitRateWeekly() throws IOException {
            JsonObject response = client.getProfitRate(2);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(7)
        @DisplayName("Test Get Account Configuration")
        void testGetAccountConfig() throws IOException {
            JsonObject response = client.getAccountConfig();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(8)
        @DisplayName("Test Get Account Discount Type")
        void testGetAccountDiscountType() throws IOException {
            JsonObject response = client.getAccountDiscountType();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 2. ASSET ANALYSIS & PNL
    // =========================================================================

    @Nested
    @DisplayName("Asset Analysis & PnL Tests")
    class AssetAnalysisTests {

        @Test
        @Order(9)
        @DisplayName("Test Get Asset Analysis - This Week")
        void testGetAssetAnalysisThisWeek() throws IOException {
            JsonObject response = client.getAssetAnalysis(1, TEST_CURRENCY, null, null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(10)
        @DisplayName("Test Get Asset Analysis - This Month")
        void testGetAssetAnalysisThisMonth() throws IOException {
            JsonObject response = client.getAssetAnalysis(2, TEST_CURRENCY, null, null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(11)
        @DisplayName("Test Get Asset Analysis - All Time")
        void testGetAssetAnalysisAllTime() throws IOException {
            JsonObject response = client.getAssetAnalysis(3, TEST_CURRENCY, null, null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(12)
        @DisplayName("Test Get Asset Analysis - Custom Range")
        void testGetAssetAnalysisCustom() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getAssetAnalysis(4, TEST_CURRENCY, startTime, endTime);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(13)
        @DisplayName("Test Get Yesterday's PnL")
        void testGetYesterdayPnl() throws IOException {
            JsonObject response = client.getYesterdayPnl();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(14)
        @DisplayName("Test Post Asset Analysis V3")
        void testPostAssetAnalysisV3() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.postAssetAnalysisV3(startTime, endTime, 1, 1, TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(15)
        @DisplayName("Test Post Daily Calendar Analysis V3")
        void testPostCalendarDailyV3() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.postCalendarDailyV3(startTime, endTime, 1, 1);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(16)
        @DisplayName("Test Post Monthly Calendar Analysis V3")
        void testPostCalendarMonthlyV3() throws IOException {
            JsonObject response = client.postCalendarMonthlyV3(1, 1);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(17)
        @DisplayName("Test Post Recent Analysis V3")
        void testPostRecentAnalysisV3() throws IOException {
            JsonObject response = client.postRecentAnalysisV3(1, 1, TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(18)
        @DisplayName("Test Get Today's PnL")
        void testGetTodayPnl() throws IOException {
            JsonObject response = client.getTodayPnl();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(19)
        @DisplayName("Test Export Asset Analysis")
        void testExportAssetAnalysis() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.exportAssetAnalysis(startTime, endTime, TEST_SYMBOL, 1, 1);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 3. FEE & RATE INFORMATION
    // =========================================================================

    @Nested
    @DisplayName("Fee & Rate Information Tests")
    class FeeRateTests {

        @Test
        @Order(20)
        @DisplayName("Test Get Fee Deduction Configurations")
        void testGetFeeDeductConfigs() throws IOException {
            JsonObject response = client.getFeeDeductConfigs();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(21)
        @DisplayName("Test Get User Tiered Fee Rate V2")
        void testGetTieredFeeRateV2() throws IOException {
            JsonObject response = client.getTieredFeeRateV2();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }


        @Test
        @Order(22)
        @DisplayName("Test Get Contract Fee Discount Configuration")
        void testGetContractFeeDiscountConfig() throws IOException {
            JsonObject response = client.getContractFeeDiscountConfig();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(23)
        @DisplayName("Test Get Order Fee Details")
        void testGetOrderFeeDetails() throws IOException {
            JsonObject response = client.getOrderFeeDetails(TEST_SYMBOL, TEST_ORDER_ID);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(24)
        @DisplayName("Test Get Order Fee Details by Client ID")
        void testGetOrderFeeDetailsByClientId() throws IOException {
            JsonObject response = client.getOrderFeeDetailsByClientId(TEST_SYMBOL, TEST_CLIENT_ORDER_ID);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(25)
        @DisplayName("Test Get Total Order Deal Fee")
        void testGetTotalOrderDealFee() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getTotalOrderDealFee(startTime, endTime, TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(26)
        @DisplayName("Test Get Contract Fee Rate")
        void testGetContractFeeRate() throws IOException {
            JsonObject response = client.getContractFeeRate(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(27)
        @DisplayName("Test Get Zero Fee Rate Contracts")
        void testGetZeroFeeRateContracts() throws IOException {
            JsonObject response = client.getZeroFeeRateContracts();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(28)
        @DisplayName("Test Get Account Risk Limit")
        void testGetAccountRiskLimit() throws IOException {
            JsonObject response = client.getAccountRiskLimit();
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 4. POSITION MANAGEMENT
    // =========================================================================

    @Nested
    @DisplayName("Position Management Tests")
    class PositionManagementTests {

        @Test
        @Order(29)
        @DisplayName("Test Get History Positions")
        void testGetHistoryPositions() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getHistoryPositions(TEST_SYMBOL, startTime, endTime, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(30)
        @DisplayName("Test Get Open Positions")
        void testGetOpenPositions() throws IOException {
            JsonObject response = client.getOpenPositions(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(31)
        @DisplayName("Test Get Funding Records")
        void testGetFundingRecords() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getFundingRecords(TEST_SYMBOL, startTime, endTime, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(32)
        @DisplayName("Test Get Position Leverage")
        void testGetPositionLeverage() throws IOException {
            JsonObject response = client.getPositionLeverage(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(33)
        @DisplayName("Test Get Position Mode")
        void testGetPositionMode() throws IOException {
            JsonObject response = client.getPositionMode(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(34)
        @DisplayName("Test Change Position Margin - Add")
        void testChangePositionMarginAdd() throws IOException {
            JsonObject response = client.changePositionMargin(TEST_POSITION_ID, TEST_AMOUNT, "ADD");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(35)
        @DisplayName("Test Change Position Margin - SUB")
        void testChangePositionMarginReduce() throws IOException {
            JsonObject response = client.changePositionMargin(TEST_POSITION_ID, TEST_AMOUNT, "SUB");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(36)
        @DisplayName("Test Change Auto Add Margin - Enable")
        void testChangeAutoAddMarginEnable() throws IOException {
            JsonObject response = client.changeAutoAddMargin(TEST_POSITION_ID, true);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(37)
        @DisplayName("Test Change Auto Add Margin - Disable")
        void testChangeAutoAddMarginDisable() throws IOException {
            JsonObject response = client.changeAutoAddMargin(TEST_POSITION_ID, false);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(38)
        @DisplayName("Test Change Leverage")
        void testChangeLeverage() throws IOException {
            JsonObject response = client.changeLeverage(TEST_POSITION_ID, TEST_SYMBOL, TEST_LEVERAGE, "2", "2");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(39)
        @DisplayName("Test Change Position Mode")
        void testChangePositionMode() throws IOException {
            JsonObject response = client.changePositionMode("1");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(40)
        @DisplayName("Test Reverse Position")
        void testReversePosition() throws IOException {
            JsonObject response = client.reversePosition(TEST_SYMBOL, TEST_POSITION_ID, TEST_AMOUNT);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(41)
        @DisplayName("Test Close All Positions")
        void testCloseAllPositions() throws IOException {
            JsonObject response = client.closeAllPositions(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 5. ORDER PLACEMENT
    // =========================================================================

    @Nested
    @DisplayName("Order Placement Tests")
    class OrderPlacementTests {

        @Test
        @Order(42)
        @DisplayName("Test Place Order")
        void testPlaceOrder() throws IOException {
            JsonObject response = client.placeOrder(
                    TEST_SYMBOL, "1", "1", "1",
                    TEST_LEVERAGE, TEST_PRICE, TEST_VOLUME, "1", false
            );
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(43)
        @DisplayName("Test Place Batch Orders")
        void testPlaceBatchOrders() throws IOException {
            JsonObject order1 = new JsonObject();
            order1.addProperty("symbol", TEST_SYMBOL);
            order1.addProperty("price", TEST_PRICE);
            order1.addProperty("vol", TEST_VOLUME);
            order1.addProperty("leverage", TEST_LEVERAGE);
            order1.addProperty("side", "1");
            order1.addProperty("type", "2");
            order1.addProperty("openType", "2");
            order1.addProperty("externalOid", UUID.randomUUID().toString());

            List<Object> batchOrders = new ArrayList<>();
            batchOrders.add(order1);
            JsonObject response = client.placeBatchOrders(batchOrders);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(44)
        @DisplayName("Test Chase Limit Order")
        void testChaseLimitOrder() throws IOException {
            JsonObject response = client.chaseLimitOrder("781672424696530432");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(45)
        @DisplayName("Test Change Limit Order")
        void testChangeLimitOrder() throws IOException {
            JsonObject response = client.changeLimitOrder("781672424696530432", "51000", "2");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 6. ORDER CANCELLATION
    // =========================================================================

    @Nested
    @DisplayName("Order Cancellation Tests")
    class OrderCancellationTests {

        @Test
        @Order(46)
        @DisplayName("Test Cancel Order")
        void testCancelOrder() throws IOException {
            List<Object> orderIds = new ArrayList<>();
            orderIds.add("781672424696530432");
            JsonObject response = client.cancelOrder(orderIds);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(47)
        @DisplayName("Test Batch Cancel with External IDs")
        void testBatchCancelWithExternal() throws IOException {
            Map<String, String> params = new HashMap<>();
            params.put("symbol", TEST_SYMBOL);
            params.put("externalOid", "_m_6112519dcd9e40378323a00a3fdf61da");

            List<Object> cancelParams = new ArrayList<>();
            cancelParams.add(params);

            JsonObject response = client.batchCancelWithExternal(cancelParams);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(48)
        @DisplayName("Test Cancel with External ID")
        void testCancelWithExternal() throws IOException {
            Map<String, String> params = new HashMap<>();
            params.put("symbol", TEST_SYMBOL);
            params.put("externalOid", "_m_1663cc7b4d1d49848695edf4f34158a4");
            JsonObject response = client.cancelWithExternal(params);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(49)
        @DisplayName("Test Cancel All Orders")
        void testCancelAllOrders() throws IOException {
            JsonObject response = client.cancelAllOrders(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 7. ORDER QUERY
    // =========================================================================

    @Nested
    @DisplayName("Order Query Tests")
    class OrderQueryTests {

        @Test
        @Order(50)
        @DisplayName("Test Get Order by External ID")
        void testGetOrderByExternalId() throws IOException {
            JsonObject response = client.getOrderByExternalId(TEST_SYMBOL, TEST_EXTERNAL_OID);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(51)
        @DisplayName("Test Get Order by ID")
        void testGetOrderById() throws IOException {
            JsonObject response = client.getOrderById("781910101266088960");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(52)
        @DisplayName("Test Batch Query Orders")
        void testBatchQueryOrders() throws IOException {
            JsonObject response = client.batchQueryOrders(TEST_ORDER_ID);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(53)
        @DisplayName("Test Batch Query with External IDs")
        void testBatchQueryWithExternal() throws IOException {
            Map<String, String> params = new HashMap<>();
            params.put("symbol", TEST_SYMBOL);
            params.put("externalOid", TEST_EXTERNAL_OID);

            List<Object> queryParams = new ArrayList<>();
            queryParams.add(params);

            JsonObject response = client.batchQueryWithExternal(queryParams);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(54)
        @DisplayName("Test Get All Open Orders")
        void testGetListOpenOrders() throws IOException {
            JsonObject response = client.getListOpenOrders(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(55)
        @DisplayName("Test Get History Orders")
        void testGetHistoryOrders() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getHistoryOrders(TEST_SYMBOL, startTime, endTime, TEST_PAGE_NUM, TEST_PAGE_SIZE, null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(56)
        @DisplayName("Test Get Order Deals V3")
        void testGetOrderDealsV3() throws IOException {
            JsonObject response = client.getOrderDealsV3(TEST_SYMBOL, TEST_ORDER_ID, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(57)
        @DisplayName("Test Get Deal Details by Order ID")
        void testGetDealDetailsByOrderId() throws IOException {
            JsonObject response = client.getDealDetailsByOrderId(TEST_ORDER_ID);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(58)
        @DisplayName("Test Get Deal Details")
        void testGetDealDetails() throws IOException {
            JsonObject response = client.getDealDetails(TEST_ORDER_ID);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(59)
        @DisplayName("Test Get Close Orders")
        void testGetCloseOrders() throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getCloseOrders(TEST_SYMBOL, startTime, endTime, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }
    }

    // =========================================================================
    // 8. PLAN ORDERS
    // =========================================================================

    @Nested
    @DisplayName("Plan Orders Tests")
    class PlanOrderTests {

        @Test
        @Order(60)
        @DisplayName("Test Get Plan Orders")
        void testGetPlanOrders() throws IOException {
            JsonObject response = client.getPlanOrders(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(61)
        @DisplayName("Test Place Plan Order V2")
        void testPlacePlanOrderV2() throws IOException {
            JsonObject response = client.placePlanOrderV2(
                    TEST_SYMBOL, "1", "1", TEST_LEVERAGE,
                    "2", "50000", "5", "50000", TEST_VOLUME, "1", "1"
            );
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(62)
        @DisplayName("Test Change Plan Order Price")
        void testChangePlanOrderPrice() throws IOException {
            JsonObject response = client.changePlanOrderPrice(TEST_SYMBOL, "781673679430960128", "51000", "51000", "1");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(63)
        @DisplayName("Test Cancel Plan Order")
        void testCancelPlanOrder() throws IOException {
            Map<String, String> params = new HashMap<>();
            params.put("symbol", TEST_SYMBOL);
            params.put("orderId", "781673679430960128");
            List<Object> paramsList = new ArrayList<>();
            paramsList.add(params);
            JsonObject response = client.cancelPlanOrder(paramsList);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(64)
        @DisplayName("Test Cancel All Plan Orders")
        void testCancelAllPlanOrders() throws IOException {
            JsonObject response = client.cancelAllPlanOrders(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 9. STOP ORDERS
    // =========================================================================

    @Nested
    @DisplayName("Stop Orders Tests")
    class StopOrderTests {

        @Test
        @Order(65)
        @DisplayName("Test Place Stop Order")
        void testPlaceStopOrder() throws IOException {
            JsonObject response = client.placeStopOrder(
                    TEST_SYMBOL, 1, 1, TEST_POSITION_ID, 1, 1, "20000", "20000");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(66)
        @DisplayName("Test Cancel Stop Order")
        void testCancelStopOrderEnhanced() throws IOException {
            JsonObject response = client.cancelStopOrder(TEST_SYMBOL, "470962178");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(67)
        @DisplayName("Test Cancel All Stop Orders")
        void testCancelAllStopOrdersEnhanced() throws IOException {
            JsonObject response = client.cancelAllStopOrdersEnhanced(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(68)
        @DisplayName("Test Change Stop Price For Limit Order")
        void testChangeStopOrderPrice() throws IOException {
            JsonObject response = client.changeStopOrderPrice("781904986492716544", "53000");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(69)
        @DisplayName("Test Change Stop Order Plan Price")
        void testChangeStopOrderPlanPrice() throws IOException {
            JsonObject response = client.changeStopOrderPlanPrice(TEST_SYMBOL, "470962178", "52000");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(70)
        @DisplayName("Test Change Plan Order Stop Order")
        void testChangePlanOrderStopOrder() throws IOException {
            JsonObject response = client.changePlanOrderStopOrder(TEST_SYMBOL, "781906241524945408", "43000");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(71)
        @DisplayName("Test Get Stop Orders List")
        void testGetStopOrdersList() throws IOException {
            JsonObject response = client.getStopOrdersList(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(72)
        @DisplayName("Test Get Open Stop Orders")
        void testGetOpenStopOrdersEnhanced() throws IOException {
            JsonObject response = client.getOpenStopOrdersEnhanced(TEST_SYMBOL);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }
    }

    // =========================================================================
    // 10. TRAILING STOP ORDERS
    // =========================================================================

    @Nested
    @DisplayName("Trailing Stop Orders Tests")
    class TrailingStopOrderTests {

        @Test
        @Order(73)
        @DisplayName("Test Place Trailing Stop Order")
        void testPlaceTrailingStopOrderEnhanced() throws IOException {
            JsonObject response = client.placeTrailingStopOrder(
                    "BTC_USDT", 3, 1, "1", 1, 1, "50000", 1, "0.2", 1, true
            );
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }

        @Test
        @Order(74)
        @DisplayName("Test Cancel Trailing Stop Order")
        void testCancelTrailingStopOrder() throws IOException {
            JsonObject response = client.cancelTrailingStopOrder(TEST_SYMBOL, "781677246015426560");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(75)
        @DisplayName("Test Change Trailing Stop Order")
        void testChangeTrailingStopOrder() throws IOException {
            JsonObject response = client.changeTrailingStopOrder(TEST_SYMBOL, "781677246015426560", "56000");
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(76)
        @DisplayName("Test Get Trailing Stop Orders List")
        void testGetTrailingStopOrdersList() throws IOException {
            JsonObject response = client.getTrailingStopOrdersList(TEST_SYMBOL, TEST_PAGE_NUM, TEST_PAGE_SIZE);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
            System.out.println(response);
        }
    }

    // =========================================================================
    // 11. PARAMETERIZED TESTS
    // =========================================================================

    @Nested
    @DisplayName("Parameterized Tests")
    class ParameterizedTests {

        @ParameterizedTest
        @Order(77)
        @DisplayName("Test Get Single Asset with Different Currencies")
        @ValueSource(strings = {"USDT", "BTC", "ETH", "BNB"})
        void testGetSingleAssetParameterized(String currency) throws IOException {
            JsonObject response = client.getSingleAsset(currency);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @ParameterizedTest
        @Order(78)
        @DisplayName("Test Get Profit Rate with Different Types")
        @ValueSource(ints = {1, 2})
        void testGetProfitRateParameterized(int type) throws IOException {
            JsonObject response = client.getProfitRate(type);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @ParameterizedTest
        @Order(79)
        @DisplayName("Test Get Asset Analysis with Different Types")
        @ValueSource(ints = {1, 2, 3})
        void testGetAssetAnalysisParameterized(int type) throws IOException {
            long endTime = System.currentTimeMillis();
            long startTime = endTime - 30L * 24 * 60 * 60 * 1000;
            JsonObject response = client.getAssetAnalysis(type, TEST_CURRENCY,
                    type == 4 ? startTime : null, type == 4 ? endTime : null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @ParameterizedTest
        @Order(80)
        @DisplayName("Test Get Contract Fee Rate with Different Symbols")
        @ValueSource(strings = {"BTC_USDT", "ETH_USDT", "BNB_USDT"})
        void testGetContractFeeRateParameterized(String symbol) throws IOException {
            JsonObject response = client.getContractFeeRate(symbol);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }

    // =========================================================================
    // 13. ERROR HANDLING TESTS
    // =========================================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @Order(81)
        @DisplayName("Test Get Single Asset with Invalid Currency")
        void testGetSingleAssetInvalidCurrency() throws IOException {
            JsonObject response = client.getSingleAsset("INVALID_CURRENCY");
            assertThat(response).isNotNull();
        }

        @Test
        @Order(82)
        @DisplayName("Test Get Transfer Records with Invalid Page Size")
        void testGetTransferRecordInvalidPageSize() throws IOException {
            JsonObject response = client.getTransferRecord(null, null, null, 1, 1000);
            assertThat(response).isNotNull();
        }

        @Test
        @Order(83)
        @DisplayName("Test Place Order with Invalid Symbol")
        void testPlaceOrderInvalidSymbol() throws IOException {
            JsonObject response = client.placeOrder(
                    "INVALID_SYMBOL", "LIMIT", "BUY", "ISOLATED",
                    TEST_LEVERAGE, TEST_PRICE, TEST_VOLUME, "ONE_WAY", false
            );
            assertThat(response).isNotNull();
        }

        @Test
        @Order(84)
        @DisplayName("Test Get Order Detail with Invalid Order ID")
        void testGetOrderDetailInvalidOrderId() throws IOException {
            JsonObject response = client.getOrderById("invalid-order-id");
            assertThat(response).isNotNull();
        }
    }

    // =========================================================================
    // 14. NULL PARAMETER TESTS
    // =========================================================================

    @Nested
    @DisplayName("Null Parameter Tests")
    class NullParameterTests {

        @Test
        @Order(85)
        @DisplayName("Test Get Open Orders with Null Symbol")
        void testGetOpenOrdersNullSymbol() throws IOException {
            JsonObject response = client.getListOpenOrders(null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(86)
        @DisplayName("Test Get Open Positions with Null Symbol")
        void testGetOpenPositionsNullSymbol() throws IOException {
            JsonObject response = client.getOpenPositions(null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(87)
        @DisplayName("Test Cancel All Orders with Null Symbol")
        void testCancelAllOrdersNullSymbol() throws IOException {
            JsonObject response = client.cancelAllOrders(null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }

        @Test
        @Order(88)
        @DisplayName("Test Close All Positions with Null Symbol")
        void testCloseAllPositionsNullSymbol() throws IOException {
            JsonObject response = client.closeAllPositions(null);
            assertThat(response).isNotNull();
            assertThat(response.get("success").getAsBoolean()).isTrue();
        }
    }
}