package com.tvd12.ezydata.database.converter;

public interface EzyResultDeserializer {
	
	EzyResultDeserializer DEFAULT = new EzyResultDeserializer() {};

	default Object deserialize(Object data) { 
		return data; 
	}
	
	default Object deserialize(Object data, EzyResultDeserializers deserializers) {
		return deserialize(data);
	}
	
}
