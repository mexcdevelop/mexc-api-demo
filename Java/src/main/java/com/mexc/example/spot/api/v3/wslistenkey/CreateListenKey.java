package com.mexc.example.spot.api.v3.wslistenkey;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CreateListenKey {
    public static Map<String, String> postUserDataStream(Map<String, String> params) {
        return UserDataClient.post("/api/v3/userDataStream", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {
        //get listenKey
        String listenKey = postUserDataStream(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()).get("listenKey");
        log.info("==>>listenKey:{}", listenKey);
    }
}
