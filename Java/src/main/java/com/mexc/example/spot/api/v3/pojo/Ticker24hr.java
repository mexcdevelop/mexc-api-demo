package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ticker24hr {

    private String symbol;
    private String priceChange;
    private String priceChangePercent;
    private String prevClosePrice;
    private String lastPrice;
    private String lastQty;
    private String bidPrice;
    private String bidQty;
    private String askPrice;
    private String askQty;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String volume;
    private String quoteVolume;
    private Long openTime;
    private Long closeTime;
    private Integer count;
}
