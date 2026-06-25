package com.mexc.example.spot.api.v3.spottrade;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.TestConfig;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.OrderPlaceResp;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ApiKeyInfo {
    public static Object updateApiKeyInfo(Map<String, String> params) {
        return UserDataClient.post("/api/v3/apiKeyInfo", params, new TypeReference<Object>() {
        });
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("apiKey", "mx0XXX");
        params.put("ipWhiteList", "127.0.0.1");
        params.put("note", "123");
        params.put("recvWindow", "60000");

        //place order
        Object resp = updateApiKeyInfo(params);
        log.info("==>>resp:{}", JsonUtil.toJson(resp));
    }
}
