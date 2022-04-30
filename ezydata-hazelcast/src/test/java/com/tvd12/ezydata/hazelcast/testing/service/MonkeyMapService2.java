package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezydata.hazelcast.service.EzyHazelcastMapService;

@EzyMapServiceAutoImpl("monkey")
public abstract class MonkeyMapService2
    implements EzyHazelcastMapService<String, Monkey> {}
