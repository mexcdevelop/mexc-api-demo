

package com.mexc.example.spot.api.v2.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class DepositRecordDto {
    private String txid;
    private String coinName;
    private String walletType;
    private BigDecimal amount;
    private BigDecimal fee;
    private Long confirmations;
    private Long requireConfirmations;
    private String address;
    private String state;
    private String explorerUrl;
    private Date createTime;
    private Date updateTime;
}