package com.tvd12.ezydata.jpa.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tvd12.ezydata.jpa.test.repo.UserRepo;

public class AppTest {
	
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("UsersDB");
		EntityManager entityManager = factory.createEntityManager();
//		entityManager.getTransaction().begin();
//		User newUser = new User();
//		newUser.setEmail("billjoy@gmail.com");
//		newUser.setFullName("bill Joy");
//		newUser.setPassword("billi");
//		 
//		entityManager.persist(newUser);
//		entityManager.getTransaction().commit();
		UserRepo userRepo = new UserRepo();
		userRepo.setEntityManager(entityManager);
		System.out.println(userRepo.count());
		entityManager.close();
		factory.close();
	}
	
}
