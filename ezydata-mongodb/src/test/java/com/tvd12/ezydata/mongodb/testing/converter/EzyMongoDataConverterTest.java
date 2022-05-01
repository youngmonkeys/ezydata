package com.tvd12.ezydata.mongodb.testing.converter;

import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToData;
import com.tvd12.ezydata.mongodb.converter.EzyMongoBsonValueToString;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataToBsonValue;
import org.testng.annotations.Test;

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
