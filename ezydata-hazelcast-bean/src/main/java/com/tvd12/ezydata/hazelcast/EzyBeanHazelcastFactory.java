package com.tvd12.ezydata.hazelcast;

import com.tvd12.ezydata.hazelcast.mapstore.EzyBeanMapstoreCreator;
import com.tvd12.ezydata.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextAware;

import lombok.Setter;

public class EzyBeanHazelcastFactory 
		extends EzyAbstractHazelcastFactory
		implements EzyBeanContextAware {
	
	@Setter
	protected EzyBeanContext context;

	@Override
	protected EzyMapstoreCreator newMapstoreCreator() {
		EzyBeanMapstoreCreator creator = new EzyBeanMapstoreCreator();
		creator.setContext(context);
		return creator;
	}

}
