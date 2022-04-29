package com.tvd12.ezydata.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzyPostInit;

public abstract class EzyBeanAbstractHazelcastService extends EzyAbstractHazelcastService {

    @EzyAutoBind
    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        super.setHazelcastInstance(hazelcastInstance);
    }

    @EzyPostInit
    @Override
    public void init() {
        super.init();
    }

}
