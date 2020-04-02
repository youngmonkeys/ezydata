package com.tvd12.ezydata.jpa.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseJpaTest {

	protected static final EntityManager ENTITY_MANAGER;
	
	static {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("UsersDB");
		ENTITY_MANAGER = factory.createEntityManager();
	}
	
}
