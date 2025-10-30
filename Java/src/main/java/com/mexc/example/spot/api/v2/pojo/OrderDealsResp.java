package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sniper
 * @date 2019/10/22
 */
@Data
public class OrderDealsResp {
    private String id;

    @JsonIgnore
    private String memberId;

    private String symbol;

    @JsonProperty("trade_type")
    private String tradeType;

    @JsonProperty("order_id")
    private String orderId;
    /**
     * Opponent order ID
     */
    @JsonProperty("opponent_order_id")
    private String opponentOrderId;

    private String quantity;

    private String price;

    private String amount;

    private String fee;

    @JsonProperty("fee_currency")
    private String feeCurrency;

    @JsonProperty("create_time")
    private Long createTime;
}
