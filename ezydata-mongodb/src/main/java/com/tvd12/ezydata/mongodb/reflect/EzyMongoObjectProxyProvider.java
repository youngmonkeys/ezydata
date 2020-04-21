package com.tvd12.ezydata.mongodb.reflect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezydata.database.reflect.EzyObjectProxyProvider;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyValue;
import com.tvd12.ezyfox.reflect.EzyField;

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
	
}
