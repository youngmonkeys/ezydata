package com.tvd12.ezydata.hazelcast.mapstore;

import com.hazelcast.map.MapStore;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextAware;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.database.annotation.EzyMapstore;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.tvd12.ezyfox.database.util.EzyMapstoreAnnotations.getMapName;

@SuppressWarnings("rawtypes")
public class EzyBeanMapstoreCreator
    extends EzyLoggable
    implements EzyMapstoreCreator, EzyBeanContextAware {

    protected Map<String, MapStore> mapstores = new ConcurrentHashMap<>();

    @Override
    public Set<String> getMapNames() {
        return new HashSet<>(mapstores.keySet());
    }

    @Override
    public MapStore create(String mapName, Properties properties) {
        return mapstores.get(mapName);
    }

    @SuppressWarnings("unchecked")
    public void setContext(EzyBeanContext context) {
        EzySingletonFactory singletonFactory = context.getSingletonFactory();
        List<Object> objects = singletonFactory.getSingletons(EzyMapstore.class);
        for (Object object : objects) {
            mapstores.put(getMapName(object.getClass()), (MapStore) object);
        }
    }
}
