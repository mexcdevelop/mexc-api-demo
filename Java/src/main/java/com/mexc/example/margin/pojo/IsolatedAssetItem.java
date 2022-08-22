package com.mexc.example.margin.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IsolatedAssetItem {

    private String asset;
    private boolean borrowEnabled;
    private String borrowed;
    private String free;
    private String interest;
    private String locked;
    private String netAsset;
    private String netAssetOfBtc;
    private boolean repayEnabled;
    private String totalAsset;
}
