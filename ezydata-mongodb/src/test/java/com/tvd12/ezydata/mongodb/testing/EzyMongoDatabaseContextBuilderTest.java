package com.tvd12.ezydata.mongodb.testing;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezyfox.naming.EzyNameTranslator;
import com.tvd12.ezyfox.naming.EzyNamingCase;
import com.tvd12.ezyfox.naming.EzySimpleNameTranslator;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

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

    @Test
    public void setPropertiesTest() {
        // given
        String databaseName = RandomUtil.randomShortAlphabetString();
        EzyNamingCase namingCase = RandomUtil.randomEnumValue(EzyNamingCase.class);
        String ignoredSuffix = RandomUtil.randomShortAlphabetString();

        // when
        EzyMongoDatabaseContextBuilder sut = new EzyMongoDatabaseContextBuilder()
            .collectionNameTranslator(
                namingCase,
                ignoredSuffix
            )
            .databaseName(databaseName);

        // then
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "databaseName"),
            databaseName
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "collectionNameTranslator"),
            EzySimpleNameTranslator.builder()
                .namingCase(namingCase)
                .ignoredSuffix(ignoredSuffix)
                .build()
        );
    }

    public static class InternalErrorEntity {}
}
