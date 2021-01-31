package com.tvd12.ezydata.database.converter;

import com.tvd12.ezydata.database.annotation.EzyResultDeserialize;
import com.tvd12.ezyfox.reflect.EzyGenerics;

public interface EzyResultDeserializer<T> {
	
	@SuppressWarnings("rawtypes")
	EzyResultDeserializer DEFAULT = new EzyResultDeserializer() {};

	@SuppressWarnings("unchecked")
	default T deserialize(Object data) { 
		return (T)data; 
	}
	
	default T deserialize(Object data, EzyResultDeserializers deserializers) {
		return deserialize(data);
	}
	
	default Class<?> getOutType() {
		try {
			Class<?> readerClass = getClass();
			Class<?>[] args = EzyGenerics.getGenericInterfacesArguments(
					readerClass, 
					EzyResultDeserializer.class, 1);
			return args[0];
		}
		catch(Exception e) {
			EzyResultDeserialize anno = 
					getClass().getAnnotation(EzyResultDeserialize.class);
			return anno != null ? anno.value() : null;
		}
	}
}
