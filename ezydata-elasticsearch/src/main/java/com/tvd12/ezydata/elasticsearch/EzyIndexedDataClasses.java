package com.tvd12.ezydata.elasticsearch;

import java.util.Set;

@SuppressWarnings("rawtypes")
public interface EzyIndexedDataClasses {
	
	static EzyIndexedDataClassesBuilder builder() {
		return EzySimpleIndexedDataClasses.builder();
	}
	
	Set<Class> getIndexedClasses();
	
	Set<String> getIndexes(Class clazz);
	
	
}
