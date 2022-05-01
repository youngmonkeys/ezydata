package com.tvd12.ezydata.mongodb.converter;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.io.EzyDates;
import org.bson.*;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class EzyMongoDataToBsonValue {

    protected final Map<Class, Function<Object, BsonValue>> converters;

    public EzyMongoDataToBsonValue() {
        this.converters = defaultConverters();
    }

    public void addConverter(
        Class<?> dataType, Function<Object, BsonValue> converter) {
        this.converters.put(dataType, converter);
    }

    public BsonValue convert(Object data) {
        if (data == null) {
            return BsonNull.VALUE;
        }
        if (data instanceof ObjectId) {
            return new BsonObjectId((ObjectId) data);
        }
        if (data instanceof BsonValue) {
            return (BsonValue) data;
        }
        Class<?> valueType = data.getClass();
        Function<Object, BsonValue> converter = converters.get(valueType);
        if (converter != null) {
            return converter.apply(data);
        }
        if (data instanceof Iterable) {
            BsonArray array = new BsonArray();
            for (Object item : (Iterable) data) {
                array.add(convert(item));
            }
            return array;
        }
        if (data instanceof Map) {
            Map map = (Map) data;
            BsonDocument document = new BsonDocument();
            for (Object k : map.keySet()) {
                putKeyValue(document, k, map.get(k));
            }
            return document;
        }
        if (data instanceof EzyArray) {
            EzyArray coll = (EzyArray) data;
            BsonArray array = new BsonArray();
            for (int i = 0; i < coll.size(); ++i) {
                array.add(convert(coll.get(i)));
            }
            return array;
        }
        if (data instanceof EzyObject) {
            EzyObject obj = (EzyObject) data;
            BsonDocument document = new BsonDocument();
            for (Object k : obj.keySet()) {
                putKeyValue(document, k, obj.get(k));
            }
            return document;
        }
        if (data instanceof Object[]) {
            BsonArray array = new BsonArray();
            for (Object item : (Object[]) data) {
                array.add(convert(item));
            }
            return array;
        }
        if (valueType.isEnum()) {
            return new BsonString(data.toString());
        }
        throw new IllegalArgumentException(
            "has no converter for: " + valueType.getName()
        );
    }

    protected void putKeyValue(
        BsonDocument document, Object key, Object value) {
        String keyString;
        BsonValue ck = convert(key);
        if (ck instanceof BsonDocument) {
            keyString = ((BsonDocument) ck).toJson();
        } else if (ck instanceof BsonString) {
            keyString = ((BsonString) ck).getValue();
        } else {
            keyString = ck.toString();
        }
        BsonValue cv = convert(value);
        document.put(keyString, cv);
    }

    protected Map<Class, Function<Object, BsonValue>> defaultConverters() {
        Map<Class, Function<Object, BsonValue>> map = new HashMap<>();
        map.put(Boolean.class, v -> new BsonBoolean((Boolean) v));
        map.put(Byte.class, v -> new BsonInt32((Byte) v));
        map.put(Character.class, v -> new BsonInt32((Character) v));
        map.put(Double.class, v -> new BsonDouble((Double) v));
        map.put(Float.class, v -> new BsonDouble((Float) v));
        map.put(Integer.class, v -> new BsonInt32((Integer) v));
        map.put(Long.class, v -> new BsonInt64((Long) v));
        map.put(Short.class, v -> new BsonInt32((Short) v));
        map.put(String.class, v -> new BsonString((String) v));
        map.put(Class.class, v -> new BsonString(((Class) v).getName()));
        map.put(UUID.class, v -> new BsonString(((UUID) v).toString()));
        map.put(BigDecimal.class, v -> new BsonDecimal128(new Decimal128((BigDecimal) v)));
        map.put(BigInteger.class, v -> new BsonDecimal128(new Decimal128(new BigDecimal((BigInteger) v))));
        map.put(Date.class, v -> new BsonDateTime(((Date) v).getTime()));
        map.put(LocalDate.class, v -> new BsonString(EzyDates.format((LocalDate) v, "yyyy-MM-dd")));
        map.put(LocalDateTime.class, v -> new BsonString(EzyDates.format((LocalDateTime) v)));

        map.put(boolean[].class, v -> {
            BsonArray array = new BsonArray();
            for (boolean value : (boolean[]) v) {
                array.add(new BsonBoolean(value));
            }
            return array;

        });
        map.put(byte[].class, v -> new BsonBinary((byte[]) v));
        map.put(char[].class, v -> new BsonString(new String((char[]) v)));
        map.put(double[].class, v -> {
            BsonArray array = new BsonArray();
            for (double value : (double[]) v) {
                array.add(new BsonDouble(value));
            }
            return array;

        });
        map.put(float[].class, v -> {
            BsonArray array = new BsonArray();
            for (float value : (float[]) v) {
                array.add(new BsonDouble(value));
            }
            return array;

        });
        map.put(int[].class, v -> {
            BsonArray array = new BsonArray();
            for (int value : (int[]) v) {
                array.add(new BsonInt32(value));
            }
            return array;

        });
        map.put(long[].class, v -> {
            BsonArray array = new BsonArray();
            for (long value : (long[]) v) {
                array.add(new BsonInt64(value));
            }
            return array;

        });
        map.put(short[].class, v -> {
            BsonArray array = new BsonArray();
            for (short value : (short[]) v) {
                array.add(new BsonInt32(value));
            }
            return array;
        });
        return map;
    }
}
