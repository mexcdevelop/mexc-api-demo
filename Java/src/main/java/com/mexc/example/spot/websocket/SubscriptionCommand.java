package com.mexc.example.spot.websocket;


import com.mexc.example.common.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class SubscriptionCommand {

    private String method = "SUBSCRIPTION";

    private List<String> params;

    public SubscriptionCommand(String channel) {
        this.params = Collections.singletonList(channel);
    }

    public String toJsonString() {
        return JsonUtil.toJson(this);
    }
}