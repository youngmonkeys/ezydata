package com.tvd12.ezydata.mongodb.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.EzyMongoCollectionAware;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.mongodb.util.BsonDocuments;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.naming.EzyNameTranslator;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.reflect.EzyObjectProxy;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.Next;
import org.bson.*;
import org.bson.conversions.Bson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.tvd12.ezydata.mongodb.util.BsonDocuments.decorateIdValue;
import static com.tvd12.ezyfox.database.util.EzyCollectionAnnotations.getCollectionName;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzySimpleMongoRepository<I, E>
    extends EzyLoggable
    implements EzyMongoRepository<I, E>, EzyDatabaseContextAware, EzyMongoCollectionAware {

    protected final Class<E> entityType;
    protected Class<I> idType;
    protected String collectionName;
    protected EzyObjectProxy objectProxy;
    protected MongoCollection<BsonDocument> collection;
    protected EzyMongoDatabaseContext databaseContext;
    protected EzyNameTranslator collectionNameTranslator;

    public EzySimpleMongoRepository() {
        this.idType = getIdType();
        this.entityType = getEntityType();
        this.collectionName = getCollectionName(entityType);
    }

    public void setDatabaseContext(EzyDatabaseContext databaseContext) {
        this.databaseContext = (EzyMongoDatabaseContext) databaseContext;
        this.collectionNameTranslator = this.databaseContext.getCollectionNameTranslator();
        this.collectionName = collectionNameTranslator.translate(collectionName);
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
        return collection.countDocuments();
    }

    @Override
    public void save(E entity) {
        BsonDocument document = entityToBsonDocument(entity);
        BsonValue id = document.get("_id");
        if (id == BsonNull.VALUE) {
            document.remove("_id");
            collection.insertOne(document);
            BsonValue resultId = document.get("_id");
            Object idValue = bsonValueToData(resultId, idType);
            objectProxy.setProperty(entity, "_id", idValue);
        } else {
            BsonDocument filter = new BsonDocument("_id", id);
            ReplaceOptions opts = new ReplaceOptions().upsert(true);
            collection.replaceOne(filter, document, opts);
        }
    }

    @Override
    public void save(Iterable<E> entities) {
        List<E> entityList = iterableToList(entities);
        if (entityList.isEmpty()) {
            return;
        }
        List<WriteModel<BsonDocument>> request = new ArrayList<>();
        for (E entity : entityList) {
            BsonDocument document = entityToBsonDocument(entity);
            BsonValue id = document.get("_id");
            if (id == BsonNull.VALUE) {
                document.remove("_id");
                request.add(new InsertOneModel<>(document));
            } else {
                BsonDocument filter = new BsonDocument("_id", id);
                ReplaceOptions opts = new ReplaceOptions().upsert(true);
                request.add(new ReplaceOneModel<>(filter, document, opts));
            }
        }
        collection.bulkWrite(request);
        for (int i = 0; i < request.size(); ++i) {
            WriteModel<BsonDocument> model = request.get(i);
            if (model instanceof InsertOneModel) {
                InsertOneModel<BsonDocument> m = (InsertOneModel) model;
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
        FindIterable<BsonDocument> list = collection.find(filter).limit(1);
        return bsonDocumentToEntity(list.first());
    }

    @Override
    public List<E> findListByIds(Collection<I> ids) {
        BsonDocument filter = new BsonDocument();
        BsonArray bsonIds = new BsonArray();
        for (I id : ids) {
            bsonIds.add(dataToBsonValue(id));
        }
        filter.put("_id", new BsonDocument("$in", bsonIds));
        FindIterable<BsonDocument> list = collection.find(filter);
        List<E> entities = new ArrayList<>();
        for (BsonDocument document : list) {
            entities.add(bsonDocumentToEntity(document));
        }
        return entities;
    }

    @Override
    public E findByField(String field, Object value) {
        BsonDocument filter = new BsonDocument();
        BsonValue bsonId = dataToBsonValue(value);
        filter.put(field, bsonId);
        FindIterable<BsonDocument> list = collection.find(filter).limit(1);
        return bsonDocumentToEntity(list.first());
    }

    @Override
    public List<E> findListByField(String field, Object value) {
        BsonDocument filter = new BsonDocument();
        BsonValue bsonId = dataToBsonValue(value);
        filter.put(field, bsonId);
        FindIterable<BsonDocument> list = collection.find(filter);
        List<E> entities = new ArrayList<>();
        for (BsonDocument document : list) {
            entities.add(bsonDocumentToEntity(document));
        }
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
        for (BsonDocument document : list) {
            entities.add(bsonDocumentToEntity(document));
        }
        return entities;
    }

    @Override
    public List<E> findAll() {
        FindIterable<BsonDocument> list = collection.find();
        List<E> entities = new ArrayList<>();
        for (BsonDocument document : list) {
            entities.add(bsonDocumentToEntity(document));
        }
        return entities;
    }

    @Override
    public List<E> findAll(int skip, int limit) {
        FindIterable<BsonDocument> list = collection.find()
            .skip(skip)
            .limit(limit);
        List<E> entities = new ArrayList<>();
        for (BsonDocument document : list) {
            entities.add(bsonDocumentToEntity(document));
        }
        return entities;
    }

    @Override
    public int deleteAll() {
        BsonDocument filter = new BsonDocument();
        DeleteResult result = collection.deleteMany(filter);
        return (int) result.getDeletedCount();
    }

    @Override
    public void delete(I id) {
        BsonDocument filter = new BsonDocument();
        BsonValue bsonId = dataToBsonValue(id);
        filter.put("_id", bsonId);
        collection.deleteMany(filter);
    }

    @Override
    public boolean containsById(I id) {
        return containsByField("_id", id);
    }

    @Override
    public boolean containsByField(String field, Object value) {
        BsonDocument filter = new BsonDocument();
        BsonValue bsonId = dataToBsonValue(value);
        filter.put(field, bsonId);
        FindIterable<BsonDocument> list = collection.find(filter).limit(1);
        return list.first() != null;
    }

    @Override
    public int deleteByIds(Collection<I> ids) {
        BsonDocument filter = new BsonDocument();
        BsonArray bsonIds = new BsonArray();
        for (I id : ids) {
            bsonIds.add(dataToBsonValue(id));
        }
        filter.put("_id", new BsonDocument("$in", bsonIds));
        DeleteResult result = collection.deleteMany(filter);
        return (int) result.getDeletedCount();
    }

    protected E findOneWithQuery(EzyQLQuery query) {
        String queryString = query.getValue();
        logger.debug("find one with query: {}", queryString);
        BsonDocument queryDocument = BsonDocument.parse(queryString);
        BsonDocument filter = queryDocument;
        if (queryDocument.containsKey("$query")) {
            filter = queryDocument.getDocument("$query");
        }
        FindIterable<BsonDocument> find = collection
            .find(filter)
            .limit(1);
        decorateToAddProjection(find, queryDocument);
        return bsonDocumentToEntity(find.first());
    }

    protected List<E> findListWithQuery(EzyQLQuery query) {
        return findListWithQuery(query, null);
    }

    protected List<E> findListWithQuery(EzyQLQuery query, Next next) {
        String queryString = query.getValue();
        logger.debug("find list with query: {}", queryString);
        BsonDocument queryDocument = BsonDocument.parse(queryString);
        BsonDocument filter = queryDocument;
        if (queryDocument.containsKey("$query")) {
            filter = queryDocument.getDocument("$query");
        }
        FindIterable<BsonDocument> find = collection.find(filter);
        decorateToAddProjection(find, queryDocument);
        if (queryDocument.containsKey("$orderby")) {
            find.sort(queryDocument.getDocument("$orderby"));
        } else if (queryDocument.containsKey("$orderBy")) {
            find.sort(queryDocument.getDocument("$orderBy"));
        }
        if (next != null) {
            find.skip((int) next.getSkip());
            find.limit((int) next.getLimit());
        }
        List<E> entities = new ArrayList<>();
        for (BsonDocument item : find) {
            E entity = bsonDocumentToEntity(item);
            entities.add(entity);
        }
        return entities;
    }

    protected long countWithQuery(EzyQLQuery query) {
        return countWithQuery(query, null);
    }

    protected long countWithQuery(EzyQLQuery query, Next next) {
        String queryString = query.getValue();
        logger.debug("count with query: {}", queryString);
        BsonDocument queryDocument = BsonDocument.parse(queryString);
        BsonDocument filter = queryDocument;
        CountOptions opts = new CountOptions();
        if (queryDocument.containsKey("$query")) {
            filter = queryDocument.getDocument("$query");
        }
        if (next != null) {
            opts.skip((int) next.getSkip());
            opts.limit((int) next.getLimit());
        }
        return collection.countDocuments(filter, opts);
    }

    protected <R> R aggregateOneWithQuery(EzyQLQuery query, Class<R> resultType) {
        List<R> resultList = aggregateListWithQuery(query, resultType, Next.limit(1));
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    protected <R> List<R> aggregateListWithQuery(EzyQLQuery query, Class<R> resultType) {
        return aggregateListWithQuery(query, resultType, null);
    }

    protected <R> List<R> aggregateListWithQuery(EzyQLQuery query, Class<R> resultType, Next next) {
        String queryString = query.getValue();
        logger.debug("fetch list with query: {}", queryString);
        List pipeline = BsonArray.parse(queryString);
        if (next != null) {
            pipeline.add(new BsonDocument("$skip", new BsonInt32((int) next.getSkip())));
            pipeline.add(new BsonDocument("$limit", new BsonInt32((int) next.getLimit())));
        }
        AggregateIterable<BsonDocument> aggregate = collection.aggregate(pipeline);
        List<R> answer = new ArrayList<>();
        for (BsonDocument item : aggregate) {
            decorateIdValue(item);
            answer.add(bsonValueToData(item, resultType));
        }
        return answer;
    }

    protected int updateWithQuery(EzyQLQuery query) {
        String queryString = query.getValue();
        logger.debug("update with query: {}", queryString);
        BsonDocument queryDocument = BsonDocument.parse(queryString);
        BsonDocument filter = queryDocument;
        if (queryDocument.containsKey("$query")) {
            filter = queryDocument.getDocument("$query");
        }
        BsonValue update = null;
        if (queryDocument.containsKey("$update")) {
            update = queryDocument.get("$update");
        }
        if (update == null) {
            throw new IllegalArgumentException("missing $update information");
        }
        UpdateResult result = collection.updateMany(filter, (Bson) update);
        return (int) result.getModifiedCount();
    }

    protected int deleteWithQuery(EzyQLQuery query) {
        String queryString = query.getValue();
        logger.debug("delete with query: {}", queryString);
        BsonDocument queryDocument = BsonDocument.parse(queryString);
        BsonDocument filter = queryDocument;
        if (queryDocument.containsKey("$query")) {
            filter = queryDocument.getDocument("$query");
        }
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
        BsonValue idValue = document.get(idProperty);
        BsonDocuments.putIfNotNull(document, "_id", idValue);
        if (!idProperty.equals("_id")) { // remove duplicate id field
            document.remove(idProperty);
        }
        return document;
    }

    protected E bsonDocumentToEntity(BsonDocument document) {
        E entity = databaseContext.bsonValueToData(document, entityType);
        if (entity == null) {
            return null;
        }
        BsonValue documentId = document.get("_id");
        Object idValue = bsonValueToData(documentId, idType);
        objectProxy.setProperty(entity, "_id", idValue);
        return entity;
    }

    private List iterableToList(Iterable<E> iterable) {
        return Lists.tryNewArrayList(iterable);
    }

    private void decorateToAddProjection(
        FindIterable<BsonDocument> find,
        BsonDocument queryDocument
    ) {
        if (queryDocument.containsKey("$fields")) {
            BsonArray fields = queryDocument.getArray("$fields");
            find.projection(
                Projections.include(
                    fields.stream()
                        .map(it -> ((BsonString) it).getValue())
                        .collect(Collectors.toList())
                )
            );
        }
    }

    protected Class getIdType() {
        try {
            Type genericSuperclass = getClass().getGenericSuperclass();
            Class[] genericArgs = EzyGenerics.getTwoGenericClassArguments(genericSuperclass);
            return genericArgs[0];
        } catch (Exception e) {
            return null;
        }
    }

    protected Class getEntityType() {
        try {
            Type genericSuperclass = getClass().getGenericSuperclass();
            Class[] genericArgs = EzyGenerics.getTwoGenericClassArguments(genericSuperclass);
            return genericArgs[1];
        } catch (Exception e) {
            throw new UnimplementedOperationException(
                "class " + getClass().getName() + " hasn't implemented method 'getEntityType'",
                e
            );
        }
    }
}
