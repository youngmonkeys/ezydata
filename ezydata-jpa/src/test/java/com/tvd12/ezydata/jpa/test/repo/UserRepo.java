package com.tvd12.ezydata.jpa.test.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.entity.User;

public class UserRepo extends EzyJpaRepository<String, User> {
	
	public Object findByEmail(String email) {
		Query query = entityManager.createQuery("select e.id, e.fullName from User e");
		Object result = query.getResultList();
		return result;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Object findListByEmail2(String email) {
		EzyQueryEntity queryEntity = databaseContext.getQuery("findListByEmail");
		Query query = entityManager.createQuery(queryEntity.getValue());
		List result = query.getResultList();
		List answer = new ArrayList<>();
		for(Object item : result)
			answer.add(databaseContext.deserializeResult(item, queryEntity.getResultType()));
		return answer;
	}
	
}
