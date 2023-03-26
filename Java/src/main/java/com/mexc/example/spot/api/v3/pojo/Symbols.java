package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Symbols {
    private int code;
    private String msg;
    private List<String> data;

}
