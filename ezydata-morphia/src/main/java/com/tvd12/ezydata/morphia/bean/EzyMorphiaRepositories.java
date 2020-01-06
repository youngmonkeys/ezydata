package com.tvd12.ezydata.morphia.bean;

import com.tvd12.ezydata.morphia.impl.EzyMorphiaRepositoriesImplementer;

public final class EzyMorphiaRepositories {

	private EzyMorphiaRepositories() {
	}
	
	public static EzyMorphiaRepositoriesImplementer newRepositoriesImplementer() {
		return new EzyMorphiaRepositoriesImplementer();
	}
	
}
