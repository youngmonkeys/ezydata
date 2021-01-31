package com.tvd12.ezydata.database.converter;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface EzyResultDeserializers {

	Object deserialize(Object data, Class<?> resultType); 
	
	EzyResultDeserializer getDeserializer(Class<?> resultType);
	
	Map<Class<?>, EzyResultDeserializer> getDeserializers();
}
