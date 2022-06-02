package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
    private String symbol;
    private String orderId;
    private Long orderListId;
    private String clientOrderId;
    private String price;
    private String origQty;
    private String executedQty;
    private String cummulativeQuoteQty;
    private String status;
    private String timeInForce;
    private String type;
    private String side;
    private String stopPrice;
    private String icebergQty;
    private Long time;
    private Long updateTime;
    private Boolean isWorking;
    private String origQuoteOrderQty;
}
