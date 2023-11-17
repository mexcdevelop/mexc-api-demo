package com.mexc.example.spot.api.v3.rebate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.JsonUtil;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;

@Slf4j
public class AffiliateCommission {

    public static Object affiliateCommission(Map<String, String> params) {
        return UserDataClient.get("/api/v3/rebate/affiliate/commission", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {

        //Get Affiliate Commission Record (affiliate only)
        Object affiliateCommission = affiliateCommission(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("startTime", "1566921600000")
                .build()));

        log.info("==>>affiliateCommission:{}", JsonUtil.toJson(affiliateCommission));
    }
}
