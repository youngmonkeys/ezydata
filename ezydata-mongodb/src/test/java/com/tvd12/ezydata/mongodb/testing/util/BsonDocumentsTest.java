package com.tvd12.ezydata.mongodb.testing.util;

import org.bson.BsonDocument;
import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.util.BsonDocuments;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class BsonDocumentsTest extends BaseTest {

    @Override
    public Class<?> getTestClass() {
        return BsonDocuments.class;
    }
    
    @Test
    public void putIfNotNullButNull() {
        // given
        BsonDocument document = new BsonDocument();
        String key = "key";
        
        // when
        BsonDocuments.putIfNotNull(document, key, null);
        
        // then
        Asserts.assertTrue(document.isEmpty());
    }
}
