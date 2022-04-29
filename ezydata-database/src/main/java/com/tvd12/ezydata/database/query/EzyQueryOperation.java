package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.collect.Lists;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public enum EzyQueryOperation {
    EQUAL("=", "eq", ""),
    IN("in", "in", "In"),
    GT(">", "gt", "Gt"),
    GTE(">=", "gte", "Gte"),
    LT("<", "lt", "Lt"),
    LTE("<=", "lte", "Lte");

    private static final List<EzyQueryOperation> NOT_INCLUDE_EQUAL_VALUES =
        Collections.unmodifiableList(
            Lists.newArrayList(IN, GT, GTE, LT, LTE)
        );
    @Getter
    private final String sign;
    @Getter
    private final String signName;
    @Getter
    private final String tag;

    private EzyQueryOperation(String sign, String signName, String tag) {
        this.sign = sign;
        this.signName = signName;
        this.tag = tag;
    }

    public static List<EzyQueryOperation> notIncludeEqualValues() {
        return NOT_INCLUDE_EQUAL_VALUES;
    }
}
