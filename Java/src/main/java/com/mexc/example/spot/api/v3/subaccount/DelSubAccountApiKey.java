package com.mexc.example.spot.api.v3.subaccount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.SubAccountApiKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DelSubAccountApiKey {

    public static SubAccountApiKey deleteSubAccountApiKey(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/sub-account/apiKey", params, new TypeReference<SubAccountApiKey>() {
        });
    }

    public static void main(String[] args) {
        //delete subAccount apiKey
        SubAccountApiKey delSubAccountApiKey = deleteSubAccountApiKey(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("subAccount", "subAccount0421")
                .put("apiKey", "apiKey")
                .build()));
        log.info("==>>delSubAccountApiKey:{}", JsonUtil.toJson(delSubAccountApiKey));
    }
}
