package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;

import java.util.Collection;
import java.util.List;

public class ExEzyDatabaseRepository
    implements EzyDatabaseRepository<Integer, Person>, EzyDatabaseContextAware {

    @Override
    public void setDatabaseContext(EzyDatabaseContext context) {
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void save(Person entity) {
    }

    @Override
    public void save(Iterable<Person> entities) {
    }

    @Override
    public Person findById(Integer id) {
        return null;
    }

    @Override
    public List<Person> findListByIds(Collection<Integer> ids) {
        return null;
    }

    @Override
    public Person findByField(String field, Object value) {
        return null;
    }

    @Override
    public List<Person> findListByField(String field, Object value) {
        return null;
    }

    @Override
    public List<Person> findListByField(String field, Object value, int skip, int limit) {
        return null;
    }

    @Override
    public List<Person> findAll() {
        return null;
    }

    @Override
    public List<Person> findAll(int skip, int limit) {
        return null;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public void delete(Integer id) {
    }

    @Override
    public int deleteByIds(Collection<Integer> ids) {
        return 0;
    }

    protected Class<Person> getEntityType() {
        return Person.class;
    }
}
