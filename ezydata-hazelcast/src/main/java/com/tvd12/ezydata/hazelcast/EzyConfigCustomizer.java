package com.tvd12.ezydata.hazelcast;

import com.hazelcast.config.Config;

public interface EzyConfigCustomizer {
    
    void customize(Config config);
    
}
