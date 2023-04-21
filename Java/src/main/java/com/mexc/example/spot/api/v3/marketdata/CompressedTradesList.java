package com.mexc.example.spot.api.v3.marketdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.MarketDataClient;
import com.mexc.example.spot.api.v3.pojo.AggTrades;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class CompressedTradesList {
    public static List<AggTrades> aggTrades(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/aggTrades", params, new TypeReference<List<AggTrades>>() {
        });
    }

    public static void main(String[] args) {
        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());
        //compressed/aggregate trades list
        log.info("=>>aggTrades:{}", JsonUtil.toJson(aggTrades(symbolParams)));
    }
}
