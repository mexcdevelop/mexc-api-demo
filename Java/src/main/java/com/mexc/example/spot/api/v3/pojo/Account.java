package com.mexc.example.spot.api.v3.pojo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Account {

    private Integer makerCommission;
    private Integer takerCommission;
    private Integer buyerCommission;
    private Integer sellerCommission;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    private Long updateTime;
    private String accountType;
    private List<AccountBalance> balances;
    private List<String> permissions;

}
