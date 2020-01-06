package com.tvd12.ezydata.morphia.impl;

import com.tvd12.ezydata.mongodb.bean.EzySimpleRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.bean.EzySimpleRepositoryImplementer;

public class EzyMorphiaRepositoriesImplementer
		extends EzySimpleRepositoriesImplementer {
	
	@Override
	protected EzySimpleRepositoryImplementer newRepoImplementer(Class<?> itf) {
		return new EzyMorphiaRepositoryImplementer(itf);
	}
	
}
