package com.tvd12.ezydata.database.converter;

import com.tvd12.ezydata.database.reflect.EzyDatabaseTypes;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class EzySimpleResultDeserializers
    extends EzyLoggable
    implements EzyResultDeserializers {

    protected final Map<Class<?>, EzyResultDeserializer> deserializers;

    public EzySimpleResultDeserializers() {
        this.deserializers = defaultDeserializers();
    }

    @Override
    public Object deserialize(Object data, Class<?> resultType) {
        EzyResultDeserializer deserializer = deserializers.get(resultType);
        if (deserializer == null) {
            throw new IllegalArgumentException("has no deserializer with type: " + resultType.getName());
        }
        return deserializer.deserialize(data, this);
    }

    @Override
    public EzyResultDeserializer getDeserializer(Class<?> resultType) {
        return deserializers.get(resultType);
    }

    @Override
    public Map<Class<?>, EzyResultDeserializer> getDeserializers() {
        return new HashMap<>(deserializers);
    }

    public void addDeserializer(Class<?> resultType, EzyResultDeserializer deserializer) {
        if (!deserializers.containsKey(resultType)) {
            deserializers.put(resultType, deserializer);
        }
    }

    protected Map<Class<?>, EzyResultDeserializer> defaultDeserializers() {
        Map<Class<?>, EzyResultDeserializer> map = new HashMap<>();
        for (Class<?> type : EzyDatabaseTypes.DEFAULT_TYPES) {
            map.put(type, EzyResultDeserializer.DEFAULT);
        }
        return map;
    }
}
