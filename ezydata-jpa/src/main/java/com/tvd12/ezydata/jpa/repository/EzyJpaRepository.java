package com.tvd12.ezydata.jpa.repository;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.EzyEntityManagerAware;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContext;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings("unchecked")
public abstract class EzyJpaRepository<I,E> 
		extends EzyLoggable
		implements EzyDatabaseRepository<I,E>, EzyDatabaseContextAware, EzyEntityManagerAware {

	protected final Class<E> entityType;
	protected EntityManager entityManager;
	protected EzyJpaDatabaseContext databaseContext;
	
	public EzyJpaRepository() {
		this.entityType = getEntityType();
	}
	
	@Override
	public void setEntityManager(EntityManager entityManager) {
		if(this.entityManager == null)
			this.entityManager = entityManager;
		else if(this.entityManager != entityManager)
			throw new IllegalStateException("set entityManager twice");
	}
	
	@Override
	public void setDatabaseContext(EzyDatabaseContext context) {
		this.databaseContext = (EzyJpaDatabaseContext)context;
		this.setEntityManager(databaseContext.getEntityManager());
	}
	
	@Override
	public long count() {
		String queryString = new StringBuilder()
				.append("select count(e.id) from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		long count = (long)query.getSingleResult();
		return count;
	}

	@Override
	public void save(E entity) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			entityManager.merge(entity);
			transaction.commit();
		}
		catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void save(Iterable<E> entities) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			for(E entity : entities)
				entityManager.merge(entity);
			transaction.commit();
		}
		catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public E findById(I id) {
		E entity = entityManager.find(entityType, id);
		return entity;
	}

	@Override
	public List<E> findListByIds(Collection<I> ids) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id in ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, ids);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public E findByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		Object entity = query.getSingleResult();
		return (E)entity;
	}

	@Override
	public List<E> findListByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public List<E> findListByField(String field, Object value, int skip, int limit) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		query.setFirstResult(skip);
		query.setMaxResults(limit);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public List<E> findAll() {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public List<E> findAll(int skip, int limit) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setFirstResult(skip);
		query.setMaxResults(limit);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public int deleteAll() {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			int deletedRows = query.executeUpdate();
			return deletedRows;
		}
		catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public void delete(I id) {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, id);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			query.executeUpdate();
			transaction.commit();
		}
		catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}

	@Override
	public int deleteByIds(Collection<I> ids) {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id in ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, ids);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			int deletedRows = query.executeUpdate();
			transaction.commit();
			return deletedRows;
		}
		catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
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
