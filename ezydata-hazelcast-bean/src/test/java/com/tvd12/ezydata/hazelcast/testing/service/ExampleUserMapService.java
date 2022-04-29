package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.service.EzyBeanAbstractMapService;
import com.tvd12.ezydata.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

@EzySingleton
public class ExampleUserMapService extends EzyBeanAbstractMapService<String, ExampleUser> {

    @Override
    protected String getMapName() {
        return "hazelcast-bean-user-map";
    }

    
    
}
