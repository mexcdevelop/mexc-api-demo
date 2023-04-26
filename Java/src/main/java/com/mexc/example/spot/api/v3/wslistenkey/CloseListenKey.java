package com.mexc.example.spot.api.v3.wslistenkey;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CloseListenKey {

    public static Map<String, String> deleteUserDataStream(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/userDataStream", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {
        //get listenKey
        String listenKey = deleteUserDataStream(ImmutableMap.<String, String>builder()
                .put("listenKey", "listenKey1")
                .build()).get("listenKey");

        log.info("==>>listenKey:{}", listenKey);
    }
}
