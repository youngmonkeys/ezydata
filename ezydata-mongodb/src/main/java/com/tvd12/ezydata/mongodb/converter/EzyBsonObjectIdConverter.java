package com.tvd12.ezydata.mongodb.converter;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyTemplate;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

public final class EzyBsonObjectIdConverter implements EzyTemplate<Object, BsonObjectId> {

    private static final EzyBsonObjectIdConverter INSTANCE = new EzyBsonObjectIdConverter();

    private EzyBsonObjectIdConverter() {}

    public static EzyBsonObjectIdConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public BsonObjectId read(EzyUnmarshaller unmarshaller, Object value) {
        if (value instanceof String) {
            return new BsonObjectId(new ObjectId((String) value));
        }
        if (value instanceof ObjectId) {
            return new BsonObjectId((ObjectId) value);
        }
        return (BsonObjectId) value;
    }

    @Override
    public Object write(EzyMarshaller marshaller, BsonObjectId object) {
        return object;
    }
}
