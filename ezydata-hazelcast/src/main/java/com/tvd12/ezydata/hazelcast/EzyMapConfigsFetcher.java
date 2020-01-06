package com.tvd12.ezydata.hazelcast;

import com.hazelcast.config.MapConfig;

public interface EzyMapConfigsFetcher {

	Iterable<MapConfig> get();
	
}
