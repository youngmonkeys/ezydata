package com.tvd12.ezydata.database.converter;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.util.EzyLoggable;

public class EzySimpleResultDeserializers 
		extends EzyLoggable 
		implements EzyResultDeserializers {

	protected final Map<Class<?>, EzyResultDeserializer> deserializers;
	
	public EzySimpleResultDeserializers() {
		this.deserializers = new HashMap<>();
	}
	
	@Override
	public Object deserialize(Object data, Class<?> resultType) {
		EzyResultDeserializer deserializer = deserializers.get(resultType);
		if(deserializer == null)
			throw new IllegalArgumentException("has no deserializer with type: " + resultType.getName());
		Object answer = deserializer.deserialize(data);
		return answer;
	}
	
	@Override
	public EzyResultDeserializer getDeserializer(Class<?> resultType) {
		EzyResultDeserializer deserializer = deserializers.get(resultType);
		return deserializer;
	}
	
	@Override
	public Map<Class<?>, EzyResultDeserializer> getDeserializers() {
		return new HashMap<>(deserializers);
	}
	
	public void addDeserializer(Class<?> resultType, EzyResultDeserializer deserializer) {
		if(!deserializers.containsKey(resultType))
			deserializers.put(resultType, deserializer);
	}
	
}
