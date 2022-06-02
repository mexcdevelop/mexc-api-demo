package com.mexc.example.spot.api.v2.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class WithdrawRecord {
    private String id;
    private String coinName;
    private String walletType;
    private String address;
    private BigDecimal amount;
    private BigDecimal fee;
    private String txid;
    private String remark;
    private String explorerUrl;
    private String state;
    private Date updateTime;
    private Date createTime;
}