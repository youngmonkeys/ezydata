package com.tvd12.ezydata.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezydata.database.annotation.EzyNamedQuery;
import com.tvd12.ezydata.database.annotation.EzyResultDeserialize;
import com.tvd12.ezydata.database.annotation.EzyResultType;
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
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class EzyDatabaseContextBuilder<B extends EzyDatabaseContextBuilder<B>>
		extends EzyLoggable
		implements EzyBuilder<EzyDatabaseContext> {

	protected Set<String> packagesToScan;
	protected Set<Class<?>> repositoryClasses;
	protected Set<Class<?>> autoImplInterfaces;
	protected Set<Class<?>> queryResultClasses;
	protected List<EzyReflection> reflections;
	protected Map<Class<?>, Object> repositories;
	protected EzySimpleQueryManager queryManager;
	protected EzyBindingContextBuilder bindingContextBuilder;
	protected EzySimpleResultDeserializers resultDeserializers;
	
	public EzyDatabaseContextBuilder() {
		this.reflections = new ArrayList<>();
		this.packagesToScan = new HashSet<>();
		this.repositoryClasses = new HashSet<>();
		this.autoImplInterfaces = new HashSet<>();
		this.queryResultClasses = new HashSet<>();
		this.repositories = new HashMap<>();
		this.queryManager = new EzySimpleQueryManager();
		this.resultDeserializers = new EzySimpleResultDeserializers();
	}
	
	public B addQuery(EzyQueryEntity query) {
		this.queryManager.addQuery(query);
		return (B)this;
	}
	
	public B addQueries(Iterable<EzyQueryEntity> queries) {
		for(EzyQueryEntity query : queries)
			addQuery(query);
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
	
	public B scan(EzyReflection reflection) {
		reflections.add(reflection);
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
	
	public B repositoryClass(Class<?> repoClass) {
		this.repositoryClasses.add(repoClass);
		return (B)this;
	}
	
	public B repositoryClasses(Class<?>... repoClasses) {
		return repositoryClasses(Sets.newHashSet(repoClasses));
	}
	
	public B repositoryClasses(Iterable<Class<?>> repoClasses) {
		for(Class<?> repoClass : repoClasses)
			repositoryClass(repoClass);
		return (B)this;
	}
	
	public B queryResultClass(Class<?> resultClass) {
		this.queryResultClasses.add(resultClass);
		return (B)this;
	}
	
	public B queryResultClasses(Class<?>... resultClasses) {
		return queryResultClasses(Sets.newHashSet(resultClasses));
	}
	
	public B queryResultClasses(Iterable<Class<?>> resultClasses) {
		for(Class<?> resultClass : resultClasses)
			this.queryResultClasses.add(resultClass);
		return (B)this;
	}
	
	public B bindingContextBuilder(EzyBindingContextBuilder bindingContextBuilder) {
		this.bindingContextBuilder = bindingContextBuilder;
		return (B)this;
	}
	
	public B addResultDeserializer(Class<?> resultType, EzyResultDeserializer deserializer) {
		this.queryResultClasses.add(resultType);
		this.resultDeserializers.addDeserializer(resultType, deserializer);
		return (B)this;
	}
	
	public B addResultDeserializesr(Map<Class<?>, EzyResultDeserializer> deserializers) {
		for(Class<?> resultType : deserializers.keySet()) {
			EzyResultDeserializer deserializer = deserializers.get(resultType);
			addResultDeserializer(resultType, deserializer);
		}
		return (B)this;
	}
	
	@Override
	public EzyDatabaseContext build() {
		if(packagesToScan.size() > 0)
			reflections.add(new EzyReflectionProxy(packagesToScan));
		if(bindingContextBuilder == null)
			bindingContextBuilder = EzyBindingContext.builder();
		for(EzyReflection reflection : reflections)
			bindingContextBuilder.addAllClasses(reflection);
		preBuild();
		scanAndAddQueries();
		scanAndAddResultTypes();
		scanAndAddRepoClasses();
		EzySimpleDatabaseContext context = newDatabaseContext();
		context.setQueryManager(queryManager);
		context.setDeserializers(resultDeserializers);
		addRepositoriesFromClasses(context);
		implementAutoImplRepositories(context);
		createResultDeserializers();
		context.setRepositories((Map)repositories);
		printDatabaseContextInformation(context);
		postBuild();
		return context;
	}
	
	protected abstract EzySimpleDatabaseContext newDatabaseContext();

	protected void preBuild() {}
	protected void postBuild() {}
	
	protected void scanAndAddQueries() {
		for(EzyReflection reflection : reflections) {
			scanAndAddNamedQueries(reflection);
			scanAndAddQueries(reflection);
		}
	}
	
	protected void scanAndAddQueries(EzyReflection reflection) {}
	
	protected void scanAndAddNamedQueries(EzyReflection reflection) {
		Set<Class<?>> resultClasses = reflection.getAnnotatedClasses(EzyNamedQuery.class);
		for(Class<?> resultClass : resultClasses) {
			EzyNamedQuery anno = resultClass.getAnnotation(EzyNamedQuery.class);
			String queryName = anno.name();
			EzyQueryEntity queryEntity = getQuery(queryName, anno.type(), anno.value(), resultClass);
			queryEntity.setNativeQuery(anno.nativeQuery());
		}
	}
	
	protected EzyQueryEntity getQuery(String name, String type, String value, Class<?> resultClass) {
		String queryName = name;
		String queryType = type;
		String queryValue = value;
		if(EzyStrings.isNoContent(name))
			queryName = resultClass.getName();
		EzyQueryEntity queryEntity = queryManager.getQuery(queryName);
		if(queryEntity != null) {
			if(queryEntity.getResultType() != resultClass)
				throw new IllegalStateException("too many result type of query: " + queryName + "(" + queryEntity.getResultType().getName() + ", " + resultClass.getName() + ")");
		}
		else {
			if(EzyStrings.isNoContent(queryValue))
				throw new IllegalStateException("has no query with name: " + queryName);
			queryEntity = EzyQueryEntity.builder()
					.name(queryName)
					.type(queryType)
					.value(queryValue)
					.resultType(resultClass)
					.build();
			queryManager.addQuery(queryEntity);
		}
		return queryEntity;
	}
	
	protected void scanAndAddRepoClasses() {
		for(EzyReflection reflection: reflections) {
			repositoryClasses.addAll(reflection.getAnnotatedClasses(EzyRepository.class));
		}
	}
	
	protected void scanAndAddResultTypes() {
		for(EzyReflection reflection: reflections) {
			queryResultClasses.addAll(reflection.getAnnotatedClasses(EzyResultType.class));
		}
	}
	
	protected void scanAndAddResultDeserializers() {
		for(EzyReflection reflection: reflections) 
			scanAndAddResultDeserializers(reflection);
	}
	
	protected void scanAndAddResultDeserializers(EzyReflection reflection) {
		Set<Class> classes = (Set)reflection.getAnnotatedClasses(EzyResultDeserialize.class);
		for(Class<EzyResultDeserializer> clazz : classes) {
			EzyResultDeserialize anno = clazz.getAnnotation(EzyResultDeserialize.class);
			Class<?> resultType = anno.value();
			if(EzyResultDeserializer.class.isAssignableFrom(clazz)) {
				EzyResultDeserializer deserializer = EzyClasses.newInstance(clazz);
				resultDeserializers.addDeserializer(resultType, deserializer);
			}
			else {
				throw new IllegalStateException(clazz + " must implement interface " + EzyResultDeserializer.class.getName());
			}
		}
	}
	
	private void createResultDeserializers() {
		Map<String, EzyQueryEntity> queries = queryManager.getQueries();
		for(EzyQueryEntity query : queries.values()) {
			Class<?> resultType = query.getResultType();
			if(resultType == Object.class)
				continue;
			queryResultClasses.add(resultType);
		}
		Set<Class<?>> unknownDeserializerResultTypes = new HashSet<>();
		for(Class<?> resultType : queryResultClasses) {
			EzyResultDeserializer ds = resultDeserializers.getDeserializer(resultType);
			if(ds == null) {
				unknownDeserializerResultTypes.add(resultType);
				bindResultType(bindingContextBuilder, resultType);
			}
		}
		EzySimpleBindingContext bindingContext = bindingContextBuilder.build();
		EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
		for(Class<?> resultType : unknownDeserializerResultTypes) {
			EzyResultDeserializer deserializer = 
					newResultDeserializer(resultType, unmarshaller);
			resultDeserializers.addDeserializer(resultType, deserializer);
		}
	}
	
	protected EzyResultDeserializer 
			newResultDeserializer(Class<?> resultType, EzyUnmarshaller unmarshaller) {
		return new EzyBindResultDeserializer(resultType, unmarshaller);
	}
	
	protected void bindResultType(EzyBindingContextBuilder builder, Class<?> resultType) {
		builder.addClass(resultType);
	}
	
	protected void addRepositoriesFromClasses(EzyDatabaseContext context) {
		for(Class<?> repoClass : repositoryClasses) {
			Object repo = EzyClasses.newInstance(repoClass);
			if(repo instanceof EzyDatabaseContextAware)
				((EzyDatabaseContextAware)repo).setDatabaseContext(context);
			postCreateRepositoryFromClass(context, repo);
			repositories.put(repoClass, repo);
		}
	}
	
	protected void postCreateRepositoryFromClass(EzyDatabaseContext context, Object repo) {}
	
	private void implementAutoImplRepositories(EzySimpleDatabaseContext context) {
		EzyAbstractRepositoriesImplementer implementer = createRepositoriesImplementer();
		Map<Class<?>, Object> implementedRepos = implementer.implement(context);
		repositories.putAll((Map)implementedRepos);
	}
	
	private EzyAbstractRepositoriesImplementer createRepositoriesImplementer() {
		EzyAbstractRepositoriesImplementer answer = newRepositoriesImplementer();
		answer.queryManager(queryManager);
		for(EzyReflection reflection : reflections)
			answer.repositoryInterfaces(reflection);
		answer.repositoryInterfaces(autoImplInterfaces);
		for(EzyReflection reflection : reflections)
			answer.repositoryInterfaces(reflection);
		return answer;
	}
	
	protected abstract EzyAbstractRepositoriesImplementer newRepositoriesImplementer();

	protected void printDatabaseContextInformation(EzyDatabaseContext context) {
		logger.debug("\n{}\n{}\n{}",
				"====================== DATABASE CONTEXT ===============",
				context,
				"=======================================================");
	}
	
}
