package com.mexc.example.spot.api.v3.spottrade;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.Symbols;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UserAPIDefaultSymbol {
    public static Symbols selfSymbols(Map<String, String> params) {
        return UserDataClient.get("/api/v3/selfSymbols", params, new TypeReference<Symbols>() {
        });
    }

    public static void main(String[] args) {
        Symbols selfSymbols = selfSymbols(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()));
        //user self symbols
        log.info("==>>selfSymbols:{}", JsonUtil.toJson(selfSymbols));
    }
}
