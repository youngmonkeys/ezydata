package com.tvd12.ezydata.mongodb.testing.converter;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToData;
import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToString;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataToBsonValue;

public class EzyMongoDataConverterTest {

    @Test
    public void test() {
        EzyMongoDataConverter.builder()
                .dataToBsonValue(new EzyMongoDataToBsonValue())
                .bsonValueToData(new EzyMongoBsonValueToData())
                .bsonValueToString(new EzyMongoBsonValueToString())
                .build();
    }

}
