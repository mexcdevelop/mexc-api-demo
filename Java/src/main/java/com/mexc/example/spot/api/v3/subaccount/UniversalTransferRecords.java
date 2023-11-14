package com.mexc.example.spot.api.v3.subaccount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.TransferId;
import com.mexc.example.spot.api.v3.pojo.TransferRec;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UniversalTransferRecords {

    public static TransferRec universalTransfer(Map<String, String> params) {
        return UserDataClient.get("/api/v3/capital/sub-account/universalTransfer", params, new TypeReference<TransferRec>() {
        });
    }

    public static void main(String[] args) {

        //subAccount universalTransfer
        TransferRec transferRec = universalTransfer(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("toAccount", "toAccount1")
                .put("fromAccountType", "SPOT")
                .put("toAccountType", "FUTURES")
                .build()));

        log.info("==>>transferRec:{}", JsonUtil.toJson(transferRec));
    }
}
