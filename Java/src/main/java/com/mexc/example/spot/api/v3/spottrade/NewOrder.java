package com.mexc.example.spot.api.v3.spottrade;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.OrderPlaceResp;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NewOrder {
    public static OrderPlaceResp placeOrder(Map<String, String> params) {
        return UserDataClient.post("/api/v3/order", params, new TypeReference<OrderPlaceResp>() {
        });
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        //symbol=AEUSDT&side=SELL&type=LIMIT&timeInForce=GTC&quantity=1&price=20
        params.put("symbol", URLEncoder.encode("🤮🐰💗❤️🌞💤📖⛰️USDT", "UTF-8").replaceAll("\\+", "%20"));
        params.put("side", "SELL");
        params.put("type", "LIMIT");
        params.put("quantity", "1");
        params.put("price", "100000");
        params.put("recvWindow", "60000");

        //place order
        OrderPlaceResp placeResp = placeOrder(params);
        log.info("==>>placeResp:{}", JsonUtil.toJson(placeResp));
    }
}
