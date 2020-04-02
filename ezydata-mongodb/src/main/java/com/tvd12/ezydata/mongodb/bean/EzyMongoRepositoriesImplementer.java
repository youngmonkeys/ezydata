package com.tvd12.ezydata.mongodb.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;

public abstract class EzyMongoRepositoriesImplementer
		extends EzyAbstractRepositoriesImplementer {

	@Override
	protected Class<?> getBaseRepositoryInterface() {
		return EzyMongoRepository.class;
	}
	
}
