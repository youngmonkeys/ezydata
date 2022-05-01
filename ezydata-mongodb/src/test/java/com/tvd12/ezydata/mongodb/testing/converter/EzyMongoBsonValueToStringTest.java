package com.tvd12.ezydata.mongodb.testing.converter;

import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToString;
import org.bson.*;
import org.testng.annotations.Test;

public class EzyMongoBsonValueToStringTest {

    @Test
    public void test() {
        EzyMongoBsonValueToString converter = new EzyMongoBsonValueToString();
        assert converter.convert(BsonNull.VALUE) == null;
        assert converter.convert(new BsonBoolean(true)).equals("true");
        assert converter.convert(new BsonDouble(12.3)).equals("12.3");
        assert converter.convert(new BsonInt64(100)).equals("100");
        BsonArray array = new BsonArray();
        array.add(new BsonInt64(100));
        array.add(new BsonString("hello"));
        assert converter.convert(array).equals("[100,'hello']");
        System.out.println(converter.convert(new BsonDateTime(System.currentTimeMillis())));
        System.out.println(converter.convert(new BsonUndefined()));
    }
}
