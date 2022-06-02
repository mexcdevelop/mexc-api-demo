package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeHistory {

    @JsonProperty("trade_time")
    private Long tradeTime;

    @JsonProperty("trade_price")
    private String tradePrice;

    @JsonProperty("trade_quantity")
    private String tradeQuantity;

    @JsonProperty("trade_type")
    private String tradeType;
}
