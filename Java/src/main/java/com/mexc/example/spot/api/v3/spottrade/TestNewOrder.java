package com.mexc.example.spot.api.v3.spottrade;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.TestConfig;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.OrderPlaceResp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestNewOrder {

    /**
     * Example 1: All parameters sent via request body (Form/Text format)
     */
    public static OrderPlaceResp testExample1(Map<String, String> params) {
        log.info("=== Example 1: All parameters via request body (Form/Text) ===");
        return UserDataClient.post("/api/v3/order/test", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    /**
     * Example 2: All parameters sent via query string
     */
    public static OrderPlaceResp testExample2(Map<String, String> params) {
        log.info("=== Example 2: All parameters via query string ===");
        return UserDataClient.postEmptyBody("/api/v3/order/test", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    /**
     * Example 3: Mixed usage of query string and request body
     */
    public static OrderPlaceResp testExample3(Map<String, String> queryParams, Map<String, String> bodyParams) {
        log.info("=== Example 3: Mixed query string and request body ===");
        return UserDataClient.postMixed("/api/v3/order/test", queryParams, bodyParams, new TypeReference<OrderPlaceResp>() {
        });
    }

    /**
     * JSON format test
     */
    public static OrderPlaceResp testJsonFormat(Map<String, String> params) {
        log.info("=== JSON Format Test ===");
        return UserDataClient.postJson("/api/v3/order/test", params, new TypeReference<OrderPlaceResp>() {
        });
    }


    public static void main(String[] args) throws Exception {
        log.info("\n================================================================================");
        log.info("Starting API Tests with Dynamic Timestamps");
        log.info("================================================================================");

        // Basic parameters
        Map<String, String> allParams = new HashMap<>();
        allParams.put("symbol", TestConfig.TEST_SYMBOL);
        allParams.put("side", "BUY");
        allParams.put("type", "LIMIT");
        allParams.put("quantity", "1");
        allParams.put("price", "11");
        allParams.put("recvWindow", "5000");

        try {
            // Example 1: All parameters sent via request body
            log.info("\n============================================================");
            log.info("Testing Example 1: All parameters via request body");
            log.info("============================================================");
            OrderPlaceResp result1 = testExample1(new HashMap<>(allParams));
            log.info("Example 1 Result: {}", JsonUtil.toJson(result1));
        } catch (Exception e) {
            log.error("Example 1 failed: {}", e.getMessage());
        }

        try {
            // Example 2: All parameters sent via query string
            log.info("\n============================================================");
            log.info("Testing Example 2: All parameters via query string");
            log.info("============================================================");
            OrderPlaceResp result2 = testExample2(new HashMap<>(allParams));
            log.info("Example 2 Result: {}", JsonUtil.toJson(result2));
        } catch (Exception e) {
            log.error("Example 2 failed: {}", e.getMessage());
        }

        try {
            // Example 3: Mixed usage of query string and request body
            log.info("\n============================================================");
            log.info("Testing Example 3: Mixed query string and request body");
            log.info("============================================================");

            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("symbol", TestConfig.TEST_SYMBOL);
            queryParams.put("side", "BUY");
            queryParams.put("type", "LIMIT");

            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("quantity", "1");
            bodyParams.put("price", "11");
            bodyParams.put("recvWindow", "5000");

            OrderPlaceResp result3 = testExample3(queryParams, bodyParams);
            log.info("Example 3 Result: {}", JsonUtil.toJson(result3));
        } catch (Exception e) {
            log.error("Example 3 failed: {}", e.getMessage());
        }

        try {
            // JSON format test
            log.info("\n============================================================");
            log.info("Testing JSON Format");
            log.info("============================================================");
            OrderPlaceResp result4 = testJsonFormat(new HashMap<>(allParams));
            log.info("JSON Format Result: {}", JsonUtil.toJson(result4));
        } catch (Exception e) {
            log.error("JSON Format failed: {}", e.getMessage());
        }

    }
}
