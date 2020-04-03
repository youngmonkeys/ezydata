package com.tvd12.ezydata.database.codec;

public interface EzyResultDeserializers {

	Object deserialize(Object result, Class<?> resultType); 
	
}
