package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CancelWithdraw {
    public static Map<String, String> cancelWithdraw(Map<String, String> params) {
        return UserDataClient.delete("/api/v3/capital/withdraw", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {
        //cancel withdraw
        Map<String, String> cancelWithdrawResp = cancelWithdraw(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("id", "ca7bd51895134fb5bd749f1cf875b8af")
                .put("recvWindow", "60000")
                .build()));
        log.info("===>>cancelWithdrawResp:{}", JsonUtil.toJson(cancelWithdrawResp));
    }
}
