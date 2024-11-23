package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.collect.Lists;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public enum EzyQueryOperation {
    EQUAL("=", "eq", ""),
    IN("in", "in", "In"),
    GT(">", "gt", "Gt"),
    GTE(">=", "gte", "Gte"),
    LT("<", "lt", "Lt"),
    LTE("<=", "lte", "Lte");

    private final String sign;
    private final String signName;
    private final String tag;

    private static final List<EzyQueryOperation> NOT_INCLUDE_EQUAL_VALUES =
        Collections.unmodifiableList(
            Lists.newArrayList(IN, GT, GTE, LT, LTE)
        );

    EzyQueryOperation(String sign, String signName, String tag) {
        this.sign = sign;
        this.signName = signName;
        this.tag = tag;
    }

    public static List<EzyQueryOperation> notIncludeEqualValues() {
        return NOT_INCLUDE_EQUAL_VALUES;
    }
}
