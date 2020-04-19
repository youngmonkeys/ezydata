package com.tvd12.ezydata.database.util;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.io.EzyStrings;

public final class EzyCollectionAnnotations {

	private EzyCollectionAnnotations() {}
	
	public static String getCollectionName(EzyCollection anno) {
		String answer = anno.value();
		if(EzyStrings.isNoContent(answer))
			answer = anno.name();
		return answer;
	}
	
	public static String getCollectionName(Class<?> entityClass) {
		EzyCollection anno = entityClass.getAnnotation(EzyCollection.class);
		if(anno != null)
			return getCollectionName(anno);
		return entityClass.getSimpleName();
	}
}
