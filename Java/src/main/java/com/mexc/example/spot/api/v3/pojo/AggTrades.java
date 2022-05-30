package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AggTrades {
    /**
     * {
     * "a": null,
     * "f": null,
     * "l": null,
     * "p": "30183.97",
     * "q": "0.397548",
     * "T": 1653051040000,
     * "m": false,
     * "M": true
     * },
     */

    private Long a;         // 归集成交ID
    private Long f;     // 被归集的末个成交ID
    private Long l;     // 被归集的末个成交ID
    private String p;   // 成交价
    private String q;     // 成交量
    private Long T;       // 成交时间
    private Boolean m; // 是否为主动卖出单
    private Boolean M; // 是否为最优撮合单(可忽略，目前总为最优撮合)
}
