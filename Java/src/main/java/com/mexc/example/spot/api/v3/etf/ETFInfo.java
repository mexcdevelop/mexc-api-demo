package com.mexc.example.spot.api.v3.etf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.MarketDataClient;
import com.mexc.example.spot.api.v3.pojo.EtfInfo;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ETFInfo {

    public static EtfInfo etfInfo(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/etf/info", params, new TypeReference<EtfInfo>() {
        });
    }

    public static void main(String[] args) throws Exception {

        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", URLEncoder.encode("BTCUSDT3L", "UTF-8").replaceAll("\\+", "%20"))
                .build());

        log.info("=>>etfInfo:{}", JsonUtil.toJson(etfInfo(symbolParams)));
    }
}
