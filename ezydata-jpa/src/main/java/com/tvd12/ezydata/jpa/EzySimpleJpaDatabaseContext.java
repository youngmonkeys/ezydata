package com.tvd12.ezydata.jpa;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.tvd12.ezydata.database.EzySimpleDatabaseContext;

import lombok.Setter;

public class EzySimpleJpaDatabaseContext 
		extends EzySimpleDatabaseContext
		implements EzyJpaDatabaseContext {

	protected Set<EntityManager> entityManagers;
	@Setter
	protected EntityManagerFactory entityManagerFactory;
	
	public EzySimpleJpaDatabaseContext() {
		this.entityManagers = Collections.synchronizedSet(new HashSet<>());
	}
	
	@Override
	public EntityManager getEntityManager() {
		EntityManager em = entityManagerFactory.createEntityManager();
		entityManagers.add(em);
		return em;
	}
	
	@Override
	public void close() {
		for(EntityManager em : entityManagers)
			em.close();
		entityManagerFactory.close();
	}
	
}
