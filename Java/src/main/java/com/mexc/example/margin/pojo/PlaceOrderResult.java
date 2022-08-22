package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaceOrderResult {

    private String symbol;
    private String orderId;
    private String clientOrderId;
    private Boolean isIsolated;
    private long transactTime;
}
