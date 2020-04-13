package com.tvd12.ezydata.morphia.bean;

import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoriesImplementer;

public class EzyMorphiaRepositoriesImplementer
		extends EzyMongoRepositoriesImplementer {
	
	@Override
	protected EzyMorphiaRepositoryImplementer newRepoImplementer(Class<?> itf) {
		return new EzyMorphiaRepositoryImplementer(itf);
	}
	
}
