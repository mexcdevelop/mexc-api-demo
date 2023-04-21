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
public class Ticker24hr {
    public static com.mexc.example.spot.api.v3.pojo.Ticker24hr ticker24hr(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/ticker/24hr", params, new TypeReference<com.mexc.example.spot.api.v3.pojo.Ticker24hr>() {
        });
    }

    public static void main(String[] args) {
        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());
        //24hr ticker price change statistics
        log.info("=>>ticker24hr:{}", JsonUtil.toJson(ticker24hr(symbolParams)));
    }
}
