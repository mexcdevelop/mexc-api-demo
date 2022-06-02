package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PlaceOrderRequest {
    private String symbol;
    private String price;
    private String quantity;
    @JsonProperty("trade_type")
    private String tradeType;
    @JsonProperty("order_type")
    private String orderType;
    @JsonProperty("client_order_id")
    private String clientOrderId;
}
