package com.tvd12.ezydata.jpa;

import javax.persistence.EntityManagerFactory;

import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoriesImplementer;

public class EzyJpaDatabaseContextBuilder 
		extends EzyDatabaseContextBuilder<EzyJpaDatabaseContextBuilder> {

	protected EntityManagerFactory entityManagerFactory;
	
	public EzyJpaDatabaseContextBuilder 
			entityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		return this;
	}
	
	@Override
	protected EzySimpleDatabaseContext newDatabaseContext() {
		EzySimpleJpaDatabaseContext context = new EzySimpleJpaDatabaseContext();
		context.setEntityManagerFactory(entityManagerFactory);
		return context;
	}
	
	@Override
	protected EzyAbstractRepositoriesImplementer newRepositoriesImplementer() {
		return new EzyJpaRepositoriesImplementer();
	}
	
}
