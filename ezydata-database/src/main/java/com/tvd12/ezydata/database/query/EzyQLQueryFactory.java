package com.tvd12.ezydata.database.query;

import java.util.function.Function;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyQLQueryFactory {

    protected final Function<Object, Object> parameterConveter;

    protected EzyQLQueryFactory(Builder builder) {
        this.parameterConveter = builder.parameterConveter;
    }

    public EzyQLQuery.Builder newQueryBuilder() {
        return newEmptyQueryBuilder()
                .parameterConveter(parameterConveter);
    }

    protected EzyQLQuery.Builder newEmptyQueryBuilder() {
        return EzyQLQuery.builder();
    }

    public EzyQLQuery.Builder newQueryBuilder(int parameterCount) {
        return newQueryBuilder()
                .parameterCount(parameterCount);
    }

    public EzyQLQuery newQuery(String query, Object... parameters) {
        EzyQLQuery.Builder builder = newQueryBuilder(parameters.length)
                .query(query);
        for(int i = 0 ; i < parameters.length ; ++i)
            builder.parameter(i, parameters[i]);
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyQLQueryFactory> {

        protected Function<Object, Object> parameterConveter;

        public Builder parameterConveter(Function<Object, Object> parameterConveter) {
            this.parameterConveter = parameterConveter;
            return this;
        }

        @Override
        public EzyQLQueryFactory build() {
            return new EzyQLQueryFactory(this);
        }

    }

}
