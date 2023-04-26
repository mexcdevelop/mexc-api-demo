package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.CoinItem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class Getall {
    public static List<CoinItem> getAllCoins(Map<String, String> params) {
        return UserDataClient.get("/api/v3/capital/config/getall", params, new TypeReference<List<CoinItem>>() {
        });
    }

    public static void main(String[] args) {
        //get allCoins

        List<CoinItem> allCoins = getAllCoins(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>allCoins:{}", JsonUtil.toJson(allCoins));
    }
}
