package com.tvd12.ezydata.database;

import java.util.List;
import java.util.Map;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezyfox.util.EzyCloseable;

@SuppressWarnings("rawtypes")
public interface EzyDatabaseContext extends EzyCloseable {

	<T> T getRepository(String name);
	
	<T> T getRepository(Class<T> repoType);
	
	Map<Class, Object> getRepositories();
	
	Map<String, Object> getRepositoriesByName();
	
	EzyQueryEntity getQuery(String queryName);

	Object deserializeResult(Object result, Class<?> resultType);
	
	List deserializeResultList(Object result, Class<?> resultItemType);
	
	@Override
	default void close() {}
	
}
