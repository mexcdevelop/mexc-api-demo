package com.mexc.example.spot.api.v3.marketdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.MarketDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class TickerPrice {
    public static com.mexc.example.spot.api.v3.pojo.TickerPrice tickerPrice(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/ticker/price", params, new TypeReference<com.mexc.example.spot.api.v3.pojo.TickerPrice>() {
        });
    }

    public static void main(String[] args) {
        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());
        //symbol price ticker
        log.info("=>>tickerPrice:{}", JsonUtil.toJson(tickerPrice(symbolParams)));
    }
}
