package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.WithdrawHisRec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class WithdrawHistory {
    public static List<WithdrawHisRec> getWithdrawHisRec(Map<String, String> params) {
        return UserDataClient.get("/api/v3/capital/withdraw/history", params, new TypeReference<List<WithdrawHisRec>>() {
        });
    }

    public static void main(String[] args) {
        //get withdraw history record
        List<WithdrawHisRec> withdrawHisRec = getWithdrawHisRec(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("coin", "USDT")
                .build()));
        log.info("==>>withdrawHisRec:{}", JsonUtil.toJson(withdrawHisRec));
    }
}
