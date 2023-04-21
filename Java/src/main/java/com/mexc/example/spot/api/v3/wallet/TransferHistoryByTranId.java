package com.mexc.example.spot.api.v3.wallet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.TransferRows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class TransferHistoryByTranId {
    public static TransferRows getTransferRecByTranId(Map<String, String> params) {
        return UserDataClient.get("/api/v3/capital/transfer/tranId", params, new TypeReference<TransferRows>() {
        });
    }

    public static void main(String[] args) {
        //get transfer record by tranId
        TransferRows transferRow = getTransferRecByTranId(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("tranId", "cb28c88cd20c42819e4d5148d5fb5742")
                .build()));
        log.info("==>>transferRow:{}", JsonUtil.toJson(transferRow));
    }
}
