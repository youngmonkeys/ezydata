package com.tvd12.ezydata.example.mongo.result;

import com.tvd12.ezydata.database.annotation.EzyQueryResult;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EzyQueryResult
public class SumBookPriceResult {
    private BigDecimal sum;
}