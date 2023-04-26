package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RebateHistory {

    private String spot;
    private String futures;
    private String total;
    private String uid;
    private String account;
    private long inviteTime;
}
