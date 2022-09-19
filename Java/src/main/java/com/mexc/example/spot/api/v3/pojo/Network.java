package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Network {
        private String coin;
        private String depositDesc;
        private Boolean depositEnable;
        private Integer minConfirm;
        private String name;
        private String network;
        private Boolean withdrawEnable;
        private String withdrawFee;
        private String withdrawIntegerMultiple;
        private String withdrawMax;
        private String withdrawMin;
        private Boolean sameAddress;
        private String contract;
}

