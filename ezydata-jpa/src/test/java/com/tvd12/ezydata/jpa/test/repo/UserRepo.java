package com.tvd12.ezydata.jpa.test.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.repository.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class UserRepo extends EzyJpaRepository<String, User> {

    public UserIdFullNameResult findByEmail(String email) {
        EntityManager entityManager = databaseContext.createEntityManager();
        Query query = entityManager.createQuery("select e.id, e.fullName from User e where e.email = ?0");
        query.setParameter(0, "dzung@gmail.com");
        try {
            Object result = query.getSingleResult();
            Object answer = databaseContext.deserializeResult(result, UserIdFullNameResult.class);
            return (UserIdFullNameResult)answer;
        }
        catch(Exception e) {
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
        List result = query.getResultList();
        return databaseContext.deserializeResultList(result, queryEntity.getResultType());
    }

    public int countAll(String email) {
        EntityManager entityManager = databaseContext.createEntityManager();
        Query query = entityManager.createQuery("select count(e) from User e");
        return (int)(long)query.getSingleResult();
    }
}
