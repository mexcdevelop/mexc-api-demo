package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepayListItem {

    private String symbol;
    private String amount;
    private String asset;
    private String interest;
    private String principal;
    private Long timestamp;
    private String tranId;

}
