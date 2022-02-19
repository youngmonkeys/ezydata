package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyQueryConditionBuilder implements EzyBuilder<String> {

    private final StringBuilder builder = new StringBuilder();

    public EzyQueryConditionBuilder append(String condition) {
        this.builder.append(condition);
        return this;
    }

    public EzyQueryConditionBuilder and(String condition) {
        if (builder.length() > 0) {
            builder.append(" AND ");
        }
        builder.append(condition);
        return this;
    }

    public EzyQueryConditionBuilder or(String condition) {
        if (builder.length() > 0) {
            builder.append(" OR ");
        }
        builder.append(condition);
        return this;
    }

    @Override
    public String build() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
