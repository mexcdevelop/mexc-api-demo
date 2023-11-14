package com.mexc.example.spot.api.v3.marketdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mexc.example.common.MarketDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class Ping {

    public static Object ping() {
        return MarketDataClient.get("/api/v3/ping", new HashMap<>(), new TypeReference<Object>() {
        });
    }

    public static void main(String[] args) {
        log.info("=>> ping:{}", ping());
    }
}
