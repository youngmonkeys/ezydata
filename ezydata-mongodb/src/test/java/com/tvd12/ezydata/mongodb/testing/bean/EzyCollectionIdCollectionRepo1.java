package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@EzyRepository
public interface EzyCollectionIdCollectionRepo1 
        extends EzyMongoRepository<EzyCollectionIdCompositeId1, EzyCollectionIdCollection1> {
}
