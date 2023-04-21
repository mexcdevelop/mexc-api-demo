package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GenerateDepositAddress {

    public static Map<String, String> createDepositAddress(Map<String, String> params) {
        return UserDataClient.post("/api/v3/capital/deposit/address", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {
        //create deposit address
        Map<String, String> createDepositAddressResp = createDepositAddress(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("coin", "USDT")
                .put("network", "TRC20")
                .build()));

        log.info("===>>createDepositAddressResp:{}", JsonUtil.toJson(createDepositAddressResp));
    }
}
