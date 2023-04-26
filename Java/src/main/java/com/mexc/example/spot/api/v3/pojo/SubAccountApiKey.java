package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubAccountApiKey {

    private String subAccount;
    private String note;
    private String apiKey;
    private String secretKey;
    private String permissions;
    private String ip;
    private long creatTime;
}
