package com.tvd12.ezydata.jpa;

import javax.persistence.EntityManager;

public interface EzyEntityManagerAware {

	void setEntityManager(EntityManager entityManager);
	
}
