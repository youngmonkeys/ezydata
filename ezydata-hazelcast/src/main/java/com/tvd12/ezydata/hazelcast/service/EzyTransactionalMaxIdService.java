/**
 * 
 */
package com.tvd12.ezydata.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.transaction.TransactionalMap;
import com.tvd12.ezydata.hazelcast.constant.EzyMapNames;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezyfox.database.service.EzyMaxIdService;

/**
 * @author tavandung12
 *
 */
public class EzyTransactionalMaxIdService
        extends EzyAbstractMapService<String, Long>
        implements EzyMaxIdService {

    public EzyTransactionalMaxIdService() {
    }

    public EzyTransactionalMaxIdService(HazelcastInstance hazelcastInstance) {
        super(hazelcastInstance);
    }

    @Override
    public void loadAll() {
        map.loadAll(false);
    }

    @Override
    public Long incrementAndGet(String key) {
        EzyMapReturnTransaction<String, Long, Long> transaction =
                newReturnTransaction();
        transaction.begin();
        try {
            Long maxId =
            transaction.apply(map -> incrementAndGetMaxId(map, key, 1));
            transaction.commit();
            return maxId;
        }
        catch(Exception e) {
            transaction.rollback();
            throw new IllegalStateException("cannot increment and get on key " + key, e);
        }
    }

    @Override
    public Long incrementAndGet(String key, int delta) {
        EzyMapReturnTransaction<String, Long, Long> transaction =
                newReturnTransaction();
        transaction.begin();
        try {
            Long maxId =
            transaction.apply(map -> incrementAndGetMaxId(map, key, delta));
            transaction.commit();
            return maxId;
        }
        catch(Exception e) {
            transaction.rollback();
            throw new IllegalStateException("cannot increment and get on key " + key, e);
        }
    }

    private Long incrementAndGetMaxId(TransactionalMap<String, Long> map, String key, int delta) {
        Long current = map.getForUpdate(key);
        Long maxId = current != null ? current + delta : delta;
        map.set(key, maxId);
        return maxId;
    }

    @Override
    protected String getMapName() {
        return EzyMapNames.MAX_ID;
    }

}
