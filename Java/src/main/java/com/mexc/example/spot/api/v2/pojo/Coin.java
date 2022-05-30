package com.mexc.example.spot.api.v2.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Coin {

    private String chain;
    private Integer precision;
    private BigDecimal fee;
    private boolean is_withdraw_enabled;
    private boolean is_deposit_enabled;
    private Integer deposit_min_confirm;
    private Integer withdraw_limit_max;
    private BigDecimal withdraw_limit_min;

}
