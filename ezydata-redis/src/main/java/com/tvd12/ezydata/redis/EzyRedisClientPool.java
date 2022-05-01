package com.tvd12.ezydata.redis;

import com.tvd12.ezyfox.util.EzyCloseable;

public interface EzyRedisClientPool extends EzyCloseable {

    EzyRedisClient getClient();
}
