package com.tvd12.ezydata.mongodb.reflect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyValue;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;
import com.tvd12.ezyfox.reflect.EzyGetterBuilder;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyObjectProxy.Builder;
import com.tvd12.ezyfox.reflect.EzyObjectProxyProvider;
import com.tvd12.ezyfox.reflect.EzySetterBuilder;

public class EzyMongoObjectProxyProvider extends EzyObjectProxyProvider {

	@Override
	protected Map<String, String> getFieldKeys(Collection<EzyField> fields) {
		Map<String, String> map = new HashMap<>();
		for(EzyField field : fields) {
			String name = field.getName();
			EzyId idAnno = field.getAnnotation(EzyId.class);
			if(idAnno != null) {
				map.put("_id", name);
				continue;
			}
			EzyValue valueAnno = field.getAnnotation(EzyValue.class);
			if(valueAnno != null) {
				map.put(valueAnno.value(), name);
				continue;
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void preBuildObjectProxy(EzyClass clazz, Builder builder) {
		Optional<EzyMethod> idGetterMethod = clazz.getAnnotatedGetterMethod(EzyId.class);
		Optional<EzyMethod> idSetterMethod = clazz.getAnnotatedSetterMethod(EzyId.class);
		if(idGetterMethod.isPresent()) {
			String fieldName = idGetterMethod.get().getFieldName();
			builder.addGetter("_id", new EzyGetterBuilder()
					.method(idGetterMethod.get())
					.build());
			builder.propertyKey("_id", fieldName);
			builder.addPropertyType(fieldName, idGetterMethod.get().getReturnType());
			
		}
		if(idSetterMethod.isPresent()) {
			String fieldName = idSetterMethod.get().getFieldName();
			builder.addSetter("_id", new EzySetterBuilder()
					.method(idSetterMethod.get())
					.build());
			builder.propertyKey("_id", fieldName);
			builder.addPropertyType(fieldName, idSetterMethod.get().getParameterTypes()[0]);
		}
	}
	
}
