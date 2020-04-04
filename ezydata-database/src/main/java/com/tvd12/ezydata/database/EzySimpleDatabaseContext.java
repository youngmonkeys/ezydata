package com.tvd12.ezydata.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.ezydata.database.codec.EzyResultDeserializer;
import com.tvd12.ezydata.database.codec.EzyResultDeserializers;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzyQueryManager;
import com.tvd12.ezydata.database.util.EzyDatabaseRepositories;

import lombok.Setter;

@Setter
@SuppressWarnings({"rawtypes", "unchecked"})
public class EzySimpleDatabaseContext implements EzyDatabaseContext {

	protected EzyQueryManager queryManager;
	protected EzyResultDeserializers deserializers;
	protected Map<Class, EzyDatabaseRepository> repositories;
	
	protected EzySimpleDatabaseContext() {
		this.repositories = new HashMap<>();
	}
	
	@Override
	public EzyQueryEntity getQuery(String queryName) {
		EzyQueryEntity query = queryManager.getQuery(queryName);
		if(query == null)
			throw new IllegalArgumentException("has no query with name: " + queryName);
		return query;
	}

	@Override
	public Object deserializeResult(Object result, Class<?> resultType) {
		Object answer = deserializers.deserialize(result, resultType);
		return answer;
	}
	
	@Override
	public List deserializeResultList(Object result, Class<?> resultType) {
		List answer = new ArrayList<>();
		for(Object item : (Iterable)result) {
			Object data = deserializers.deserialize(item, resultType);
			answer.add(data);
		}
		return answer;
	}

	@Override
	public Map<Class, EzyDatabaseRepository> getRepositories() {
		return repositories;
	}
	
	public void setRepositories(Map<Class, EzyDatabaseRepository> repos) {
		this.repositories.putAll(repos);
	}
	
	@Override
	public <T> T getRepository(Class<T> repoType) {
		EzyDatabaseRepository repo = repositories.get(repoType);
		if(repo == null)
			throw new IllegalArgumentException("has no repository with type: " + repoType.getName());
		return (T)repo;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("list of queries:");
		Map<String, EzyQueryEntity> queries = queryManager.getQueries();
		for(EzyQueryEntity query : queries.values()) {
			builder.append("\n");
			builder.append(query.getName()).append("=").append(query.getValue());
		}
		builder.append("\n\nlist of repositories:");
		for(Class<?> repoType : repositories.keySet()) {
			Object repo = repositories.get(repoType);
			String repoName = EzyDatabaseRepositories.getRepoName(repoType);
			builder.append("\n");
			builder.append(repoName).append("=").append(repo.getClass().getName());
		}
		builder.append("\n\nlist of result deserializers:");
		Map<Class<?>, EzyResultDeserializer> resultDeserializers = deserializers.getDeserializers();
		for(Class<?> resultType : resultDeserializers.keySet()) {
			builder.append("\n");
			Object deserializer = resultDeserializers.get(resultType);
			builder.append(resultType.getName()).append("=").append(deserializer.getClass().getName());
		}
		return builder.toString();
	}
	
}
