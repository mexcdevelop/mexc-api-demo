package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RebateRecords {

    private String asset;
    private String type;
    private String rate;
    private String amount;
    private String uid;
    private String account;
    private long tradeTime;
    private long updateTime;
}
