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

    private Long a;         // Aggregate trade ID
    private Long f;     // First trade ID
    private Long l;     // Last trade ID
    private String p;   // Price
    private String q;     // Quantity
    private Long T;       // Timestamp
    private Boolean m; // Was the buyer the maker?
    private Boolean M; // Was the trade the best price match? (can be ignored, currently always true)
}
