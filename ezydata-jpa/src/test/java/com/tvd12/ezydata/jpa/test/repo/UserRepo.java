package com.tvd12.ezydata.jpa.test.repo;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.repository.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class UserRepo extends EzyJpaRepository<String, User> {

    public UserIdFullNameResult findByEmail(String email) {
        EntityManager entityManager = databaseContext.createEntityManager();
        Query query = entityManager.createQuery("select e.id, e.fullName from User e where e.email = ?0");
        query.setParameter(0, email);
        try {
            Object result = query.getSingleResult();
            Object answer = databaseContext.deserializeResult(result, UserIdFullNameResult.class);
            return (UserIdFullNameResult) answer;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings({"rawtypes"})
    public Object findListByEmail2(String email) {
        EzyQueryEntity queryEntity = databaseContext.getQuery("findListByEmail");
        EntityManager entityManager = databaseContext.createEntityManager();
        Query query = entityManager.createQuery(queryEntity.getValue());
        query.setFirstResult(0);
        query.setMaxResults(100);
        query.setParameter(0, email);
        List result = query.getResultList();
        return databaseContext.deserializeResultList(result, queryEntity.getResultType());
    }

    public int countAll() {
        EntityManager entityManager = databaseContext.createEntityManager();
        Query query = entityManager.createQuery("select count(e) from User e");
        return (int) (long) query.getSingleResult();
    }

    @Override
    public List<User> findListByQueryString(
        String queryString,
        Map<String, Object> parameters,
        int skip,
        int limit
    ) {
        return super.findListByQueryString(queryString, parameters, skip, limit);
    }

    public List<UserIdFullNameResult> fetchUserListByQueryString(
        String queryString,
        Object[] parameters,
        int skip,
        int limit
    ) {
        return super.fetchListByQueryString(
            queryString,
            parameters,
            UserIdFullNameResult.class,
            skip,
            limit
        );
    }

    public List<UserIdFullNameResult> fetchListByQueryString(
        String queryString,
        Map<String, Object> parameters,
        int skip,
        int limit
    ) {
        return super.fetchListByQueryString(
            queryString,
            parameters,
            UserIdFullNameResult.class,
            skip,
            limit
        );
    }

    public User findByQueryString(String queryString, Object[] parameters) {
        return super.findByQueryString(
            queryString,
            parameters
        );
    }
}
