package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EtfInfo {
    private String symbol;
    private String netValue;
    private String feeRate;
    private long timestamp;
    private long leverage;
    private long realLeverage;
    private long mergedTimes;
    private long lastMergedTime;
    private double basket;
}
