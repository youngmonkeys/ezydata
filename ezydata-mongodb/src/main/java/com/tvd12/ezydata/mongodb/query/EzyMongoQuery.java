package com.tvd12.ezydata.mongodb.query;

import java.util.function.Function;

import org.bson.BsonValue;

import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;

public class EzyMongoQuery extends EzyQLQuery {

    public EzyMongoQuery(Builder builder) {
        super(builder);
    }
    
    @Override
    protected Function<Object, Object> getParameterConverter(
            EzyQLQuery.Builder builder) {
        Function<Object, Object> firstConverter = super.getParameterConverter(builder);
        EzyMongoDataConverter secondConverter = ((Builder)builder).dataConverter;
        return it -> 
            secondConverter.bsonValueToString((BsonValue) firstConverter.apply(it));
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EzyQLQuery.Builder {
        
        protected EzyMongoDataConverter dataConverter;
        
        public Builder dataConverter(EzyMongoDataConverter dataConverter) {
            this.dataConverter = dataConverter;
            return this;
        }
        
        @Override
        public EzyQLQuery build() {
            return new EzyMongoQuery(this);
        }
        
    }
    
}
