package com.mexc.example.spot.api.v3.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageRecords<T> {

    private List<T> data;
    private int totalRecords;
    private int totalPageNum;
    private int page;

}
