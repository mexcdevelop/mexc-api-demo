package com.mexc.example.spot.api.v3.marketdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.MarketDataClient;
import com.mexc.example.spot.api.v3.pojo.Trades;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class RecentTradesList {
    public static List<Trades> trades(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/trades", params, new TypeReference<List<Trades>>() {
        });
    }

    public static void main(String[] args) {
        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());
        //recent trades list
        log.info("=>>trades:{}", JsonUtil.toJson(trades(symbolParams)));
    }
}
