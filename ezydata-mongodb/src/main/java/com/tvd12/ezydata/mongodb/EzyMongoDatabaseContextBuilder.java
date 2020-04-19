package com.tvd12.ezydata.mongodb;

import java.util.HashSet;
import java.util.Set;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezydata.database.annotation.EzyNamedQuery;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyReflection;

@SuppressWarnings("rawtypes")
public class EzyMongoDatabaseContextBuilder 
		extends EzyDatabaseContextBuilder<EzyMongoDatabaseContextBuilder> {

	protected String databaseName;
	protected MongoClient mongoClient;
	protected Set<Class> entityClasses;
	protected EzyMongoDataConverter dataConverter;
	
	public EzyMongoDatabaseContextBuilder() {
		super();
		this.entityClasses = new HashSet<>();
	}
	
	public EzyMongoDatabaseContextBuilder mongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder databaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder dataConverter(EzyMongoDataConverter dataConverter) {
		this.dataConverter = dataConverter;
		return this;
	}
	
	@Override
	public EzyMongoDatabaseContext build() {
		if(dataConverter == null)
			dataConverter = EzyMongoDataConverter.builder().build();
		return (EzyMongoDatabaseContext)super.build();
	}
	
	@Override
	protected void preBuild() {
		for(EzyReflection reflection : reflections)
			entityClasses.addAll(reflection.getAnnotatedClasses(EzyCollection.class));
	}
	
	@Override
	protected EzySimpleDatabaseContext newDatabaseContext() {
		EzyBindingContextBuilder bindingContextBuilder = EzyBindingContext.builder()
				.addClasses(entityClasses);
		for(EzyReflection reflection : reflections)
			bindingContextBuilder.addAllClasses(reflection);
		EzyBindingContext bindingContext = bindingContextBuilder.build();
		EzySimpleMongoDatabaseContext context = new EzySimpleMongoDatabaseContext();
		context.setClient(mongoClient);
		context.setDatabase(mongoClient.getDatabase(databaseName));
		context.setDataConverter(dataConverter);
		context.setMarshaller(bindingContext.newMarshaller());
		context.setUnmarshaller(bindingContext.newUnmarshaller());
		return context;
	}
	
	@Override
	protected EzyAbstractRepositoriesImplementer newRepositoriesImplementer() {
		return new EzyMongoRepositoriesImplementer();
	}
	
	@Override
	protected void scanAndAddQueries(EzyReflection reflection) {
		Set<Class<?>> resultClasses = reflection.getAnnotatedClasses(EzyNamedQuery.class);
		for(Class<?> resultClass : resultClasses) {
			EzyNamedQuery anno = resultClass.getAnnotation(EzyNamedQuery.class);
			String queryName = anno.name();
			EzyQueryEntity queryEntity = getQuery(queryName, anno.value(), resultClass);
			queryEntity.setNativeQuery(false);
		}
	}
	
	protected EzyQueryEntity getQuery(String name, String value, Class<?> resultClass) {
		String queryName = name;
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
					.value(queryValue)
					.resultType(resultClass)
					.build();
			queryManager.addQuery(queryEntity);
		}
		return queryEntity;
	}
	
}
