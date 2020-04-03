package com.tvd12.ezydata.jpa.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class AppTest {
	
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("UsersDB");
//		EntityManager entityManager = factory.createEntityManager();
//		entityManager.getTransaction().begin();
//		User newUser = new User();
//		newUser.setEmail("billjoy@gmail.com");
//		newUser.setFullName("bill Joy");
//		newUser.setPassword("billi");
//		 
//		entityManager.persist(newUser);
//		entityManager.getTransaction().commit();
		EzyQueryEntity query1 = EzyQueryEntity.builder()
				.name("findListByEmail")
				.value("select e.id, e.fullName from User e")
				.resultType(UserIdFullNameResult.class)
				.build();
		EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
				.addQuery(query1)
				.entityManagerFactory(factory)
				.build();
		UserRepo userRepo = new UserRepo();
		userRepo.setDatabaseContext(databaseContext);
		System.out.println(userRepo.count());
		System.out.println(userRepo.findByEmail("dzung@gmail.com"));
		System.out.println(userRepo.findByField("email", "dzung@gmail.com"));
		System.out.println("findListByEmail2: " + userRepo.findListByEmail2("dzung@gmail.com"));
//		entityManager.close();
//		factory.close();
		databaseContext.close();
	}
	
}
