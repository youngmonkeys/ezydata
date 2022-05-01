package com.tvd12.ezydata.morphia.repository;

import com.mongodb.WriteResult;
import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.morphia.EzyDatastoreAware;
import com.tvd12.ezydata.morphia.EzyMorphiaDatabaseContext;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Query;
import dev.morphia.query.internal.MorphiaCursor;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public abstract class EzyDatastoreRepository<I, E>
    extends EzyLoggable
    implements EzyDatabaseRepository<I, E>, EzyDatastoreAware, EzyDatabaseContextAware {

    protected final Class<E> entityType;
    @EzyAutoBind
    protected Datastore datastore;
    protected EzyMorphiaDatabaseContext databaseContext;

    public EzyDatastoreRepository() {
        this.entityType = getEntityType();
    }

    @Override
    public void setDatastore(Datastore datastore) {
        if (this.datastore == null) {
            this.datastore = datastore;
        } else if (this.datastore != datastore) {
            throw new IllegalStateException("set datastore twice");
        }
    }

    @Override
    public void setDatabaseContext(EzyDatabaseContext context) {
        this.databaseContext = (EzyMorphiaDatabaseContext) context;
        this.setDatastore(databaseContext.getDatastore());
    }

    @Override
    public long count() {
        Query<E> query = newQuery();
        return query.count();
    }

    @Override
    public void save(E entity) {
        datastore.save(entity);
    }

    @Override
    public void save(Iterable<E> entities) {
        datastore.save(entities);
    }

    @Override
    public E findById(I id) {
        return findByField("_id", id);
    }

    @Override
    public E findByField(String field, Object value) {
        Query<E> query = newQuery(field, value);
        return query.first();
    }

    @Override
    public List<E> findListByIds(Collection<I> ids) {
        Query<E> query = newQuery().field("_id").in(ids);
        MorphiaCursor<E> cursor = query.find();
        return cursor.toList();
    }

    @Override
    public List<E> findListByField(String field, Object value) {
        Query<E> query = newQuery(field, value);
        MorphiaCursor<E> cursor = query.find();
        return cursor.toList();
    }

    @Override
    public List<E> findListByField(String field, Object value, int skip, int limit) {
        FindOptions options = new FindOptions().skip(skip).limit(limit);
        Query<E> query = newQuery(field, value);
        MorphiaCursor<E> cursor = query.find(options);
        return cursor.toList();
    }

    @Override
    public List<E> findAll() {
        Query<E> query = newQuery();
        MorphiaCursor<E> cursor = query.find();
        return cursor.toList();
    }

    @Override
    public List<E> findAll(int skip, int limit) {
        FindOptions options = new FindOptions().skip(skip).limit(limit);
        Query<E> query = newQuery();
        MorphiaCursor<E> cursor = query.find(options);
        return cursor.toList();
    }

    @Override
    public void delete(I id) {
        datastore.delete(newQuery("_id", id), new DeleteOptions().copy());
    }

    @Override
    public int deleteByIds(Collection<I> ids) {
        WriteResult result = datastore.delete(newQuery().field("_id").in(ids), new DeleteOptions().copy());
        return result.getN();
    }

    @Override
    public int deleteAll() {
        WriteResult result = datastore.delete(newQuery());
        return result.getN();
    }

    protected Query<E> newQuery() {
        return datastore.createQuery(entityType);
    }

    protected Query<E> newQuery(String field, Object value) {
        return newQuery().field(field).equal(value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Class<E> getEntityType() {
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
