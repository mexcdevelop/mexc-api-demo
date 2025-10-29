package com.mexc.example.spot.api.v3.spottrade;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.OrderCancelResp;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
public class CancelallOpenOrders {
    public static List<OrderCancelResp> cancelOpenOrders(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/openOrders", params, new TypeReference<List<OrderCancelResp>>() {
        });
    }

    public static void main(String[] args) throws Exception {
        //cancel open orders
        List<OrderCancelResp> orderCancelResps = cancelOpenOrders(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("symbol", URLEncoder.encode("BTCUSDT", "UTF-8").replaceAll("\\+", "%20"))
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>orderCancelResps:{}", JsonUtil.toJson(orderCancelResps));
    }
}
