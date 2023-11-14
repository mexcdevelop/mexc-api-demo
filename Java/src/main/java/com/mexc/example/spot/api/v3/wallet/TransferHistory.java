package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.TransferRec;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
public class TransferHistory {
    public static TransferRec getTransferRec(Map<String, String> params) {
        return UserDataClient.get("/api/v3/capital/transfer", params, new TypeReference<TransferRec>() {
        });
    }

    public static void main(String[] args) {
        //get transfer record
        TransferRec transferRec = getTransferRec(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("fromAccountType", "SPOT")
                .put("toAccountType", "SPOT")
                .build()));
        log.info("==>>transferRec:{}", JsonUtil.toJson(transferRec));
    }
}
