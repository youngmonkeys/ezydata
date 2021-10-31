package com.tvd12.ezydata.jpa;

import javax.persistence.EntityManager;

import com.tvd12.ezydata.database.EzyDatabaseContext;

public interface EzyJpaDatabaseContext extends EzyDatabaseContext {
	
	EntityManager createEntityManager();
	
}
