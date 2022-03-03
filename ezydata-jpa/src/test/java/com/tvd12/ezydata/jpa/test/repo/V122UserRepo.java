package com.tvd12.ezydata.jpa.test.repo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.tvd12.ezydata.jpa.repository.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.result.UserEmailFullNameResult;
import com.tvd12.ezyfox.util.EzyMapBuilder;

public class V122UserRepo extends EzyJpaRepository<String, User> {

    public List<User> findByEmail(String email) {
        String queryString = "SELECT e from User e WHERE e.email = :email";
        Map<String, Object> parameters = EzyMapBuilder.mapBuilder()
            .put("email", email)
            .put("name", null)
            .toMap();
        return findListByQueryString(queryString, parameters, 0, 100);
    }
    
    public List<UserEmailFullNameResult> fetchUserEmailByParamMap(String email) {
        String queryString = "SELECT e.email, e.fullName from User e WHERE e.email = :email";
        Map<String, Object> parameters = EzyMapBuilder.mapBuilder()
            .put("email", email)
            .put("name", null)
            .toMap();
        return fetchListByQueryString(
            queryString,
            parameters,
            UserEmailFullNameResult.class,
            0,
            100
        );
    }
    
    public List<UserEmailFullNameResult> fetchUserEmailByParamList(String email) {
        String queryString = "SELECT e.email, e.fullName from User e WHERE e.email = ?0";
        return fetchListByQueryString(
            queryString,
            new Object[] {email},
            UserEmailFullNameResult.class,
            0,
            100
        );
    }
    
    public long countByEmail(String email) {
        String queryString = "SELECT count(e) from User e WHERE e.email = :email";
        Map<String, Object> parameters = Collections.singletonMap("email", email);
        return countByQueryString(queryString, parameters);
    }
    
    public long countByEmailWithArrayParam(String email) {
        String queryString = "SELECT count(e) from User e WHERE e.email = ?0";
        Object[] parameters = new Object[] { email };
        return countByQueryString(queryString, parameters);
    }
}
