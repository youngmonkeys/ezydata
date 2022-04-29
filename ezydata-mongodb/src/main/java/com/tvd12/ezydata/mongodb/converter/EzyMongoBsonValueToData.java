package com.tvd12.ezydata.mongodb.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bson.BsonArray;
import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDecimal128;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonObjectId;
import org.bson.BsonRegularExpression;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.bson.BsonType;
import org.bson.BsonValue;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;

public class EzyMongoBsonValueToData  {

    protected final Map<BsonType, Function<BsonValue, Object>> converters;

    public EzyMongoBsonValueToData() {
        this.converters = defaultConverters();
    }

    public void addConverter(
            BsonType valueType, Function<BsonValue, Object> converter) {
        this.converters.put(valueType, converter);
    }

    public Object convert(BsonValue value) {
        if(value == null)
            return null;
        BsonType type = value.getBsonType();
        Function<BsonValue, Object> converter = converters.get(type);
        if(converter != null)
            return converter.apply(value);
        throw new IllegalArgumentException("has no converter for bson type: " + type);
    }

    protected Map<BsonType, Function<BsonValue, Object>> defaultConverters() {
        Map<BsonType, Function<BsonValue, Object>> map = new HashMap<>();
        map.put(BsonType.BOOLEAN, v -> ((BsonBoolean)v).getValue());
        map.put(BsonType.BINARY, v -> ((BsonBinary)v).getData());
        map.put(BsonType.DOUBLE, v -> ((BsonDouble)v).getValue());
        map.put(BsonType.INT32, v -> ((BsonInt32)v).getValue());
        map.put(BsonType.INT64, v -> ((BsonInt64)v).getValue());
        map.put(BsonType.STRING, v -> ((BsonString)v).getValue());
        map.put(BsonType.NULL, v -> null);
        map.put(BsonType.OBJECT_ID, v -> ((BsonObjectId)v).getValue().toString());
        map.put(BsonType.DOCUMENT, v -> {
            EzyObject answer = EzyEntityFactory.newObject();
            BsonDocument document = (BsonDocument)v;
            for(String key : document.keySet()) {
                BsonValue value = document.get(key);
                answer.put(key, convert(value));
            }
            return answer;
        });
        map.put(BsonType.ARRAY, v -> {
            EzyArray answer = EzyEntityFactory.newArray();
            BsonArray array = (BsonArray)v;
            for(BsonValue value : array)
                answer.add(convert(value));
            return answer;
        });
        map.put(BsonType.DATE_TIME, v -> ((BsonDateTime)v).getValue());
        map.put(BsonType.TIMESTAMP, v -> ((BsonTimestamp)v).getValue());
        map.put(BsonType.DECIMAL128, v -> ((BsonDecimal128)v).getValue().toString());
        map.put(BsonType.REGULAR_EXPRESSION, v -> ((BsonRegularExpression)v).getPattern());
        return map;
    }

}
