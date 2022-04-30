/**
 *
 */
package com.tvd12.ezydata.hazelcast.factory;

import com.hazelcast.map.MapLoader;
import com.hazelcast.map.MapStoreFactory;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyPostInit;

import java.util.Properties;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractMapstoreFactory
    extends EzyLoggable
    implements MapStoreFactory {

    @Override
    public final MapLoader newMapStore(String mapName, Properties properties) {
        Object map = newMapstore(mapName, properties);
        if (map instanceof EzyPostInit) {
            ((EzyPostInit) map).postInit();
        }
        return (MapLoader) map;
    }

    protected abstract Object newMapstore(String mapName, Properties properties);
}
