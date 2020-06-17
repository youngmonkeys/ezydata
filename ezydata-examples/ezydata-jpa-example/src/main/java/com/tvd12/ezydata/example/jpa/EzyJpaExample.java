package com.tvd12.ezydata.example.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.example.jpa.repo.UserRepo;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;

public class EzyJpaExample {
	
	public static void main(String[] args) throws Exception {
		EzyDatabaseContext databaseContext = createDatabaseContext();
		UserRepo userRepo = databaseContext.getRepository(UserRepo.class);
		databaseContext.close();
	}
	
	private static EzyDatabaseContext createDatabaseContext() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("UsersDB");
		EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
				.repositoryClass(UserRepo.class)
				.scan("com.tvd12.ezydata.example.jpa")
				.entityManagerFactory(entityManagerFactory)
				.build();
		return databaseContext;
	}
	
}
