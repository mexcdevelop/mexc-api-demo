package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConvertRecordItem {

    private String totalConvert;
    private String totalFee;
    private long convertTime;
    private List<ConvertDetail> convertDetails;
}
