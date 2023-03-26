package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WithdrawAddressResp {
    private List<WithdrawAddress> data;
    private int totalRecords;
    private int page;
    private int totalPageNum;

}
