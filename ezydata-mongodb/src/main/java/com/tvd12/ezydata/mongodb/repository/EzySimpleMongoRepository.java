package com.tvd12.ezydata.mongodb.repository;

import static com.tvd12.ezydata.database.util.EzyCollectionAnnotations.getCollectionName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonNull;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.database.reflect.EzyObjectProxy;
import com.tvd12.ezydata.mongodb.EzyMongoCollectionAware;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzySimpleMongoRepository<I,E>
		extends EzyLoggable
		implements EzyMongoRepository<I, E>, EzyDatabaseContextAware, EzyMongoCollectionAware {

	protected Class<I> idType;
	protected final Class<E> entityType;
	protected final String collectionName;
	protected EzyObjectProxy objectProxy;
	protected MongoCollection<BsonDocument> collection;
	protected EzyMongoDatabaseContext databaseContext;
	
	public EzySimpleMongoRepository() {
		this.idType = getIdType();
		this.entityType = getEntityType();
		this.collectionName = getCollectionName(entityType);
	}
	
	public void setDatabaseContext(EzyDatabaseContext databaseContext) {
		this.databaseContext = (EzyMongoDatabaseContext) databaseContext;
		this.objectProxy = this.databaseContext.getObjectProxy(entityType);
		this.idType = (Class<I>) objectProxy.getPropertyType("_id");
		this.setCollection(this.databaseContext.getCollection(collectionName, BsonDocument.class));
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
		BsonValue id = document.get("_id");
		if(id == BsonNull.VALUE) {
			document.remove("_id");
			collection.insertOne(document);
			BsonValue resultId = document.get("_id");
			Object idValue = bsonValueToData(resultId, idType);
			objectProxy.setProperty(entity, "_id", idValue);
		}
		else {
			BsonDocument filter = new BsonDocument("_id", id);
			ReplaceOptions opts = new ReplaceOptions().upsert(true);
			collection.replaceOne(filter, document, opts);
		}
	}

	@Override
	public void save(Iterable<E> entities) {
		List<E> entityList = iterableToList(entities);
		List<WriteModel<BsonDocument>> request = new ArrayList<>();
		for(E entity : entityList) {
			BsonDocument document = entityToBsonDocument(entity);
			BsonValue id = document.get("_id");
			if(id == BsonNull.VALUE) {
				document.remove("_id");
				request.add(new InsertOneModel<>(document));
			}
			else {
				BsonDocument filter = new BsonDocument("_id", id);
				ReplaceOptions opts = new ReplaceOptions().upsert(true);
				request.add(new ReplaceOneModel<>(filter, document, opts));
			}
		}
		collection.bulkWrite(request);
		for(int i = 0 ; i< request.size() ; ++i) {
			WriteModel<BsonDocument> model = request.get(i);
			if(model instanceof InsertOneModel) {
				InsertOneModel<BsonDocument> m = (InsertOneModel)model;
				BsonDocument document = m.getDocument();
				Object idValue = bsonValueToData(document.get("_id"), idType);
				objectProxy.setProperty(entityList.get(i), "_id", idValue);
			}
		}
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
	
	protected E findOneWithQuery(EzyQLQuery query) {
		String queryString = query.getValue();
		logger.debug("find one with query: {}", queryString);
		BsonDocument queryDocument = BsonDocument.parse(queryString);
		BsonDocument filter = queryDocument;
		if(queryDocument.containsKey("$query"))
			filter = queryDocument.getDocument("$query");
		BsonDocument result = collection.find(filter).first();
		E entity = bsonDocumentToEntity(result);
		return entity;
	}
	
	protected List<E> findListWithQuery(EzyQLQuery query) {
		String queryString = query.getValue();
		logger.debug("find list with query: {}", queryString);
		BsonDocument queryDocument = BsonDocument.parse(queryString);
		BsonDocument filter = queryDocument;
		if(queryDocument.containsKey("$query"))
			filter = queryDocument.getDocument("$query");
		FindIterable<BsonDocument> find = collection.find(filter);
		if(queryDocument.containsKey("$orderby"))
			find.sort(queryDocument.getDocument("$orderby"));
		if(queryDocument.containsKey("$skip"))
			find.skip(queryDocument.getInt32("$skip").getValue());
		if(queryDocument.containsKey("$limit"))
			find.limit(queryDocument.getInt32("$limit").getValue());
		List<E> entities = new ArrayList<>();
		for(BsonDocument item : find) {
			E entity = bsonDocumentToEntity(item);
			entities.add(entity);
		}
		return entities;
	}
	
	protected long countWithQuery(EzyQLQuery query) {
		String queryString = query.getValue();
		logger.debug("find list with query: {}", queryString);
		BsonDocument queryDocument = BsonDocument.parse(queryString);
		BsonDocument filter = queryDocument;
		CountOptions opts = new CountOptions();
		if(queryDocument.containsKey("$query"))
			filter = queryDocument.getDocument("$query");
		if(queryDocument.containsKey("$skip"))
			opts.skip(queryDocument.getInt32("$skip").getValue());
		if(queryDocument.containsKey("$limit"))
			opts.limit(queryDocument.getInt32("$limit").getValue());
		long count = collection.countDocuments(filter, opts);
		return count;
	}
	
	protected <R> R aggregateOneWithQuery(EzyQLQuery query, Class<R> resultType) {
		String queryString = query.getValue();
		logger.debug("fetch one with query: {}", queryString);
		List pipeline = BsonArray.parse(queryString); 
		AggregateIterable<BsonDocument> aggregate = collection.aggregate(pipeline);
		BsonDocument result = aggregate.first();
		if(result == null)
			return null;
		R answer = bsonValueToData(result, resultType);
		return answer;
	}
	
	protected <R> List<R> aggregateListWithQuery(EzyQLQuery query, Class<R> resultType) {
		String queryString = query.getValue();
		logger.debug("fetch list with query: {}", queryString);
		List pipeline = BsonArray.parse(queryString); 
		AggregateIterable<BsonDocument> aggregate = collection.aggregate(pipeline);
		List<R> answer = new ArrayList<>();
		for(BsonDocument item : aggregate)
			answer.add(bsonValueToData(item, resultType));
		return answer;
	}
	
	protected int updateWithQuery(EzyQLQuery query) {
		String queryString = query.getValue();
		logger.debug("update with query: {}", queryString);
		BsonDocument queryDocument = BsonDocument.parse(queryString);
		BsonDocument filter = queryDocument;
		if(queryDocument.containsKey("$query"))
			filter = queryDocument.getDocument("$query");
		BsonValue update = null;
		if(queryDocument.containsKey("$update"))
			update = queryDocument.get("$update");
		if(update == null)
			throw new IllegalArgumentException("missing $update information");
		UpdateResult result = collection.updateMany(filter, (Bson)update);
		return (int)result.getModifiedCount();
	}
	
	protected int deleteWithQuery(EzyQLQuery query) {
		String queryString = query.getValue();
		logger.debug("delete with query: {}", queryString);
		BsonDocument queryDocument = BsonDocument.parse(queryString);
		BsonDocument filter = queryDocument;
		if(queryDocument.containsKey("$query"))
			filter = queryDocument.getDocument("$query");
		DeleteResult result = collection.deleteMany(filter);
		return (int) result.getDeletedCount();
	}
	
	protected EzyQLQuery.Builder newQueryBuilder() {
		return databaseContext.newQueryBuilder();
	}
	
	protected <T extends BsonValue> T dataToBsonValue(Object data) {
		return databaseContext.dataToBsonValue(data);
	}
	
	protected <T> T bsonValueToData(BsonValue value, Class<T> dataType) {
		return databaseContext.bsonValueToData(value, dataType);
	}
	
	protected BsonDocument entityToBsonDocument(Object entity) {
		BsonDocument document = dataToBsonValue(entity);
		String idProperty = objectProxy.getPropertyName("_id");
		document.put("_id", document.get(idProperty));
		if(!idProperty.equals("_id")) // remove duplicate id field
			document.remove(idProperty);
		return document;
	}
	
	protected E bsonDocumentToEntity(BsonDocument document) {
		E entity = databaseContext.bsonValueToData(document, entityType);
		if(entity == null)
			return null;
		BsonValue documentId = document.get("_id");
		Object idValue = bsonValueToData(documentId, idType);
		objectProxy.setProperty(entity, "_id", idValue);
		return entity;
	}
	
	private List iterableToList(Iterable<E> iterable) {
		if(iterable instanceof List)
			return (List)iterable;
		return Lists.newArrayList(iterable);
	}
	
	protected Class getIdType() {
		try {
			Type genericSuperclass = getClass().getGenericSuperclass();
			Class[] genericArgs = EzyGenerics.getTwoGenericClassArguments(genericSuperclass);
			return genericArgs[0];
		}
		catch (Exception e) {
			return null;
		}
	}
	
	protected Class getEntityType() {
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
