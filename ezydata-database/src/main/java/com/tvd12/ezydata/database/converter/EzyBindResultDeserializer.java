package com.tvd12.ezydata.database.converter;

import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.factory.EzyEntityFactory;

import lombok.AllArgsConstructor;

import com.tvd12.ezyfox.entity.EzyArray;

@AllArgsConstructor
public class EzyBindResultDeserializer implements EzyResultDeserializer {

	protected final Class<?> resultType;
	protected final EzyUnmarshaller unmarshaller;
	
	@Override
	public Object deserialize(Object result) {
		EzyArray array = EzyEntityFactory.newArray();
		array.add((Object[])result);
		Object answer = unmarshaller.unmarshal(array, resultType);
		return answer;
	}
	
}
