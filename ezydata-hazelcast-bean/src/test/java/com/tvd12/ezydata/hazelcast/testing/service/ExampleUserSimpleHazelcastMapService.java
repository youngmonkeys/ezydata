package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.service.EzyBeanSimpleHazelcastMapService;
import com.tvd12.ezydata.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

@EzySingleton
public class ExampleUserSimpleHazelcastMapService
    extends EzyBeanSimpleHazelcastMapService<String, ExampleUser> {

    @Override
    protected String getMapName() {
        return "hazelcast-bean-simple-hazelcast-user";
    }
}
