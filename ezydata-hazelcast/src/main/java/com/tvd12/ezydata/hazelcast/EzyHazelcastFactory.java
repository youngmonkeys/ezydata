package com.tvd12.ezydata.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;

public interface EzyHazelcastFactory {

    HazelcastInstance newHazelcast(Config config);
}
