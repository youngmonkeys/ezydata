package com.tvd12.ezydata.database.bean;

import java.util.Map;

import com.tvd12.ezyfox.reflect.EzyReflection;

public interface EzyRepositoriesImplementer {
	
	public abstract EzyRepositoriesImplementer scan(String packageName);
	
	public abstract EzyRepositoriesImplementer scan(String... packageNames);
	
	public abstract EzyRepositoriesImplementer scan(Iterable<String> packageNames);
	
	public abstract EzyRepositoriesImplementer repositoryInterface(Class<?> itf);
	
	public abstract EzyRepositoriesImplementer repositoryInterfaces(Class<?>... itfs);
	
	public abstract EzyRepositoriesImplementer repositoryInterfaces(Iterable<Class<?>> itfs);
	
	public abstract EzyRepositoriesImplementer repositoryInterfaces(EzyReflection reflection);
	
	public abstract Map<Class<?>, Object> implement(Object template);
	
}
