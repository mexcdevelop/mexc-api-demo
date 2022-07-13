package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IsolatedMarginTierResult {

    private String symbol;
    private Integer tier;
    private String effectiveMultiple;
    private String initialRiskRatio;
    private String liquidationRiskRatio;
    private String baseAssetMaxBorrowable;
    private String quoteAssetMaxBorrowable;
}
