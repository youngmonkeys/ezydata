/**
 * 
 */
package com.tvd12.ezydata.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.tvd12.ezydata.hazelcast.constant.EzyMapNames;
import com.tvd12.ezydata.hazelcast.map.EzyMaxIdEntryProcessor;
import com.tvd12.ezydata.hazelcast.map.EzyMaxIdOneEntryProcessor;
import com.tvd12.ezyfox.database.service.EzyMaxIdService;


/**
 * @author tavandung12
 *
 */
public class EzyEntryProcessorMaxIdService
        extends EzyAbstractMapService<String, Long>
        implements EzyMaxIdService {
    
    protected final EntryProcessor<String, Long, Long> onEntryProcessor;
    
    public EzyEntryProcessorMaxIdService() {
        this.onEntryProcessor = newOneEntryProcessor();
    }

    public EzyEntryProcessorMaxIdService(HazelcastInstance hazelcastInstance) {
        super(hazelcastInstance);
        this.onEntryProcessor = newOneEntryProcessor();
    }
    
    @Override
    public void loadAll() {
        map.loadAll(false);
    }

    @Override
    public Long incrementAndGet(String key) {
        return (Long)map.executeOnKey(key, onEntryProcessor);
    }
    
    @Override
    public Long incrementAndGet(String key, int delta) {
        return (Long)map.executeOnKey(key, newEntryProcessor(delta));
    }

    @Override
    protected String getMapName() {
        return EzyMapNames.MAX_ID;
    }
    
    protected EntryProcessor<String, Long, Long> newOneEntryProcessor() {
        return new EzyMaxIdOneEntryProcessor();
    }
    
    protected EntryProcessor<String, Long, Long> newEntryProcessor(int delta) {
        return new EzyMaxIdEntryProcessor(delta);
    }
    
}
