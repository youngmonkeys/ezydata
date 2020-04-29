package com.tvd12.ezydata.database.codec;

import java.util.Map;

public interface EzyResultDeserializers {

	Object deserialize(Object data, Class<?> resultType); 
	
	EzyResultDeserializer getDeserializer(Class<?> resultType);
	
	Map<Class<?>, EzyResultDeserializer> getDeserializers();
}
