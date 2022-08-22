package com.mexc.example.spot.api.v2.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {
    private T data;
    private int code;
    private String msg;
}
