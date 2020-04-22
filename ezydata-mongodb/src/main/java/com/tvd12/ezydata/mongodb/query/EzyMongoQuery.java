package com.tvd12.ezydata.mongodb.query;

import org.bson.BsonValue;

import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;

public class EzyMongoQuery extends EzyQLQuery {

	protected EzyMongoDataConverter dataConverter;
	
	public EzyMongoQuery(Builder builder) {
		super(builder);
	}
	
	@Override
	protected void init(EzyQLQuery.Builder builder) {
		this.dataConverter = ((Builder)builder).dataConverter;
	}

	@Override
	protected String parseParameterValue(Object value) {
		return dataConverter.bsonValueToString((BsonValue) value);
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
