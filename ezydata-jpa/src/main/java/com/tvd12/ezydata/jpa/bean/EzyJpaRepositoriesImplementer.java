package com.tvd12.ezydata.jpa.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;

public class EzyJpaRepositoriesImplementer
		extends EzyAbstractRepositoriesImplementer {
	
	@Override
	protected EzyJpaRepositoryImplementer newRepoImplementer(Class<?> itf) {
		return new EzyJpaRepositoryImplementer(itf);
	}
	
}
