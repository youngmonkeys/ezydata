package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezydata.hazelcast.service.EzyHazelcastMapService;

@EzyMapServiceAutoImpl("monkey")
interface MonkeyMapService1
    extends EzyHazelcastMapService<String, Monkey> {}
