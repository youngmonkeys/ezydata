package com.tvd12.ezydata.database.codec;

public interface EzyResultDeserializer {

	default Object deserialize(Object data) { 
		return data; 
	}
	
	default Object deserialize(Object data, EzyResultDeserializers deserializers) {
		return deserialize(data);
	}
	
}
