package com.tvd12.ezydata.database.test;

import com.tvd12.ezydata.database.EzyDatabaseRepository;

import java.util.Collection;
import java.util.List;

public class BaseDbRepository<I, E> implements EzyDatabaseRepository<I, E> {

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void save(E entity) {
    }

    @Override
    public void save(Iterable<E> entities) {
    }

    @Override
    public E findById(I id) {
        return null;
    }

    @Override
    public List<E> findListByIds(Collection<I> ids) {
        return null;
    }

    @Override
    public E findByField(String field, Object value) {
        return null;
    }

    @Override
    public List<E> findListByField(String field, Object value) {
        return null;
    }

    @Override
    public List<E> findListByField(String field, Object value, int skip, int limit) {
        return null;
    }

    @Override
    public List<E> findAll() {
        return null;
    }

    @Override
    public List<E> findAll(int skip, int limit) {
        return null;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public void delete(I id) {

    }

    @Override
    public int deleteByIds(Collection<I> ids) {
        return 0;
    }

    @SuppressWarnings("unused")
    protected Class<E> getEntityType() {
        return null;
    }
}
