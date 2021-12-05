package com.tvd12.ezydata.mongodb.testing.converter;

import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.converter.EzyBsonObjectIdConverter;
import com.tvd12.test.assertion.Asserts;

public class EzyBsonObjectIdConverterTest {

    @Test
    public void readTestWithObjectId() {
        // given
        ObjectId value = new ObjectId();
        
        // when
        BsonObjectId actual = EzyBsonObjectIdConverter.getInstance()
                .read(null, value);
        
        // then
        Asserts.assertEquals(actual, new BsonObjectId(value));
    }
    
    @Test
    public void readTestWithBsonObjectId() {
        // given
        BsonObjectId value = new BsonObjectId(new ObjectId());
        
        // when
        BsonObjectId actual = EzyBsonObjectIdConverter.getInstance()
                .read(null, value);
        
        // then
        Asserts.assertEquals(actual, value);
    }
}
