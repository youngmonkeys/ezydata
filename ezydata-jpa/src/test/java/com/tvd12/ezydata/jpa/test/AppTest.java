package com.tvd12.ezydata.jpa.test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class AppTest extends BaseJpaTest {
	
	public static void main(String[] args) {
		EzyQueryEntity query1 = EzyQueryEntity.builder()
				.name("findListByEmail")
				.value("select e.id, e.fullName from User e")
				.resultType(UserIdFullNameResult.class)
				.build();
		EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
				.addQuery(query1)
				.repositoryClass(UserRepo.class)
				.scan("com.tvd12.ezydata.jpa.test.repo")
				.entityManagerFactory(ENTITY_MANAGER_FACTORY)
				.build();
		UserRepo userRepo = databaseContext.getRepository(UserRepo.class);
		System.out.println(userRepo.count());
		System.out.println(userRepo.findByEmail("dzung@gmail.com"));
		System.out.println(userRepo.findByField("email", "dzung@gmail.com"));
		System.out.println("findListByEmail2: " + userRepo.findListByEmail2("dzung@gmail.com"));
		databaseContext.close();
	}
	
}
