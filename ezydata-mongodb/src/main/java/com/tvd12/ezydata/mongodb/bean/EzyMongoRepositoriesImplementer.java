package com.tvd12.ezydata.mongodb.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;

public class EzyMongoRepositoriesImplementer
		extends EzyAbstractRepositoriesImplementer {

	@Override
	protected Class<?> getBaseRepositoryInterface() {
		return EzyMongoRepository.class;
	}

	@Override
	protected EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf) {
		return new EzyMongoRepositoryImplementer(itf);
	}
	
}
