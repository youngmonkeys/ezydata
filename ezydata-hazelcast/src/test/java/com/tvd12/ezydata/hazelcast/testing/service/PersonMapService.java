package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.service.EzyHazelcastMapService;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.annotation.EzyKeyValue;

@EzyAutoImpl(properties = {
		@EzyKeyValue(key = "map.name", value = "person")
})
public interface PersonMapService extends EzyHazelcastMapService<String, Person> {
}
