package com.tvd12.ezydata.example.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.example.jpa.constant.UserType;
import com.tvd12.ezydata.example.jpa.entity.User;
import com.tvd12.ezydata.example.jpa.repo.UserRepo;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;

public class EzyJpaExample {
	
	public static void main(String[] args) throws Exception {
		EzyDatabaseContext databaseContext = createDatabaseContext();
		UserRepo userRepo = databaseContext.getRepository(UserRepo.class);
		saveUser(userRepo);
		findUserByEmail(userRepo);
		databaseContext.close();
	}
	
	private static void saveUser(UserRepo userRepo) {
		User user = new User();
		user.setEmail("foo@world.com");
		user.setFullName("foo bar");
		user.setPassword("123456");
		user.setType(UserType.PERSONAL);
		userRepo.save(user);
		System.out.println("saved user: " + user);
	}
	
	private static void findUserByEmail(UserRepo userRepo) {
		User user = userRepo.findByEmail("foo@world.com");
		System.out.println("user by email: " + user);
	}
	
	private static EzyDatabaseContext createDatabaseContext() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("User");
		EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
				.scan("com.tvd12.ezydata.example.jpa")
				.entityManagerFactory(entityManagerFactory)
				.build();
		return databaseContext;
	}
	
}
