package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.TransferId;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UserUniversalTransfer {
    public static TransferId transfer(Map<String, String> params) {
        return UserDataClient.post("/api/v3/capital/transfer", params, new TypeReference<TransferId>() {
        });
    }

    public static void main(String[] args) {
        //transfer
        TransferId transferResp = transfer(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("fromAccountType", "SPOT")
                .put("toAccountType", "FUTURES")
                .put("asset", "USDT")
                .put("amount", "3")
                .build()));
        log.info("==>>transferResp:{}", JsonUtil.toJson(transferResp));
    }
}
