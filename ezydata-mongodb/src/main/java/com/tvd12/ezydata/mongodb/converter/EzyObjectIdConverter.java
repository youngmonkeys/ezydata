package com.tvd12.ezydata.mongodb.converter;

import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyTemplate;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;

public final class EzyObjectIdConverter implements EzyTemplate<Object, ObjectId> {

    private static final EzyObjectIdConverter INSTANCE = new EzyObjectIdConverter();

    private EzyObjectIdConverter() {}

    public static EzyObjectIdConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public ObjectId read(EzyUnmarshaller unmarshaller, Object value) {
        if(value instanceof String)
            return new ObjectId((String)value);
        if(value instanceof BsonObjectId)
            return ((BsonObjectId)value).getValue();
        return (ObjectId)value;
    }

    @Override
    public Object write(EzyMarshaller marshaller, ObjectId object) {
        return new BsonObjectId(object);
    }
}
