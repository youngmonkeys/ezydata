package com.tvd12.ezydata.mongodb.testing.repository;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoryImplementer;
import com.tvd12.ezydata.mongodb.repository.EzySimpleMongoRepository;
import com.tvd12.ezydata.mongodb.testing.MongodbTest;
import com.tvd12.ezydata.mongodb.testing.bean.Chicken;
import com.tvd12.ezydata.mongodb.testing.bean.Duck;
import com.tvd12.ezydata.mongodb.testing.bean.DuckId;
import com.tvd12.ezydata.mongodb.testing.bean.DuckRepo;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.util.Next;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.bson.BsonDocument;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

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
        Throwable e = Asserts.assertThrows(
            EzySimpleMongoRepository::new
        );

        // then
        Asserts.assertEqualsType(e, UnimplementedOperationException.class);
    }

    @Test
    public void findOneWithQueryWithFieldTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckRepo duckRepo = databaseContext.getRepository(DuckRepo.class);
        DuckId duckId1 = new DuckId(1, "foo");
        Duck duck1 = new Duck();
        duck1.setId(duckId1);
        duck1.setAge(10);
        duck1.setDescription("test");
        DuckId duckId2 = new DuckId(2, "bar");
        Duck duck2 = new Duck();
        duck2.setId(duckId2);
        duck2.setAge(10);
        duck2.setDescription("hello");
        duckRepo.save(duck1, duck2);

        // when
        Duck actual = duckRepo.findOneDuckSomeFields(10);

        // then
        Asserts.assertEquals(
            actual,
            new Duck(duck1.getId(), duck1.getAge(), null)
        );
    }

    @Test
    public void findListWithQueryWithFieldTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckRepo duckRepo = databaseContext.getRepository(DuckRepo.class);
        DuckId duckId1 = new DuckId(1, "foo");
        Duck duck1 = new Duck();
        duck1.setId(duckId1);
        duck1.setAge(10);
        duck1.setDescription("test");
        DuckId duckId2 = new DuckId(2, "bar");
        Duck duck2 = new Duck();
        duck2.setId(duckId2);
        duck2.setAge(10);
        duck2.setDescription("hello");
        duckRepo.save(duck1, duck2);

        // when
        List<Duck> actual = duckRepo.findListDuckSomeFields(
            10,
            Next.limit(100)
        );

        // then
        Asserts.assertEquals(
            actual,
            Arrays.asList(
                new Duck(duck1.getId(), duck1.getAge(), null),
                new Duck(duck2.getId(), duck2.getAge(), null)
            ),
            false
        );
    }

    public static class InternalChickenRepo
        extends EzySimpleMongoRepository<String, Chicken> {
    }
}
