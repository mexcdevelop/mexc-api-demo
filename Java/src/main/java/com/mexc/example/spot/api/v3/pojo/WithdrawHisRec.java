package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WithdrawHisRec {
    private String id;
    private String txId;
    private String coin;
    private String network;
    private String address;
    private String amount;
    private Integer transferType;
    private Integer status;
    private String transactionFee;
    private String confirmNo;
    private long applyTime;
    private String remark;
}
