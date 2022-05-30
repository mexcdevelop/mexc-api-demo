package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SymbolInfo {
    private String symbol;

    @JsonProperty("price_scale")
    private int priceScale;

    @JsonProperty("quantity_scale")
    private int quantityScale;

    @JsonProperty("min_amount")
    private String minAmount;

    @JsonProperty("max_amount")
    private String maxAmount;

    @JsonProperty("buy_fee_rate")
    private String buyFeeRate;

    @JsonProperty("sell_fee_rate")
    private String sellFeeRate;
}
