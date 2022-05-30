package com.mexc.example.spot.api.v3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mexc.example.common.SignatureUtil;
import com.mexc.example.spot.api.v3.pojo.*;
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
public class MexcApiV3AuthExample {


    private static final String REQUEST_HOST = "https://api.mexc.com";

    private static final OkHttpClient OK_HTTP_CLIENT = createOkHttpClient();

    private static final String accessKey = "mx0LAUvP5u0UNufqpH";
    private static final String secretKey = "cccadcc82efa41ad83fc73079d8000b8";

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new SignatureInterceptor(secretKey, accessKey))
                .build();
    }

    private static <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Response response = OK_HTTP_CLIENT
                    .newCall(new Request.Builder().url(REQUEST_HOST + uri + "?" + toQueryString(params)).get().build())
                    .execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T post(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            Response response = OK_HTTP_CLIENT
                    .newCall(new Request.Builder()
                            .url(REQUEST_HOST.concat(uri))
                            .post(RequestBody.create(toQueryString(params), MediaType.get("text/plain"))).build()).execute();
            return handleResponse(response, ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static <T> T delete(String uri, Map<String, String> params, TypeReference<T> ref) {
        try {
            return handleResponse(OK_HTTP_CLIENT
                    .newCall(new Request.Builder()
                            .url(REQUEST_HOST.concat(uri))
                            .delete(RequestBody.create(toQueryString(params), MediaType.get("text/plain"))).build()).execute(), ref);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T handleResponse(Response response, TypeReference<T> ref) throws IOException {
        Gson gson = new Gson();
        assert response.body() != null;
        String content = response.body().string();
        if (200 != response.code()) {
            throw new RuntimeException(content);
        }
        return gson.fromJson(content, ref.getType());
    }


    private static String toQueryString(Map<String, String> params) {
        return params.entrySet().stream().map((entry) -> entry.getKey() + "=" + SignatureUtil.urlEncode(entry.getValue())).collect(Collectors.joining("&"));
    }

    public static Order getOrder(Map<String, String> params) {
        return get("/api/v3/order", params, new TypeReference<Order>() {
        });
    }

    public static List<Order> allOrders(Map<String, String> params) {
        return get("/api/v3/allOrders", params, new TypeReference<List<Order>>() {
        });
    }


    public static List<Order> openOrders(Map<String, String> params) {
        return get("/api/v3/openOrders", params, new TypeReference<List<Order>>() {
        });
    }

    public static List<MyTrades> myTrades(Map<String, String> params) {
        return get("/api/v3/myTrades", params, new TypeReference<List<MyTrades>>() {
        });
    }

    public static Account account(Map<String, String> params) {
        return get("/api/v3/account", params, new TypeReference<Account>() {
        });
    }

    public static OrderPlaceResp placeOrder(Map<String, String> params) {
        return post("/api/v3/order", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    public static OrderPlaceResp placeOrderTest(Map<String, String> params) {
        return post("/api/v3/order/test", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    public static OrderCancelResp cancelOrder(Map<String, String> params) {
        return delete("/api/v3/order", params, new TypeReference<OrderCancelResp>() {
        });
    }

    public static List<OrderCancelResp> cancelOpenOrders(Map<String, String> params) {
        return delete("/api/v3/openOrders", params, new TypeReference<List<OrderCancelResp>>() {
        });
    }


    public static void main(String[] args) {
        Gson gson = new Gson();
        //订单查询
        Order order = getOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "150751023827259392")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>order:{}", gson.toJson(order));

        //所有订单
        List<Order> allOrders = allOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "GAL3SUSDT")
                .build()));
        log.info("==>>allOrders:{}", gson.toJson(allOrders));

        //当前挂单
        List<Order> openOrders = openOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MXUSDT")
                .build()));
        log.info("==>>openOrders:{}", gson.toJson(openOrders));

        //成交记录
        List<MyTrades> myTrades = myTrades(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MXUSDT")
                .build()));
        log.info("==>>myTrades:{}", gson.toJson(myTrades));

        //账户信息
        Account account = account(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>account:{}", gson.toJson(account));


        Map<String, String> params = new HashMap<>();
        //symbol=AEUSDT&side=SELL&type=LIMIT&timeInForce=GTC&quantity=1&price=20
        params.put("symbol", "BTCUSDT");
        params.put("side", "SELL");
        params.put("type", "LIMIT");
        params.put("quantity", "1");
        params.put("price", "100000");
        params.put("recvWindow", "60000");

        //下单
        OrderPlaceResp placeResp = placeOrder(params);
        log.info("==>>placeResp:{}", gson.toJson(placeResp));

        //测试下单
        OrderPlaceResp placeRespTest = placeOrderTest(params);
        log.info("==>>placeRespTest:{}", gson.toJson(placeRespTest));


        //撤销订单
        OrderCancelResp cancelResp = cancelOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "150751023827259392")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>cancelResp:{}", gson.toJson(cancelResp));

        //撤销所有挂单
        List<OrderCancelResp> orderCancelResps = cancelOpenOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>orderCancelResps:{}", gson.toJson(orderCancelResps));
    }


}
