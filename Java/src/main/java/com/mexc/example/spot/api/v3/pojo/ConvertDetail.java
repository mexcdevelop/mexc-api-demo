package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConvertDetail {

    private String id;
    private String convert;
    private String fee;
    private String amount;
    private String asset;
    private long time;
}
