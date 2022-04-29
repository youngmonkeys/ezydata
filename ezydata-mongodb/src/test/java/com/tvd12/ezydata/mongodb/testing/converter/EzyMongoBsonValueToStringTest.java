package com.tvd12.ezydata.mongodb.testing.converter;

import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDouble;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonUndefined;
import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToString;

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
