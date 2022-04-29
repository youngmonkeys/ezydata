package com.tvd12.ezydata.morphia.testing.repo1;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.morphia.testing.Pig;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
abstract class PigRepo3 implements EzyMongoRepository<Long, Pig> {

}
