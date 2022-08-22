package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoanListItem {

    private String symbol;
    private String tranId;
    private String asset;
    private String principal;
    private String remainAmount;
    private String remainInterest;
    private String repayAmount;
    private String repayInterest;
    private String status;
    private Long timestamp;
}
