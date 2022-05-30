package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ExchangeInfo {

    private String timezone;
    private Long serverTime;
    private List<Object> rateLimits = new ArrayList<>();
    private List<Object> exchangeFilters = new ArrayList<>();
    private List<Symbol> symbols;
}
