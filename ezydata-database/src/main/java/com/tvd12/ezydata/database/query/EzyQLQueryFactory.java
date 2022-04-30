package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.builder.EzyBuilder;

import java.util.function.Function;

public class EzyQLQueryFactory {

    protected final Function<Object, Object> parameterConverter;

    protected EzyQLQueryFactory(Builder builder) {
        this.parameterConverter = builder.parameterConverter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public EzyQLQuery.Builder newQueryBuilder() {
        return newEmptyQueryBuilder()
            .parameterConverter(parameterConverter);
    }

    public EzyQLQuery.Builder newQueryBuilder(int parameterCount) {
        return newQueryBuilder()
            .parameterCount(parameterCount);
    }

    protected EzyQLQuery.Builder newEmptyQueryBuilder() {
        return EzyQLQuery.builder();
    }

    public EzyQLQuery newQuery(String query, Object... parameters) {
        EzyQLQuery.Builder builder = newQueryBuilder(parameters.length)
            .query(query);
        for (int i = 0; i < parameters.length; ++i) {
            builder.parameter(i, parameters[i]);
        }
        return builder.build();
    }

    public static class Builder implements EzyBuilder<EzyQLQueryFactory> {

        protected Function<Object, Object> parameterConverter;

        public Builder parameterConverter(Function<Object, Object> parameterConverter) {
            this.parameterConverter = parameterConverter;
            return this;
        }

        @Override
        public EzyQLQueryFactory build() {
            return new EzyQLQueryFactory(this);
        }
    }
}
