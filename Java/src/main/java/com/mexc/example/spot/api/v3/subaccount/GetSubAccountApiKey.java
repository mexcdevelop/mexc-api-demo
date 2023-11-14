package com.mexc.example.spot.api.v3.subaccount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.SubAccount;
import com.mexc.example.spot.api.v3.pojo.SubAccountApiKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class GetSubAccountApiKey {

    public static Map<String,List<SubAccountApiKey>> getSubAccountApiKey(Map<String, String> params) {
        return UserDataClient.get("/api/v3/sub-account/apiKey", params, new TypeReference<Map<String,List<SubAccountApiKey>>>() {
        });
    }

    public static void main(String[] args) {
        //get subAccount apiKey
        Map<String,List<SubAccountApiKey>> subAccountApiKeys = getSubAccountApiKey(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("subAccount", "subAccount0421")
                .build()));
        log.info("==>>subAccountApiKeys:{}", JsonUtil.toJson(subAccountApiKeys));
    }
}
