package com.tvd12.ezydata.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.codec.EzyBindResultDeserializer;
import com.tvd12.ezydata.database.codec.EzyResultDeserializer;
import com.tvd12.ezydata.database.codec.EzySimpleResultDeserializers;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzySimpleQueryManager;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class EzyDatabaseContextBuilder<B extends EzyDatabaseContextBuilder<B>>
		extends EzyLoggable
		implements EzyBuilder<EzyDatabaseContext> {

	protected Set<String> packagesToScan;
	protected Set<Class<?>> autoImplInterfaces;
	protected List<EzyReflection> reflections;
	protected EzySimpleQueryManager queryManager;
	protected EzySimpleResultDeserializers resultDeserializers;
	protected Map<Class<?>, EzyDatabaseRepository> repositories;
	
	public EzyDatabaseContextBuilder() {
		this.reflections = new ArrayList<>();
		this.packagesToScan = new HashSet<>();
		this.autoImplInterfaces = new HashSet<>();
		this.repositories = new HashMap<>();
		this.queryManager = new EzySimpleQueryManager();
		this.resultDeserializers = new EzySimpleResultDeserializers();
	}
	
	public B addQuery(EzyQueryEntity query) {
		this.queryManager.addQuery(query);
		return (B)this;
	}
	
	public B scan(String packageName) {
		packagesToScan.add(packageName);
		return (B)this;
	}
	
	public B scan(String... packageNames) {
		return scan(Sets.newHashSet(packageNames));
	}
	
	public B scan(Iterable<String> packageNames) {
		for(String packageName : packageNames)
			this.scan(packageName);
		return (B)this;	
	}
	
	public B repositoryInterface(Class<?> itf) {
		this.autoImplInterfaces.add(itf);
		return (B)this;
	}
	
	public B repositoryInterfaces(Class<?>... itfs) {
		return repositoryInterfaces(Sets.newHashSet(itfs));
	}
	
	public B repositoryInterfaces(Iterable<Class<?>> itfs) {
		for(Class<?> itf : itfs)
			this.repositoryInterface(itf);
		return (B)this;
	}
	
	public B repositoryInterfaces(EzyReflection reflection) {
		this.reflections.add(reflection);
		return (B)this;
	}
	
	public B addResultDeserializer(Class<?> resultType, EzyResultDeserializer deserializer) {
		this.resultDeserializers.addDeserializer(resultType, deserializer);
		return (B)this;
	}
	
	@Override
	public EzyDatabaseContext build() {
		EzySimpleDatabaseContext context = newDatabaseContext();
		context.setQueryManager(queryManager);
		context.setDeserializers(resultDeserializers);
		implementAutoImplRepositories(context);
		registerQueries();
		createResultDeserializers();
		context.setRepositories((Map)repositories);
		return context;
	}
	
	protected abstract EzySimpleDatabaseContext newDatabaseContext();
	
	private void createResultDeserializers() {
		EzyBindingContextBuilder bindingContextBuilder = EzyBindingContext.builder();
		Map<String, EzyQueryEntity> queries = queryManager.getQueries();
		for(EzyQueryEntity query : queries.values()) {
			Class<?> resultType = query.getResultType();
			if(resultType != Object.class)
				bindResultType(bindingContextBuilder, query.getResultType());
		}
		EzySimpleBindingContext bindingContext = bindingContextBuilder.build();
		EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
		for(EzyQueryEntity query : queries.values()) {
			Class<?> resultType = query.getResultType();
			EzyResultDeserializer deserializer = 
					new EzyBindResultDeserializer(resultType, unmarshaller);
			resultDeserializers.addDeserializer(resultType, deserializer);
		}
	}
	
	protected void bindResultType(EzyBindingContextBuilder builder, Class<?> resultType) {
		builder.addArrayBindingClass(resultType);
	}
	
	private void implementAutoImplRepositories(EzySimpleDatabaseContext context) {
		EzyAbstractRepositoriesImplementer implementer = createRepositoriesImplementer();
		Map<Class<?>, Object> implementedRepos = implementer.implement(context);
		repositories.putAll((Map)implementedRepos);
	}
	
	private EzyAbstractRepositoriesImplementer createRepositoriesImplementer() {
		EzyAbstractRepositoriesImplementer answer = newRepositoriesImplementer();
		answer.repositoryInterfaces(autoImplInterfaces);
		for(EzyReflection reflection : reflections)
			answer.repositoryInterfaces(reflection);
		return answer;
	}
	
	protected void registerQueries() {
		registerQueriesFromRepoClasses();
	}
	
	protected void registerQueriesFromRepoClasses() {
		for(Class<?> repoClass : repositories.keySet()) {
			EzyClass clazz = new EzyClass(repoClass);
			List<EzyMethod> methods 
				= clazz.getMethods(m -> m.isAnnotated(EzyQuery.class));
			for(EzyMethod method : methods)
				registerQuery(method);
		}
	}
	
	protected void registerQuery(EzyMethod method) {
		EzyQuery queryAnno = method.getAnnotation(EzyQuery.class);
		if(queryAnno == null)
			return;
		String queryValue = queryAnno.value();
		if(EzyStrings.isNoContent(queryValue))
			return;
		String queryName = queryAnno.name();
		if(EzyStrings.isNoContent(queryName))
			queryName = method.toString();
		EzyQueryEntity queryEntity = EzyQueryEntity.builder()
				.name(queryName)
				.value(queryValue)
				.nativeQuery(queryAnno.nativeQuery())
				.resultType(queryAnno.resultType())
				.build();
		queryManager.addQuery(queryEntity);
	}
	
	protected abstract EzyAbstractRepositoriesImplementer newRepositoriesImplementer();

}
