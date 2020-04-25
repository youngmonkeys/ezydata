package com.tvd12.ezydata.mongodb;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.bson.BsonValue;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.query.EzyQLQueryFactory;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.query.EzyMongoQueryFactory;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.reflect.EzyReflection;

@SuppressWarnings("rawtypes")
public class EzyMongoDatabaseContextBuilder 
		extends EzyDatabaseContextBuilder<EzyMongoDatabaseContextBuilder> {

	protected String databaseName;
	protected MongoClient mongoClient;
	protected Set<Class> entityClasses;
	protected EzyQLQueryFactory queryFactory;
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
		if(bindingContextBuilder == null)
			bindingContextBuilder = EzyBindingContext.builder();
		bindingContextBuilder.addClasses(entityClasses);
		for(EzyReflection reflection : reflections)
			bindingContextBuilder.addAllClasses(reflection);
		EzyBindingContext bindingContext = bindingContextBuilder.build();
		EzySimpleMongoDatabaseContext context = new EzySimpleMongoDatabaseContext();
		context.setClient(mongoClient);
		context.setDatabase(mongoClient.getDatabase(databaseName));
		context.setDataConverter(dataConverter);
		context.setMarshaller(bindingContext.newMarshaller());
		context.setUnmarshaller(bindingContext.newUnmarshaller());
		context.setQueryFactory(newQueryFactory(bindingContext.newMarshaller()));
		return context;
	}
	
	protected EzyMongoQueryFactory newQueryFactory(EzyMarshaller marshaller) {
		return EzyMongoQueryFactory.builder()
				.dataConverter(dataConverter)
				.parameterConveter(newQueryParameterConveter(marshaller))
				.build();
	}
	
	@Override
	protected EzyAbstractRepositoriesImplementer newRepositoriesImplementer() {
		return new EzyMongoRepositoriesImplementer();
	}
	
	protected Function<Object, Object> newQueryParameterConveter(EzyMarshaller marshaller) {
		return param -> {
			Object data = marshaller.marshal(param);
			BsonValue value = dataConverter.dataToBsonValue(data);
			return value;
		};
	}
	
}
