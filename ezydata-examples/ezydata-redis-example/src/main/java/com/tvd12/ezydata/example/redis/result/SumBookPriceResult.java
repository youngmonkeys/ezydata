package com.tvd12.ezydata.example.redis.result;

import java.math.BigDecimal;

import com.tvd12.ezydata.database.annotation.EzyQueryResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyQueryResult
public class SumBookPriceResult {
    private BigDecimal sum;
}