package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CoinItem {
    private String coin;
    private String name;
    private List<Network> networkList;
}



