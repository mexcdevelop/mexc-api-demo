package com.mexc.example.spot.api.v3;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.MarketDataClient;
import com.mexc.example.spot.api.v3.pojo.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MexcApiV3NoneAuthExample {

    public static Object ping() {
        return MarketDataClient.get("/api/v3/ping", new HashMap<>(), new TypeReference<Object>() {
        });
    }

    public static Map<String, Long> time() {
        return MarketDataClient.get("/api/v3/time", new HashMap<>(), new TypeReference<Map<String, Long>>() {
        });
    }

    public static ExchangeInfo exchangeInfo(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/exchangeInfo", params, new TypeReference<ExchangeInfo>() {
        });
    }

    public static Depth depth(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/depth", params, new TypeReference<Depth>() {
        });
    }

    public static List<Trades> trades(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/trades", params, new TypeReference<List<Trades>>() {
        });
    }

    public static List<Trades> historicalTrades(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/historicalTrades", params, new TypeReference<List<Trades>>() {
        });
    }

    public static List<AggTrades> aggTrades(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/aggTrades", params, new TypeReference<List<AggTrades>>() {
        });
    }

    public static List<Object[]> klines(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/klines", params, new TypeReference<List<Object[]>>() {
        });
    }

    public static AvgPrice avgPrice(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/avgPrice", params, new TypeReference<AvgPrice>() {
        });
    }

    public static Ticker24hr ticker24hr(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/ticker/24hr", params, new TypeReference<Ticker24hr>() {
        });
    }

    public static TickerPrice tickerPrice(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/ticker/price", params, new TypeReference<TickerPrice>() {
        });
    }

    public static BookTicker bookTicker(Map<String, String> params) {
        return MarketDataClient.get("/api/v3/ticker/bookTicker", params, new TypeReference<BookTicker>() {
        });
    }

    public static void main(String[] args) {

        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());

        log.info("=>>bookTicker:{}", JsonUtil.toJson(bookTicker(symbolParams)));

        log.info("=>>ticker24hr:{}", JsonUtil.toJson(ticker24hr(symbolParams)));

        log.info("=>>exchangeInfo:{}", JsonUtil.toJson(exchangeInfo(symbolParams)));

        log.info("=>>depth:{}", JsonUtil.toJson(depth(symbolParams)));

        log.info("=>>trades:{}", JsonUtil.toJson(trades(symbolParams)));

        log.info("=>>historicalTrades:{}", JsonUtil.toJson(historicalTrades(symbolParams)));

        log.info("=>>aggTrades:{}", JsonUtil.toJson(aggTrades(symbolParams)));

        log.info("=>>avgPrice:{}", JsonUtil.toJson(avgPrice(symbolParams)));

        log.info("=>>tickerPrice:{}", JsonUtil.toJson(tickerPrice(symbolParams)));

        symbolParams.put("interval", "1m");
        log.info("=>>klines:{}", JsonUtil.toJson(klines(symbolParams)));

    }
}
