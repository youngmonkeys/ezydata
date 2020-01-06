package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezydata.hazelcast.service.EzyHazelcastMapService;

@EzyMapServiceAutoImpl("monkey")
public interface MonkeyMapService extends EzyHazelcastMapService<String, Monkey> {
}
