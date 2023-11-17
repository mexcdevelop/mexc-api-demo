package com.mexc.example.spot.api.v3.rebate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mexc.example.common.UserDataClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class AffiliateReferral {

    public static Object affiliateReferral(Map<String, String> params) {
        return UserDataClient.get("/api/v3/rebate/affiliate/referral", params, new TypeReference<Map<String, String>>() {
        });
    }

    public static void main(String[] args) {

        //Get Affiliate Referral Data（affiliate only）
        Object affiliateReferral = affiliateReferral(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("startTime", "1566921600000")
                .build()));

        log.info("==>>commission:{}", affiliateReferral);
    }
}
