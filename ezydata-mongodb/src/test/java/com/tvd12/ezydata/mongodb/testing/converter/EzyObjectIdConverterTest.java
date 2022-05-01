package com.tvd12.ezydata.mongodb.testing.converter;

import com.tvd12.ezydata.mongodb.converter.EzyObjectIdConverter;
import com.tvd12.test.assertion.Asserts;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
import org.testng.annotations.Test;

public class EzyObjectIdConverterTest {

    @Test
    public void readTestWithObjectId() {
        // given
        ObjectId value = new ObjectId();

        // when
        ObjectId actual = EzyObjectIdConverter.getInstance()
            .read(null, value);

        // then
        Asserts.assertEquals(actual, value);
    }

    @Test
    public void readTestWithBsonObjectId() {
        // given
        BsonObjectId value = new BsonObjectId(new ObjectId());

        // when
        ObjectId actual = EzyObjectIdConverter.getInstance()
            .read(null, value);

        // then
        Asserts.assertEquals(actual, value.getValue());
    }
}
