package com.mexc.example.spot.api.v2.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Pagination<E> {
    @JsonIgnore
    private int showCount;
    @JsonProperty("total_page")
    private int totalPage;
    @JsonProperty("total_size")
    private int totalResult;
    @JsonIgnore
    private int currentPage;
    @JsonIgnore
    private int currentResult;
    @JsonProperty("result_list")
    private List<E> resultList = new ArrayList();

    public List<E> getResultList() {
        return this.resultList;
    }

    public void setResultList(List<E> resultList) {
        this.resultList = resultList;
    }
}