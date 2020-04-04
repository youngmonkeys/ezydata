package com.tvd12.ezydata.jpa.test.repo;

import java.util.List;

import javax.persistence.Query;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class UserRepo extends EzyJpaRepository<String, User> {
	
	public UserIdFullNameResult findByEmail(String email) {
		Query query = entityManager.createQuery("select e.id, e.fullName from User e where e.email = ?0");
		query.setParameter(0, "dzung@gmail.com");
		Object result = query.getSingleResult();
		Object answer = databaseContext.deserializeResult(result, UserIdFullNameResult.class);
		return (UserIdFullNameResult)answer;
	}
	
	@SuppressWarnings({"rawtypes"})
	public Object findListByEmail2(String email) {
		EzyQueryEntity queryEntity = databaseContext.getQuery("findListByEmail");
		Query query = entityManager.createQuery(queryEntity.getValue());
		List result = query.getResultList();
		return databaseContext.deserializeResultList(result, queryEntity.getResultType());
	}
	
}
