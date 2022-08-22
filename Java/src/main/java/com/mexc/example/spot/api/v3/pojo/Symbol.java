package com.mexc.example.spot.api.v3.pojo;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Symbol {

    private String symbol;
    private String status;
    private String baseAsset;
    private Integer baseAssetPrecision;
    private String quoteAsset;
    private Integer quotePrecision;
    private Integer quoteAssetPrecision;
    private Integer baseCommissionPrecision;
    private Integer quoteCommissionPrecision;
    private Object[] orderTypes;
    private boolean icebergAllowed;
    private boolean ocoAllowed;
    private boolean quoteOrderQtyMarketAllowed;
    private Boolean isSpotTradingAllowed;
    private Boolean isMarginTradingAllowed;
    private List<String> permissions;
    private List<Object> filters = new ArrayList<>();

}
