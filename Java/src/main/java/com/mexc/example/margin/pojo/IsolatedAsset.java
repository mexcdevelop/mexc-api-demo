package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IsolatedAsset {

    private IsolatedAssetItem baseAsset;
    private IsolatedAssetItem quoteAsset;

    private String symbol;
    private Boolean isolatedCreated;
    private boolean enabled;
    private String marginLevel;
    private String marginRatio;
    private String indexPrice;
    private String liquidatePrice;
    private String liquidateRate;
    private boolean tradeEnabled;

}
