package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubAccount {

    private String subAccount;
    private String note;
    private boolean isFreeze;
    private long createTime;
}
