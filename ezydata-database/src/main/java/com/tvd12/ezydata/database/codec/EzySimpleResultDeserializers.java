package com.tvd12.ezydata.database.codec;

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
	public Object deserialize(Object result, Class<?> resultType) {
		EzyResultDeserializer deserializer = deserializers.get(resultType);
		if(deserializer == null)
			throw new IllegalArgumentException("has no deserializer with type: " + resultType.getName());
		Object answer = deserializer.deserialize(result);
		return answer;
	}
	
	public void addDeserializer(Class<?> resultType, EzyResultDeserializer deserializer) {
		this.deserializers.put(resultType, deserializer);
	}
	
}
