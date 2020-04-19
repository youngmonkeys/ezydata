package com.tvd12.ezydata.morphia.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.morphia.EzyDatastoreAware;
import com.tvd12.ezydata.morphia.repository.EzyDatastoreRepository;

import dev.morphia.Datastore;

public class EzyMorphiaRepositoryImplementer extends EzyAbstractRepositoryImplementer {

	public EzyMorphiaRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected void setRepoComponent(Object repo, Object template) {
		if(template instanceof Datastore 
				&& repo instanceof EzyDatastoreAware) {
			((EzyDatastoreAware)repo).setDatastore((Datastore)template);
		}
	}
	
	@Override
	protected Class<?> getSuperClass() {
		return EzyDatastoreRepository.class;
	}
	
	@Override
	protected Class<?> getBaseRepositoryInterface() {
		return EzyMongoRepository.class;
	}
	
}
