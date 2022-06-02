package com.mexc.example.spot.api.v2.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MarketDepthWrapper {

    private List<MarketDepth> asks = new ArrayList<>();

    private List<MarketDepth> bids = new ArrayList<>();
}
