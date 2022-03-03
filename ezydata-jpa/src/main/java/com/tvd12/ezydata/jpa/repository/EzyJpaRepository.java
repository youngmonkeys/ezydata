package com.tvd12.ezydata.jpa.repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContext;
import com.tvd12.ezydata.jpa.reflect.EzyJpaIdProxy;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyJpaRepository<I,E> 
		extends EzyLoggable
		implements EzyDatabaseRepository<I,E>, EzyDatabaseContextAware {

	protected final Class<E> entityType;
	protected final EzyClass entityClass;
	protected final EzyJpaIdProxy idProxy;
	protected EzyJpaDatabaseContext databaseContext;
	
	protected final static Object[] NO_PARAMETERS = new Object[0];
	
	public EzyJpaRepository() {
		this.entityType = getEntityType();
		this.entityClass = new EzyClass(entityType);
		this.idProxy = new EzyJpaIdProxy(entityClass);
	}
	
	@Override
	public void setDatabaseContext(EzyDatabaseContext context) {
		this.databaseContext = (EzyJpaDatabaseContext)context;
	}
	
	@Override
	public void save(E entity) {
	    EntityManager entityManager = databaseContext.createEntityManager();
		try {
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
		finally {
		    entityManager.close();
        }
	}

	@Override
	public void save(Iterable<E> entities) {
	    EntityManager entityManager = databaseContext.createEntityManager();
	    try {
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
	    finally {
            entityManager.close();
        }
	}

	@Override
	public E findById(I id) {
		return findByField("id", id);
	}

	@Override
	public E findByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		EntityManager entityManager = databaseContext.createEntityManager();
		List resultList;
		try {
    		Query query = entityManager.createQuery(queryString);
    		query.setParameter(0, value);
    		query.setMaxResults(1);
    		resultList = query.getResultList();
		}
		finally {
            entityManager.close();
        }
		Object entity = resultList.isEmpty() ? null : resultList.get(0);
		return (E)entity;
	}
	
	protected E findByQueryString(String queryString, Object[] parameters) {
	    EntityManager entityManager = databaseContext.createEntityManager();
	    List resultList;
	    try {
	        Query query = createQuery(entityManager, queryString, parameters);
    		query.setMaxResults(1);
    		resultList = query.getResultList();
	    }
	    finally {
            entityManager.close();
        }
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
	    EntityManager entityManager = databaseContext.createEntityManager();
	    try {
	        Query query = createQuery(entityManager, queryString, parameters);
    		return query.getResultList();
	    }
	    finally {
            entityManager.close();
        }
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
	    EntityManager entityManager = databaseContext.createEntityManager();
	    try {
	        Query query = createQuery(entityManager, queryString, parameters);
    		query.setFirstResult(skip);
    		query.setMaxResults(limit);
    		return query.getResultList();
	    }
	    finally {
            entityManager.close();
        }
	}
	
    protected List<E> findListByQueryString(
        String queryString,
        Map<String, Object> parameters,
        int skip,
        int limit
    ) {
        EntityManager entityManager = databaseContext.createEntityManager();
        try {
            Query query = createQuery(entityManager, queryString, parameters);
            query.setFirstResult(skip);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
    
    protected <R> List<R> fetchListByQueryString(
        String queryString, 
        Object[] parameters,
        Class<R> resultType,
        int skip,
        int limit
    ) {
        EntityManager entityManager = databaseContext.createEntityManager();
        try {
            Query query = createQuery(entityManager, queryString, parameters);
            query.setFirstResult(skip);
            query.setMaxResults(limit);
            return databaseContext.deserializeResultList(
                query.getResultList(),
                resultType
            );
        }
        finally {
            entityManager.close();
        }
    }
    
    protected <R> List<R> fetchListByQueryString(
        String queryString,
        Map<String, Object> parameters,
        Class<R> resultType,
        int skip,
        int limit
    ) {
        EntityManager entityManager = databaseContext.createEntityManager();
        try {
            Query query = createQuery(entityManager, queryString, parameters);
            query.setFirstResult(skip);
            query.setMaxResults(limit);
            return databaseContext.deserializeResultList(
                query.getResultList(),
                resultType
            );
        } finally {
            entityManager.close();
        }
    }

	@Override
	public List<E> findAll() {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		EntityManager entityManager = databaseContext.createEntityManager();
		try {
		    Query query = entityManager.createQuery(queryString);
		    return query.getResultList();
		}
		finally {
            entityManager.close();
        }
	}

	@Override
	public List<E> findAll(int skip, int limit) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		EntityManager entityManager = databaseContext.createEntityManager();
		try {
    		Query query = entityManager.createQuery(queryString);
    		query.setFirstResult(skip);
    		query.setMaxResults(limit);
    		return query.getResultList();
		}
		finally {
            entityManager.close();
        }
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
	    EntityManager entityManager = databaseContext.createEntityManager();
		try {
		    Query query = createQuery(entityManager, queryString, parameters);
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
		finally {
            entityManager.close();
        }
	}
	
	@Override
    public long count() {
        String queryString = new StringBuilder()
                .append("select count(e.id) from ")
                .append(entityType.getName()).append(" e ")
                .toString();
        EntityManager entityManager = databaseContext.createEntityManager();
        try {
            Query query = entityManager.createQuery(queryString);
            return (long)query.getSingleResult();
        }
        finally {
            entityManager.close();
        }
    }
	
	   protected long countByQueryString(String queryString, Object[] parameters) {
	        EntityManager entityManager = databaseContext.createEntityManager();
	        try {
	            Query query = createQuery(entityManager, queryString, parameters);
	            return (long) query.getSingleResult();
	        }
	        finally {
	            entityManager.close();
	        }
	    }
	
	protected long countByQueryString(String queryString, Map<String, Object> parameters) {
        EntityManager entityManager = databaseContext.createEntityManager();
        try {
            Query query = createQuery(entityManager, queryString, parameters);
            return (long) query.getSingleResult();
        }  finally {
            entityManager.close();
        }
    }
	
	protected Query createQuery(
        EntityManager entityManager,
        String queryString,
        Object[] parameters
    ) {
	    Query query = entityManager.createQuery(queryString);
        for(int i = 0 ; i < parameters.length ; ++i)
            query.setParameter(i, parameters[i]);
        return query;
    }

    protected Query createQuery(
        EntityManager entityManager,
        String queryString,
        Map<String, Object> parameters
    ) {
        Query query = entityManager.createQuery(queryString);
        for (String paramName : parameters.keySet()) {
            Object paramValue = parameters.get(paramName);
            if (paramValue != null) {
                query.setParameter(paramName, paramValue);
            }
        }
        return query;
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
