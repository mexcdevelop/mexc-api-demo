package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickerInfo {

    private String symbol;

    private String volume;

    private String high;

    private String low;

    private String buy;

    private String sell;

    private String open;

    private String last;

    @JsonProperty("percent_change")
    private String percentChange;

}
