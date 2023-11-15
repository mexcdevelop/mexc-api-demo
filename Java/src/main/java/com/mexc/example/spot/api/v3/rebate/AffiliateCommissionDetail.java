package com.mexc.example.spot.api.v3.rebate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class AffiliateCommissionDetail {

    public static Object affiliateCommissionDetail(Map<String, String> params) {
        return UserDataClient.get("/api/v3/rebate/affiliate/commission/detail", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {

        //Get Affiliate Commission Detail Record (affiliate only)
        Object affiliateCommissionDetail = affiliateCommissionDetail(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("startTime", "1566921600000")
                .build()));

        log.info("==>>affiliateCommissionDetail:{}", JsonUtil.toJson(affiliateCommissionDetail));
    }
}
