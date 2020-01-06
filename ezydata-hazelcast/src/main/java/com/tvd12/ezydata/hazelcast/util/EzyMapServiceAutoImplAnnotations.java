package com.tvd12.ezydata.hazelcast.util;

import com.tvd12.ezydata.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyClasses;

public final class EzyMapServiceAutoImplAnnotations {

	private EzyMapServiceAutoImplAnnotations() {
	}
	
	public static String getBeanName(Class<?> annotatedClass) {
		EzyMapServiceAutoImpl anno = annotatedClass.getAnnotation(EzyMapServiceAutoImpl.class);
		String beanName = anno.name();
		return EzyStrings.isNoContent(beanName) ? EzyClasses.getVariableName(annotatedClass) : beanName;
	}
	
}
