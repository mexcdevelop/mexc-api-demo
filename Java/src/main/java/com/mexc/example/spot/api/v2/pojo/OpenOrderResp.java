package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author sniper
 * @date 2019/10/22
 * 当前委托信息
 */
@Data
public class OpenOrderResp {

    private String id;

    private String symbol;

    private String price;

    private String quantity;

    @JsonProperty("remain_quantity")
    private String remainQuantity;

    @JsonProperty("remain_amount")
    private String remainAmount;

    @JsonProperty("create_time")
    private Long createTime;

    private String state;

    private String type;
    @JsonIgnore
    private String memberId;
}
