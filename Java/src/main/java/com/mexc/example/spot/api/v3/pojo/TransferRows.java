package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferRows {
    private String tranId;
    private String clientTranId;
    private String asset;
    private String amount;
    private String fromAccountType;
    private String toAccountType;
    private String toSymbol;
    private String status;
    private long timestamp;
}
