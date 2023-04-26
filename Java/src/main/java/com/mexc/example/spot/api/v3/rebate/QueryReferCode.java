package com.mexc.example.spot.api.v3.rebate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class QueryReferCode {

    public static Map<String, String> referCode(Map<String, String> params) {
        return UserDataClient.get("/api/v3/rebate/referCode", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {

        //query referCode
        Map<String, String> records = referCode(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()));

        log.info("==>>referCode:{}", records.get("referCode"));
    }
}
