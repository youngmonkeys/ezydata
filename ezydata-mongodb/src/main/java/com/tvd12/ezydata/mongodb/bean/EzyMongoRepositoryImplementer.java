package com.tvd12.ezydata.mongodb.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;

public abstract class EzyMongoRepositoryImplementer 
		extends EzyAbstractRepositoryImplementer  {
	
	public EzyMongoRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected Class<?> getBaseRepositoryInterface() {
		return EzyMongoRepository.class;
	}
	
}
