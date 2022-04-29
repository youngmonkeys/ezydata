package com.tvd12.ezydata.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.ezydata.database.converter.EzyResultDeserializer;
import com.tvd12.ezydata.database.converter.EzyResultDeserializers;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzyQueryManager;
import com.tvd12.ezydata.database.query.EzyQueryManagerFetcher;
import com.tvd12.ezydata.database.util.EzyDatabaseRepositories;

import lombok.Getter;
import lombok.Setter;

@Setter
@SuppressWarnings({"rawtypes", "unchecked"})
public class EzySimpleDatabaseContext 
        implements EzyDatabaseContext, EzyQueryManagerFetcher {

    @Getter
    protected EzyQueryManager queryManager;
    protected EzyResultDeserializers deserializers;
    protected final Map<Class, Object> repositories;
    protected final Map<String, Object> repositoriesByName;

    protected EzySimpleDatabaseContext() {
        this.repositories = new HashMap<>();
        this.repositoriesByName = new HashMap<>();
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
    public List deserializeResultList(Object result, Class<?> resultItemType) {
        List answer = new ArrayList<>();
        for(Object item : (Iterable)result) {
            Object data = deserializers.deserialize(item, resultItemType);
            answer.add(data);
        }
        return answer;
    }

    @Override
    public <T> T getRepository(String name) {
        Object repo = repositoriesByName.get(name);
        if(repo == null)
            throw new IllegalArgumentException("has no repository with name: " + name);
        return (T)repo;
    }

    @Override
    public <T> T getRepository(Class<T> repoType) {
        Object repo = repositories.get(repoType);
        if(repo == null)
            throw new IllegalArgumentException("has no repository with type: " + repoType.getName());
        return (T)repo;
    }

    @Override
    public Map<Class, Object> getRepositories() {
        return new HashMap<>(repositories);
    }

    @Override
    public Map<String, Object> getRepositoriesByName() {
        return new HashMap<>(repositoriesByName);
    }

    public void setRepositories(Map<Class, Object> repos) {
        for(Class repoType : repos.keySet()) {
            Object repo = repos.get(repoType);
            String repoName = EzyDatabaseRepositories.getRepoName(repoType);
            repositories.put(repoType, repo);
            repositoriesByName.put(repoName, repo);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Map<String, EzyQueryEntity> queries = queryManager.getQueries();
        builder.append("list of queries:");
        if(queries.isEmpty())
            builder.append(" empty");
        else
            builder.append(" ").append(queries.size());
        for(EzyQueryEntity query : queries.values()) {
            builder.append("\n");
            builder.append(query.getName()).append("=").append(query.getValue());
        }
        builder.append("\n\nlist of repositories:");
        if(repositoriesByName.isEmpty())
            builder.append(" empty");
        else
            builder.append(" ").append(repositoriesByName.size());
        for(String repoName : repositoriesByName.keySet()) {
            Object repo = repositoriesByName.get(repoName);
            builder.append("\n");
            builder.append(repoName).append("=").append(repo.getClass().getName());
        }
        Map<Class<?>, EzyResultDeserializer> resultDeserializers = deserializers.getDeserializers();
        builder
            .append("\n\nlist of result deserializers:")
            .append(" ")
            .append(resultDeserializers.size());
        for(Class<?> resultType : resultDeserializers.keySet()) {
            builder.append("\n");
            Object deserializer = resultDeserializers.get(resultType);
            builder.append(resultType.getName()).append("=").append(deserializer.getClass().getName());
        }
        return builder.toString();
    }

}
