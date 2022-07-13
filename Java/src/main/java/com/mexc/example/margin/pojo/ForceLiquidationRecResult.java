package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForceLiquidationRecResult {
    private String avgPrice;
    private String executedQty;
    private String orderId;
    private String price;
    private String qty;
    private String side;
    private String symbol;
    private Boolean isIsolated;
    private long time;
    private long updatedTime;
}
