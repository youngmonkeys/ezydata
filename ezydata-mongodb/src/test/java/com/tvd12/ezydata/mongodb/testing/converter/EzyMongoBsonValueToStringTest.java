package com.tvd12.ezydata.mongodb.testing.converter;

import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToString;
import org.bson.*;
import org.testng.annotations.Test;

public class EzyMongoBsonValueToStringTest {

    @Test
    public void test() {
        EzyMongoBsonValueToString conveter = new EzyMongoBsonValueToString();
        assert conveter.convert(BsonNull.VALUE) == null;
        assert conveter.convert(new BsonBoolean(true)).equals("true");
        assert conveter.convert(new BsonDouble(12.3)).equals("12.3");
        assert conveter.convert(new BsonInt64(100)).equals("100");
        BsonArray array = new BsonArray();
        array.add(new BsonInt64(100));
        array.add(new BsonString("hello"));
        assert conveter.convert(array).equals("[100,'hello']");
        System.out.println(conveter.convert(new BsonDateTime(System.currentTimeMillis())));
        System.out.println(conveter.convert(new BsonUndefined()));
    }

}
