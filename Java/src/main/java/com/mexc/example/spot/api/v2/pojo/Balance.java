package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Balance {
    private String currency;
    @JsonProperty("account_type")
    private String accountSys;
    @JsonProperty("balance")
    private String total;
    private String available;
    private String frozen;
}
