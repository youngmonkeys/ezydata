package com.tvd12.ezydata.mongodb.testing.repository;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.repository.EzySimpleMongoRepository;
import com.tvd12.ezydata.mongodb.testing.MongodbTest;
import com.tvd12.ezydata.mongodb.testing.bean.Chicken;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.bson.BsonDocument;
import org.testng.annotations.Test;

public class EzySimpleMongoRepositoryTest extends MongodbTest {

    @Test
    public void entityToBsonDocumentTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        InternalChickenRepo sut = new InternalChickenRepo();
        sut.setDatabaseContext(databaseContext);

        Chicken chicken = new Chicken();

        // when
        BsonDocument actual = MethodInvoker.create()
            .object(sut)
            .method("entityToBsonDocument")
            .param(Object.class, chicken)
            .call();

        // then
        Asserts.assertEquals(actual.size(), 2);
    }

    @Test
    public void getEntityTypeErrorTest() {
        // given
        // when
        Throwable e = Asserts.assertThrows(() ->
            new EzySimpleMongoRepository<>()
        );

        // then
        Asserts.assertEqualsType(e, UnimplementedOperationException.class);
    }

    public static class InternalChickenRepo
        extends EzySimpleMongoRepository<String, Chicken> {
    }
}
