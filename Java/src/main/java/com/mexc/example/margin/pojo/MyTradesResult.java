package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyTradesResult {
    private String commission;
    private String commissionAsset;
    private String id;
    private Boolean isBuyer;
    private String orderId;
    private String price;
    private String qty;
    private String symbol;
    private Boolean isIsolated;
    private long time;
}
