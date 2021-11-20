package com.tvd12.ezydata.mongodb;

import static com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader.COLLECTION_NAMING_CASE;
import static com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader.COLLECTION_NAMING_IGNORED_SUFFIX;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.bson.BsonValue;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.query.EzyQLQueryFactory;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.converter.EzyBsonObjectIdConverter;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.converter.EzyObjectIdConverter;
import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezydata.mongodb.query.EzyMongoQueryFactory;
import com.tvd12.ezydata.mongodb.query.EzyMongoQueryMethodConverter;
import com.tvd12.ezydata.mongodb.repository.EzyMongoMaxIdRepository;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import com.tvd12.ezyfox.database.annotation.EzyCollectionId;
import com.tvd12.ezyfox.naming.EzyNameTranslator;
import com.tvd12.ezyfox.naming.EzyNamingCase;
import com.tvd12.ezyfox.naming.EzySimpleNameTranslator;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;
import com.tvd12.ezyfox.reflect.EzyReflection;

@SuppressWarnings("rawtypes")
public class EzyMongoDatabaseContextBuilder 
		extends EzyDatabaseContextBuilder<EzyMongoDatabaseContextBuilder> {

	protected String databaseName;
	protected String maxIdCollectionName;
	protected MongoClient mongoClient;
	protected Set<Class> entityClasses;
	protected EzyQLQueryFactory queryFactory;
	protected EzyMongoDataConverter dataConverter;
	protected EzyNameTranslator collectionNameTranslator;
	
	protected static final String DEFAULT_MAX_ID_COLLECTION_NAME = "___max_id___";
	
	public EzyMongoDatabaseContextBuilder() {
		super();
		this.entityClasses = new HashSet<>();
		this.maxIdCollectionName = DEFAULT_MAX_ID_COLLECTION_NAME;
	}
	
	public EzyMongoDatabaseContextBuilder mongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder databaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder maxIdCollectionName(String maxIdCollectionName) {
		this.maxIdCollectionName = maxIdCollectionName;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder dataConverter(EzyMongoDataConverter dataConverter) {
		this.dataConverter = dataConverter;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder collectionNameTranslator(EzyNameTranslator collectionNameTranslator) {
		this.collectionNameTranslator = collectionNameTranslator;
		return this;
	}
	
	public EzyMongoDatabaseContextBuilder collectionNameTranslator(EzyNamingCase namingCase, String ignoredSuffix) {
		return collectionNameTranslator(EzySimpleNameTranslator.builder()
				.namingCase(namingCase)
				.ignoredSuffix(ignoredSuffix)
				.build()
		);
	}
	
	@Override
	public EzyMongoDatabaseContext build() {
		if(databaseName == null)
			databaseName = properties.getProperty(EzyMongoClientLoader.DATABASE);
		if(dataConverter == null)
			dataConverter = EzyMongoDataConverter.builder().build();
		return (EzyMongoDatabaseContext)super.build();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void preBuild() {
		for(EzyReflection reflection : reflections) {
			entityClasses.addAll(reflection.getAnnotatedClasses(EzyCollection.class));
			bindingContextBuilder
				.addAllClasses(reflection)
				.addClasses((Set)reflection.getAnnotatedClasses(EzyId.class))
				.addClasses((Set)reflection.getAnnotatedClasses(EzyCollectionId.class));
		}
		bindingContextBuilder.addClasses(entityClasses);
		bindingContextBuilder.addTemplate(EzyObjectIdConverter.getInstance());
		bindingContextBuilder.addTemplate(EzyBsonObjectIdConverter.getInstance());
		for(Class<?> entityClass : entityClasses) {
			EzyField idField = getCollectionIdFieldOf(entityClass);
			EzyId idAnno = idField.getAnnotation(EzyId.class);
			if(idAnno != null && idAnno.composite())
				bindingContextBuilder.addClass(idField.getType());
			EzyCollectionId collectionIdAnno = idField.getAnnotation(EzyCollectionId.class);
			if(collectionIdAnno != null && collectionIdAnno.composite())
				bindingContextBuilder.addClass(idField.getType());
		}
	}
	
	private EzyField getCollectionIdFieldOf(Class<?> entityClass) {
		EzyClass clazz = new EzyClass(entityClass);
		return clazz.getField(f ->
			f.isAnnotated(EzyCollectionId.class) ||
			f.isAnnotated(EzyId.class)
		)
				.orElseThrow(() ->
					new IllegalArgumentException(
						"there is no Id field in entity class: " + entityClass.getName() +
						", please annotated Id field with @EzyCollectionId or @EzyId")
				);
	}
	
	@Override
	protected EzyQueryMethodConverter newQueryMethodConverter() {
		return new EzyMongoQueryMethodConverter();
	}
	
	@Override
	protected EzySimpleDatabaseContext newDatabaseContext() {
		MongoDatabase database = mongoClient.getDatabase(databaseName);
		addMaxIdRepository(database);
		EzySimpleMongoDatabaseContext context = new EzySimpleMongoDatabaseContext();
		context.setClient(mongoClient);
		context.setDatabase(database);
		context.setDataConverter(dataConverter);
		context.setCollectionNameTranslator(getOrCreateCollectionNameTranslator());
		return context;
	}
	

	@Override
	protected void postBuild(
			EzySimpleDatabaseContext ctx, 
			EzyBindingContext bindingContext) {
		EzySimpleMongoDatabaseContext context = (EzySimpleMongoDatabaseContext)ctx;
		EzyMarshaller marshaller = bindingContext.newMarshaller();
		EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
		context.setMarshaller(marshaller);
		context.setUnmarshaller(unmarshaller);
		context.setQueryFactory(newQueryFactory(marshaller));
	}
	
	protected void addMaxIdRepository(MongoDatabase database) {
		try {
			EzyMaxIdRepository repository = new EzyMongoMaxIdRepository(
					database.getCollection(maxIdCollectionName)
			);
			repositories.put(EzyMaxIdRepository.class, repository);
		}
		catch (Exception e) {
			logger.warn("can't create MaxIdRepository", e);
		}
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
	
	protected EzyNameTranslator getOrCreateCollectionNameTranslator() {
		if(collectionNameTranslator == null) {
			EzyNamingCase namingCase = 
					EzyNamingCase.of(properties.getProperty(COLLECTION_NAMING_CASE));
			String ignoredSuffix = 
					properties.getProperty(COLLECTION_NAMING_IGNORED_SUFFIX);
			collectionNameTranslator(namingCase, ignoredSuffix);
		}
		return collectionNameTranslator;
	}
	
}
