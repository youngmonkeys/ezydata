package com.tvd12.ezydata.mongodb.testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezyfox.naming.EzyNameTranslator;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyMongoDatabaseContextBuilderTest {

    @Test
    public void getCollectionIdFieldOfTest() {
        // given
        EzyMongoDatabaseContextBuilder sut = new EzyMongoDatabaseContextBuilder();
        
        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(sut)
                .method("getCollectionIdFieldOf")
                .param(InternalErrorEntity.class)
                .call()
        );
        
        // then
        Asserts.assertEqualsType(e.getCause().getCause(), IllegalArgumentException.class);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void addMaxIdRepositoryTest() {
        // given
        String maxIdCollectionName = "test";
        EzyMongoDatabaseContextBuilder sut = new EzyMongoDatabaseContextBuilder()
                .maxIdCollectionName(maxIdCollectionName);
        
        MongoDatabase mongoDatabase = mock(MongoDatabase.class);
        when(mongoDatabase.getCollection(maxIdCollectionName)).thenThrow(Exception.class);
        
        // when
        MethodInvoker.create()
            .object(sut)
            .method("addMaxIdRepository")
            .param(MongoDatabase.class, mongoDatabase)
            .call();
        
        // then
        verify(mongoDatabase, times(1)).getCollection(maxIdCollectionName);
    }
    
    @Test
    public void getOrCreateCollectionNameTranslatorNotNull() {
        // given
        EzyNameTranslator collectionNameTranslator = mock(EzyNameTranslator.class);
        EzyMongoDatabaseContextBuilder sut = new EzyMongoDatabaseContextBuilder()
                .collectionNameTranslator(collectionNameTranslator);
        
        // when
        EzyNameTranslator actual = MethodInvoker.create()
            .object(sut)
            .method("getOrCreateCollectionNameTranslator")
            .call();
        
        // then
        Asserts.assertEquals(actual, collectionNameTranslator);
    }
    
    public static class InternalErrorEntity {}
}
