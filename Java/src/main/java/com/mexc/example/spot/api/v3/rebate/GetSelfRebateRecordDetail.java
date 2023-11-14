package com.mexc.example.spot.api.v3.rebate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import com.mexc.example.spot.api.v3.pojo.PageRecords;
import com.mexc.example.spot.api.v3.pojo.RebateRecords;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetSelfRebateRecordDetail {

    public static PageRecords<RebateRecords> rebateDetailKickback(Map<String, String> params) {
        return UserDataClient.get("/api/v3/rebate/detail/kickback", params, new TypeReference<PageRecords<RebateRecords>>() {
        });
    }

    public static void main(String[] args) {

        //get self rebate records detail
        PageRecords<RebateRecords> records = rebateDetailKickback(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("page", "1")
                .build()));
        log.info("==>>records:{}", JsonUtil.toJson(records));
    }
}
