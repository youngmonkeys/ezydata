package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@EzyRepository
public interface EzyIdCollectionRepo2
    extends EzyMongoRepository<EzyIdCompositeId2, EzyIdCollection2> {}
