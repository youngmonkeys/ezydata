package com.tvd12.ezydata.mongodb.testing.converter;

import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToData;
import org.bson.*;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class EzyMongoBsonValueToDataTest {

    @Test
    public void test() {
        long current = System.currentTimeMillis();
        EzyMongoBsonValueToData converter = new EzyMongoBsonValueToData();
        converter.addConverter(BsonType.END_OF_DOCUMENT, v -> "end of document");
        assert converter.convert(new BsonBoolean(true)).equals(true);
        assertEquals(converter.convert(new BsonBinary(new byte[]{1, 2, 3})), new byte[]{1, 2, 3});
        assert converter.convert(new BsonDouble(12.0D)).equals(12.0D);
        assert converter.convert(new BsonInt64(100)).equals(100L);
        assert converter.convert(BsonNull.VALUE) == null;
        assert converter.convert(new BsonDateTime(current)).equals(current);
        assert converter.convert(new BsonTimestamp(current)).equals(current);
        assert converter.convert(new BsonDecimal128(new Decimal128(123L))).equals("123");
        assert converter.convert(new BsonRegularExpression("[0-9]")).equals("[0-9]");
        BsonArray array = new BsonArray();
        array.add(new BsonString("hello"));
        array.add(new BsonInt64(123));
        array.add(new BsonString("world"));
        System.out.println(converter.convert(array));
        try {
            converter.convert(new BsonDbPointer("", new ObjectId()));
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }
}
