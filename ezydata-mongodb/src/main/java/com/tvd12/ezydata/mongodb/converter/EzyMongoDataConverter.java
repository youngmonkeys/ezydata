package com.tvd12.ezydata.mongodb.converter;

import org.bson.BsonValue;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyMongoDataConverter {
	
	protected final EzyMongoDataToBsonValue dataToBsonValue;

	protected EzyMongoDataConverter(Builder builder) {
		this.dataToBsonValue = builder.dataToBsonValue;
	}

	public <T> T bsonValueToData(Object value) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BsonValue> T dataToBsonValue(Object data) {
		BsonValue value = dataToBsonValue.convert(data);
		return (T)value;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyMongoDataConverter> {
		
		protected EzyMongoDataToBsonValue dataToBsonValue;
		
		public Builder dataToBsonValue(EzyMongoDataToBsonValue dataToBsonValue) {
			this.dataToBsonValue = dataToBsonValue;
			return this;
		}
		
		@Override
		public EzyMongoDataConverter build() {
			if(dataToBsonValue == null)
				dataToBsonValue = new EzyMongoDataToBsonValue();
			return new EzyMongoDataConverter(this);
		}
		
	}
	
}
