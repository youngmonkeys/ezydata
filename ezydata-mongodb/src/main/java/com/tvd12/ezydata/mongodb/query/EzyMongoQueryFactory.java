package com.tvd12.ezydata.mongodb.query;

import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.database.query.EzyQLQueryFactory;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;

import java.util.function.Function;

public class EzyMongoQueryFactory extends EzyQLQueryFactory {

    protected final EzyMongoDataConverter dataConverter;

    public EzyMongoQueryFactory(Builder builder) {
        super(builder);
        this.dataConverter = builder.dataConverter;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public EzyQLQuery.Builder newEmptyQueryBuilder() {
        return EzyMongoQuery.builder()
            .dataConverter(dataConverter);
    }

    public static class Builder extends EzyQLQueryFactory.Builder {

        protected EzyMongoDataConverter dataConverter;

        public Builder dataConverter(EzyMongoDataConverter dataConverter) {
            this.dataConverter = dataConverter;
            return this;
        }

        @Override
        public Builder parameterConverter(Function<Object, Object> parameterConverter) {
            super.parameterConverter(parameterConverter);
            return this;
        }

        @Override
        public EzyMongoQueryFactory build() {
            return new EzyMongoQueryFactory(this);
        }

    }
}
