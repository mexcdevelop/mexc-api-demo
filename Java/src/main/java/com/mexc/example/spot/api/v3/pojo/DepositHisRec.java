package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepositHisRec {
    private String amount;
    private String coin;
    private String network;
    private Integer status;
    private String address;
    private String addressTag;
    private String txId;
    private long insertTime;
    private String unlockConfirm;
    private String confirmTimes;
}
