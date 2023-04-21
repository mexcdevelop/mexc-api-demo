package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DepositAddress {
    public static List<DepositAddress> getDepositAddress(Map<String, String> params) {
        return UserDataClient.get("/api/v3/capital/deposit/address", params, new TypeReference<List<DepositAddress>>() {
        });
    }

    public static void main(String[] args) {
        //get deposit address
        List<DepositAddress> depositAddress = getDepositAddress(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("coin", "USDT")
                .build()));
        log.info("==>>withdrawHisRec:{}", JsonUtil.toJson(depositAddress));
    }
}
