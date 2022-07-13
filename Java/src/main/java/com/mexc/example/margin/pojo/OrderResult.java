package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderResult {

    private String symbol;
    private String orderId;
    private String orderListId;
    private String clientOrderId;
    private String price;
    private String origQty;
    private String executedQty;
    private String cummulativeQuoteQty;
    private String status;
    private String type;
    private String side;
    private Boolean isIsolated;
    private Boolean isWorking;
    private long time;
    private long updateTime;
}
