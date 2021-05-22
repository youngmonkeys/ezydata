package com.tvd12.ezydata.example.mongo.result;

import java.math.BigDecimal;

import com.tvd12.ezyfox.database.annotation.EzyQueryResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyQueryResult
public class SumBookPriceResult {
    private BigDecimal sum;
}