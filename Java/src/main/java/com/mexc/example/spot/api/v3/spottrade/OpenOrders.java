package com.mexc.example.spot.api.v3.spottrade;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.TestConfig;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenOrders {
    public static List<Order> openOrders(Map<String, String> params) {
        return UserDataClient.get("/api/v3/openOrders", params, new TypeReference<List<Order>>() {
        });
    }

    public static void main(String[] args) throws Exception {
        //get open orders
        List<Order> openOrders = openOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", TestConfig.TEST_SYMBOL)
                .build()));
        log.info("==>>openOrders:{}", JsonUtil.toJson(openOrders));
    }
}
