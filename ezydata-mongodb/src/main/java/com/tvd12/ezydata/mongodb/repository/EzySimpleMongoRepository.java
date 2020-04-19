package com.tvd12.ezydata.mongodb.repository;

import static com.tvd12.ezydata.database.util.EzyCollectionAnnotations.getCollectionName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.mongodb.EzyMongoCollectionAware;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzySimpleMongoRepository<I,E>
		extends EzyLoggable
		implements EzyMongoRepository<I, E>, EzyDatabaseContextAware, EzyMongoCollectionAware {

	protected final Class<E> entityType;
	protected final String collectionName;
	protected MongoCollection<BsonDocument> collection;
	protected EzyMongoDatabaseContext databaseContext;
	
	public EzySimpleMongoRepository() {
		this.entityType = getEntityType();
		this.collectionName = getCollectionName(entityType);
	}
	
	public void setDatabaseContext(EzyDatabaseContext databaseContext) {
		this.databaseContext = (EzyMongoDatabaseContext) databaseContext;
		this.collection = this.databaseContext.getCollection(collectionName, BsonDocument.class);
	}
	
	@Override
	public void setCollection(MongoCollection collection) {
		this.collection = collection;
	}
	
	@Override
	public long count() {
		long count = collection.countDocuments();
		return count;
	}

	@Override
	public void save(E entity) {
		BsonDocument document = entityToBsonDocument(entity);
		collection.insertOne(document);
//		BsonValue resultId = document.get("_id");
	}

	@Override
	public void save(Iterable<E> entities) {
		List<BsonDocument> documents = new ArrayList<>();
		for(E entity : entities)
			documents.add(entityToBsonDocument(entity));
		collection.insertMany(documents);
	}

	@Override
	public E findById(I id) {
		BsonDocument filter = new BsonDocument();
		BsonValue bsonId = dataToBsonValue(id);
		filter.put("_id", bsonId);
		FindIterable<BsonDocument> list = collection.find(filter);
		E entity = bsonDocumentToEntity(list.first());
		return entity;
	}

	@Override
	public List<E> findListByIds(Collection<I> ids) {
		BsonDocument filter = new BsonDocument();
		BsonArray bsonIds = new BsonArray();
		for(I id : ids)
			bsonIds.add(dataToBsonValue(id));
		filter.put("_id", new BsonDocument("$in", bsonIds));
		FindIterable<BsonDocument> list = collection.find(filter);
		List<E> entities = new ArrayList<>();
		for(BsonDocument document : list)
			entities.add(bsonDocumentToEntity(document));
		return entities;
	}

	@Override
	public E findByField(String field, Object value) {
		BsonDocument filter = new BsonDocument();
		BsonValue bsonId = dataToBsonValue(value);
		filter.put(field, bsonId);
		FindIterable<BsonDocument> list = collection.find(filter);
		E entity = bsonDocumentToEntity(list.first());
		return entity;
	}

	@Override
	public List<E> findListByField(String field, Object value) {
		BsonDocument filter = new BsonDocument();
		BsonValue bsonId = dataToBsonValue(value);
		filter.put(field, bsonId);
		FindIterable<BsonDocument> list = collection.find(filter);
		List<E> entities = new ArrayList<>();
		for(BsonDocument document : list)
			entities.add(bsonDocumentToEntity(document));
		return entities;
	}

	@Override
	public List<E> findListByField(String field, Object value, int skip, int limit) {
		BsonDocument filter = new BsonDocument();
		BsonValue bsonId = dataToBsonValue(value);
		filter.put(field, bsonId);
		FindIterable<BsonDocument> list = collection.find(filter)
				.skip(skip)
				.limit(limit);
		List<E> entities = new ArrayList<>();
		for(BsonDocument document : list)
			entities.add(bsonDocumentToEntity(document));
		return entities;
	}

	@Override
	public List<E> findAll() {
		FindIterable<BsonDocument> list = collection.find();
		List<E> entities = new ArrayList<>();
		for(BsonDocument document : list)
			entities.add(bsonDocumentToEntity(document));
		return entities;
	}

	@Override
	public List<E> findAll(int skip, int limit) {
		FindIterable<BsonDocument> list = collection.find()
				.skip(skip)
				.limit(limit);
		List<E> entities = new ArrayList<>();
		for(BsonDocument document : list)
			entities.add(bsonDocumentToEntity(document));
		return entities;
	}

	@Override
	public int deleteAll() {
		BsonDocument filter = new BsonDocument();
		DeleteResult result = collection.deleteMany(filter);
		return (int)result.getDeletedCount();
	}

	@Override
	public void delete(I id) {
		BsonDocument filter = new BsonDocument();
		BsonValue bsonId = dataToBsonValue(id);
		filter.put("_id", bsonId);
		collection.deleteMany(filter);
	}

	@Override
	public int deleteByIds(Collection<I> ids) {
		BsonDocument filter = new BsonDocument();
		BsonArray bsonIds = new BsonArray();
		for(I id : ids)
			bsonIds.add(dataToBsonValue(id));
		filter.put("_id", new BsonDocument("$in", bsonIds));
		DeleteResult result = collection.deleteMany(filter);
		return (int)result.getDeletedCount();
	}
	
	protected E findOneWithQuery(String query) {
		return null;
	}
	
	protected List<E> findListWithQuery(String query) {
		return null;
	}
	
	protected int updateWithQuery(String query) {
		return 0;
	}
	
	protected int deleteWithQuery(String query) {
		return 0;
	}
	
	protected <T extends BsonValue> T dataToBsonValue(Object data) {
		return databaseContext.dataToBsonValue(data);
	}
	
	protected BsonDocument entityToBsonDocument(Object entity) {
		return dataToBsonValue(entity);
	}
	
	protected E bsonDocumentToEntity(BsonDocument document) {
		return databaseContext.bsonValueToData(document, entityType);
	}
	
	protected Class<E> getEntityType() {
		try {
			Type genericSuperclass = getClass().getGenericSuperclass();
			Class[] genericArgs = EzyGenerics.getTwoGenericClassArguments(genericSuperclass);
			return genericArgs[1];
		}
		catch (Exception e) {
			throw new UnimplementedOperationException("class " + getClass().getName() + " hasn't implemented method 'getEntityType'", e);
		}
	}

}
