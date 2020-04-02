package com.tvd12.ezydata.jpa.bean;

import javax.persistence.EntityManager;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.jpa.EzyEntityManagerAware;
import com.tvd12.ezydata.jpa.EzyJpaRepository;

public class EzyJpaRepositoryImplementer extends EzyAbstractRepositoryImplementer {

	public EzyJpaRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected void setRepoComponent(Object repo, Object template) {
		EntityManager entityManager = (EntityManager)template;
		EzyEntityManagerAware entityManagerAware = (EzyEntityManagerAware)repo; 
		entityManagerAware.setEntityManager(entityManager);
	}
	
	@Override
	protected Class<?> getSuperClass() {
		return EzyJpaRepository.class;
	}
	
}
