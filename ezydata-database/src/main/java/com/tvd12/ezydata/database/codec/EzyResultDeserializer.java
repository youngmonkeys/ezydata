package com.tvd12.ezydata.database.codec;

public interface EzyResultDeserializer {

	default Object deserialize(Object result) { 
		return result; 
	}
	
	default Object deserialize(Object result, EzyResultDeserializers deserializers) {
		return deserialize(result);
	}
	
}
