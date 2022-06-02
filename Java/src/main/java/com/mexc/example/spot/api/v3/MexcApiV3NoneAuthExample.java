package com.mexc.example.spot.api.v3;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mexc.example.spot.api.v3.pojo.*;
import com.mexc.example.common.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class MexcApiV3NoneAuthExample {
    private static final String REQUEST_HOST = "https://api.mexc.com";
    private static final OkHttpClient OK_HTTP_CLIENT = createOkHttpClient();

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                //.addInterceptor(httpLoggingInterceptor)
                .build();
    }


    private static <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Request.Builder builder = new Request.Builder().url(REQUEST_HOST + uri + "?" + toQueryString(params)).get();
            Response response = OK_HTTP_CLIENT.newCall(builder.build()).execute();
            Gson gson = new Gson();
            assert response.body() != null;
            String content = response.body().string();
            return gson.fromJson(content, ref.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Object ping() {
        return get("/api/v3/ping", new HashMap<>(), new TypeReference<Object>() {
        });
    }

    public static Map<String, Long> time() {
        return get("/api/v3/time", new HashMap<>(), new TypeReference<Map<String, Long>>() {
        });

    }

    public static ExchangeInfo exchangeInfo(Map<String, String> params) {
        return get("/api/v3/exchangeInfo", params, new TypeReference<ExchangeInfo>() {
        });
    }

    public static Depth depth(Map<String, String> params) {
        return get("/api/v3/depth", params, new TypeReference<Depth>() {
        });
    }

    public static List<Trades> trades(Map<String, String> params) {
        return get("/api/v3/trades", params, new TypeReference<List<Trades>>() {
        });
    }

    public static List<Trades> historicalTrades(Map<String, String> params) {
        return get("/api/v3/historicalTrades", params, new TypeReference<List<Trades>>() {
        });
    }

    public static List<AggTrades> aggTrades(Map<String, String> params) {
        return get("/api/v3/aggTrades", params, new TypeReference<List<AggTrades>>() {
        });
    }

    public static List<Object[]> klines(Map<String, String> params) {
        return get("/api/v3/klines", params, new TypeReference<List<Object[]>>() {
        });
    }

    public static AvgPrice avgPrice(Map<String, String> params) {
        return get("/api/v3/avgPrice", params, new TypeReference<AvgPrice>() {
        });
    }

    public static Ticker24hr ticker24hr(Map<String, String> params) {
        return get("/api/v3/ticker/24hr", params, new TypeReference<Ticker24hr>() {
        });
    }

    public static TickerPrice tickerPrice(Map<String, String> params) {
        return get("/api/v3/ticker/price", params, new TypeReference<TickerPrice>() {
        });
    }

    public static BookTicker bookTicker(Map<String, String> params) {
        return get("/api/v3/ticker/bookTicker", params, new TypeReference<BookTicker>() {
        });
    }

    private static String toQueryString(Map<String, String> params) {
        return params.entrySet().stream().map((entry) -> entry.getKey() + "=" + SignatureUtil.urlEncode(entry.getValue())).collect(Collectors.joining("&"));
    }


    public static void main(String[] args) throws IOException {

        Gson gson = new Gson();
        HashMap<String, String> symbolParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .build());

        log.info("=>>bookTicker:{}", gson.toJson(bookTicker(symbolParams)));

        log.info("=>>ticker24hr:{}", gson.toJson(ticker24hr(symbolParams)));

        log.info("=>>exchangeInfo:{}", gson.toJson(exchangeInfo(symbolParams)));

        log.info("=>>depth:{}", gson.toJson(depth(symbolParams)));

        log.info("=>>trades:{}", gson.toJson(trades(symbolParams)));

        log.info("=>>historicalTrades:{}", gson.toJson(historicalTrades(symbolParams)));

        log.info("=>>aggTrades:{}", gson.toJson(aggTrades(symbolParams)));

        log.info("=>>avgPrice:{}", gson.toJson(avgPrice(symbolParams)));

        log.info("=>>tickerPrice:{}", gson.toJson(tickerPrice(symbolParams)));

        symbolParams.put("interval", "1m");
        log.info("=>>klines:{}", gson.toJson(klines(symbolParams)));

    }


}
