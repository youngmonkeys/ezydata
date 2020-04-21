package com.tvd12.ezydata.mongodb.converter;

import org.bson.BsonValue;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyMongoDataConverter {
	
	protected final EzyMongoDataToBsonValue dataToBsonValue;
	protected final EzyMongoBsonValueToData bsonValueToData;

	protected EzyMongoDataConverter(Builder builder) {
		this.dataToBsonValue = builder.dataToBsonValue;
		this.bsonValueToData = builder.bsonValueToData;
	}

	@SuppressWarnings("unchecked")
	public <T> T bsonValueToData(BsonValue value) {
		Object data = bsonValueToData.convert(value);
		return (T)data;
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
		protected EzyMongoBsonValueToData bsonValueToData;
		
		public Builder dataToBsonValue(EzyMongoDataToBsonValue dataToBsonValue) {
			this.dataToBsonValue = dataToBsonValue;
			return this;
		}
		
		public Builder bsonValueToData(EzyMongoBsonValueToData bsonValueToData) {
			this.bsonValueToData = bsonValueToData;
			return this;
		}
		
		@Override
		public EzyMongoDataConverter build() {
			if(dataToBsonValue == null)
				dataToBsonValue = new EzyMongoDataToBsonValue();
			if(bsonValueToData == null)
				bsonValueToData = new EzyMongoBsonValueToData();
			return new EzyMongoDataConverter(this);
		}
		
	}
	
}
