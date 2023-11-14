package com.mexc.example.spot.api.v3.spottrade;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.MxDeductResp;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class QueryMXDeductStatus {
    public static MxDeductResp getMxDeductStatus(Map<String, String> params) {
        return UserDataClient.get("/api/v3/mxDeduct/enable", params, new TypeReference<MxDeductResp>() {
        });
    }

    public static void main(String[] args) {
        //get mx deduct status
        MxDeductResp mxDeductStatus = getMxDeductStatus(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("recvWindow", "60000")
                .build()));
        log.info("==>>mxDeductStatus:{}", JsonUtil.toJson(mxDeductStatus));
    }
}
