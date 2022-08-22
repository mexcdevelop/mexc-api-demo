package com.mexc.example.spot.api.v3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MexcApiV3AuthExample {


    public static Order getOrder(Map<String, String> params) {
        return UserDataClient.get("/api/v3/order", params, new TypeReference<Order>() {
        });
    }

    public static List<Order> allOrders(Map<String, String> params) {
        return UserDataClient.get("/api/v3/allOrders", params, new TypeReference<List<Order>>() {
        });
    }

    public static List<Order> openOrders(Map<String, String> params) {
        return UserDataClient.get("/api/v3/openOrders", params, new TypeReference<List<Order>>() {
        });
    }

    public static List<MyTrades> myTrades(Map<String, String> params) {
        return UserDataClient.get("/api/v3/myTrades", params, new TypeReference<List<MyTrades>>() {
        });
    }

    public static Account account(Map<String, String> params) {
        return UserDataClient.get("/api/v3/account", params, new TypeReference<Account>() {
        });
    }

    public static OrderPlaceResp placeOrder(Map<String, String> params) {
        return UserDataClient.post("/api/v3/order", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    public static OrderPlaceResp placeOrderTest(Map<String, String> params) {
        return UserDataClient.post("/api/v3/order/test", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    public static OrderCancelResp cancelOrder(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/order", params, new TypeReference<OrderCancelResp>() {
        });
    }

    public static List<OrderCancelResp> cancelOpenOrders(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/openOrders", params, new TypeReference<List<OrderCancelResp>>() {
        });
    }


    public static void main(String[] args) {

        //get order
        Order order = getOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "150751023827259392")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>order:{}", JsonUtil.toJson(order));

        //get all orders
        List<Order> allOrders = allOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "GAL3SUSDT")
                .build()));
        log.info("==>>allOrders:{}", JsonUtil.toJson(allOrders));

        //get open orders
        List<Order> openOrders = openOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MXUSDT")
                .build()));
        log.info("==>>openOrders:{}", JsonUtil.toJson(openOrders));

        //get my trades
        List<MyTrades> myTrades = myTrades(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "MXUSDT")
                .build()));
        log.info("==>>myTrades:{}", JsonUtil.toJson(myTrades));

        //get account
        Account account = account(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>account:{}", JsonUtil.toJson(account));


        Map<String, String> params = new HashMap<>();
        //symbol=AEUSDT&side=SELL&type=LIMIT&timeInForce=GTC&quantity=1&price=20
        params.put("symbol", "BTCUSDT");
        params.put("side", "SELL");
        params.put("type", "LIMIT");
        params.put("quantity", "1");
        params.put("price", "100000");
        params.put("recvWindow", "60000");

        //place order
        OrderPlaceResp placeResp = placeOrder(params);
        log.info("==>>placeResp:{}", JsonUtil.toJson(placeResp));

        //test place order
        OrderPlaceResp placeRespTest = placeOrderTest(params);
        log.info("==>>placeRespTest:{}", JsonUtil.toJson(placeRespTest));


        //cancel order
        OrderCancelResp cancelResp = cancelOrder(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("orderId", "150751023827259392")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>cancelResp:{}", JsonUtil.toJson(cancelResp));

        //cancel open orders
        List<OrderCancelResp> orderCancelResps = cancelOpenOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", "BTCUSDT")
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>orderCancelResps:{}", JsonUtil.toJson(orderCancelResps));
    }
}
