package com.tvd12.ezydata.morphia.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;

public class EzyMorphiaRepositoriesImplementer
        extends EzyAbstractRepositoriesImplementer {

    @Override
    protected EzyMorphiaRepositoryImplementer newRepoImplement(Class<?> itf) {
        return new EzyMorphiaRepositoryImplementer(itf);
    }

    @Override
    protected Class<?> getBaseRepositoryInterface() {
        return EzyMongoRepository.class;
    }

}
