package com.tvd12.ezydata.jpa.repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import com.tvd12.ezydata.jpa.reflect.EzyJpaIdProxy;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class EzyJpaRepository<I,E> 
		extends EzyLoggable
		implements EzyDatabaseRepository<I,E>, EzyDatabaseContextAware, EzyEntityManagerAware {

	protected final Class<E> entityType;
	protected final EzyClass entityClass;
	protected final EzyJpaIdProxy idProxy;
	protected EntityManager entityManager;
	protected EzyJpaDatabaseContext databaseContext;
	
	protected final static Object[] NO_PARAMETERS = new Object[0];
	
	public EzyJpaRepository() {
		this.entityType = getEntityType();
		this.entityClass = new EzyClass(entityType);
		this.idProxy = new EzyJpaIdProxy(entityClass);
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
			E result = entityManager.merge(entity);
			transaction.commit();
			idProxy.setId(result, entity);
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
			List<E> results = new ArrayList<>();
			for(E entity : entities) {
				E result = entityManager.merge(entity);
				results.add(result);
			}
			transaction.commit();
			int i = 0;
			for(E entity : entities) {
				idProxy.setId(results.get(i ++), entity);
			}
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
	public E findByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		List resultList = query.getResultList();
		Object entity = resultList.isEmpty() ? null : resultList.get(0);
		return (E)entity;
	}
	
	protected E findByQueryString(String queryString, Object[] parameters) {
		Query query = entityManager.createQuery(queryString);
		for(int i = 0 ; i < parameters.length ; ++i)
			query.setParameter(i, parameters[i]);
		List resultList = query.getResultList();
		Object entity = resultList.isEmpty() ? null : resultList.get(0);
		return (E)entity;
	}
	
	@Override
	public List<E> findListByIds(Collection<I> ids) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id in ?0")
				.toString();
		return findListByQueryString(queryString, new Object[] {ids});
	}

	@Override
	public List<E> findListByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		return findListByQueryString(queryString, new Object[] {value});
	}
	
	protected List<E> findListByQueryString(String queryString, Object[] parameters) {
		Query query = entityManager.createQuery(queryString);
		for(int i = 0 ; i < parameters.length ; ++i)
			query.setParameter(i, parameters[i]);
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
		return findListByQueryString(queryString, new Object[] {value}, skip, limit);
	}
	
	protected List<E> findListByQueryString(
			String queryString, 
			Object[] parameters, int skip, int limit) {
		Query query = entityManager.createQuery(queryString);
		for(int i = 0 ; i < parameters.length ; ++i)
			query.setParameter(i, parameters[i]);
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
		return deleteByQueryString(queryString, NO_PARAMETERS);
	}

	@Override
	public void delete(I id) {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id = ?0")
				.toString();
		deleteByQueryString(queryString, new Object[] {id});
	}

	@Override
	public int deleteByIds(Collection<I> ids) {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id in ?0")
				.toString();
		return deleteByQueryString(queryString, new Object[] {ids});
	}
	
	protected int deleteByQueryString(String queryString, Object[] parameters) {
		Query query = entityManager.createQuery(queryString);
		for(int i = 0 ; i < parameters.length ; ++i)
			query.setParameter(i, parameters[i]);
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
