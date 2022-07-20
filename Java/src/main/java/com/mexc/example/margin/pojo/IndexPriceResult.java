package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndexPriceResult {

    private String price;
    private String symbol;
    private Long calcTime;
}
