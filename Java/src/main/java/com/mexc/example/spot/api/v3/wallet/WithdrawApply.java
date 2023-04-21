package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.Withdraw;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WithdrawApply {
    public static Withdraw withdraw(Map<String, String> params) {
        return UserDataClient.post("/api/v3/capital/withdraw/apply", params, new TypeReference<Withdraw>() {
        });
    }

    public static void main(String[] args) {
        //withdraw apply
        HashMap<String, String> withdrawParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("coin", "USDT-TRX")
                .put("address", "TPb5qT9ZikopzCUD4zyieSEfwbjdjU8PVb")
                .put("amount", "3")
                .put("network", "TRC20")
                .put("recvWindow", "60000")
                .build());

        Object withdraw = withdraw(withdrawParams);
        log.info("===>>withdraw resp:{}", JsonUtil.toJson(withdraw));
    }
}
