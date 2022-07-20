package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IsolatedPairResult {
    private String symbol;
    private String base;
    private String quote;
    private Boolean isMarginTrade;
}
