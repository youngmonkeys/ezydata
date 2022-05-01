package com.tvd12.ezydata.mongodb.testing.repository;

import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoryImplementer;
import com.tvd12.ezydata.mongodb.testing.MongodbTest;
import com.tvd12.ezydata.mongodb.testing.bean.Duck;
import com.tvd12.ezydata.mongodb.testing.bean.DuckId;
import com.tvd12.ezydata.mongodb.testing.bean.DuckImplementationRepo;
import com.tvd12.ezydata.mongodb.testing.result.DuckResult;
import com.tvd12.ezydata.mongodb.testing.result.DuckResult2;
import com.tvd12.ezyfox.util.Next;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImplementationRepositoryTest extends MongodbTest {

    @Test
    public void findOneWithQueryTest() {
        // given
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        duckRepo.save(duck1, duck2);

        // when
        Duck actual = duckRepo.findOneWithQuery(
            EzyQLQuery.builder()
                .query("{age: ?0}")
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(actual, duck1);
        duckRepo.deleteAll();
    }

    @Test
    public void findListWithQueryTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        Duck duck3 = new Duck();
        duck3.setId(new DuckId(3, "hello"));
        duck3.setAge(11);
        duck3.setDescription("hello");
        duckRepo.save(duck1, duck2, duck3);

        // when
        List<Duck> actual = duckRepo.findListWithQuery(
            EzyQLQuery.builder()
                .query("{age: ?0}")
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(
            actual,
            Arrays.asList(duck1, duck2),
            false
        );
        duckRepo.deleteAll();
    }

    @Test
    public void findListWithQueryWithNextTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        Duck duck3 = new Duck();
        duck3.setId(new DuckId(3, "hello"));
        duck3.setAge(11);
        duck3.setDescription("hello");
        duckRepo.save(duck1, duck2, duck3);

        // when
        List<Duck> actual = duckRepo.findListWithQuery(
            EzyQLQuery.builder()
                .query("{age: ?0}")
                .parameter(0, 10)
                .build(),
            Next.limit(1)
        );

        // then
        Asserts.assertEquals(
            actual,
            Collections.singletonList(duck1),
            false
        );
        duckRepo.deleteAll();
    }

    @Test
    public void countWithQueryTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        Duck duck3 = new Duck();
        duck3.setId(new DuckId(3, "hello"));
        duck3.setAge(11);
        duck3.setDescription("hello");
        duckRepo.save(duck1, duck2, duck3);

        // when
        long actual = duckRepo.countWithQuery(
            EzyQLQuery.builder()
                .query("{age: ?0}")
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(
            actual,
            2L
        );
        duckRepo.deleteAll();
    }

    @Test
    public void countWithQueryWithNextTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        Duck duck3 = new Duck();
        duck3.setId(new DuckId(3, "hello"));
        duck3.setAge(11);
        duck3.setDescription("hello");
        duckRepo.save(duck1, duck2, duck3);

        // when
        long actual = duckRepo.countWithQuery(
            EzyQLQuery.builder()
                .query("{age: ?0}")
                .parameter(0, 10)
                .build(),
            Next.limit(1)
        );

        // then
        Asserts.assertEquals(
            actual,
            1L
        );
        duckRepo.deleteAll();
    }

    @Test
    public void aggregateOneWithQueryTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .scan("com.tvd12.ezydata.mongodb.testing.result")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        duckRepo.save(duck1, duck2);

        // when
        DuckResult actual = duckRepo.aggregateOneWithQuery(
            EzyQLQuery.builder()
                .query(
                    "[" +
                    "{ $sort: { id: 1 }}," +
                    "{ $match: { age: ?0 }}" +
                    "]"
                )
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(
            actual,
            new DuckResult(
                duck1.getId(),
                duck1.getDescription()
            )
        );
        duckRepo.deleteAll();
    }

    @Test
    public void aggregateOneWithQuery2Test() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .scan("com.tvd12.ezydata.mongodb.testing.result")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        duckRepo.save(duck1, duck2);

        // when
        DuckResult2 actual = duckRepo.aggregateOneWithQuery2(
            EzyQLQuery.builder()
                .query(
                    "[" +
                        "{ $sort: { id: 1 }}," +
                        "{ $match: { age: ?0 }}" +
                        "]"
                )
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(
            actual,
            new DuckResult2(
                duck1.getId(),
                duck1.getAge(),
                duck1.getDescription()
            )
        );
        duckRepo.deleteAll();
    }

    @Test
    public void aggregateListWithQueryTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .scan("com.tvd12.ezydata.mongodb.testing.result")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        duckRepo.save(duck1, duck2);

        // when
        List<DuckResult> actual = duckRepo.aggregateListWithQuery(
            EzyQLQuery.builder()
                .query(
                    "[" +
                        "{ $sort: { id: 1 }}," +
                        "{ $match: { age: ?0 }}" +
                        "]"
                )
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(
            actual,
            Arrays.asList(
                new DuckResult(
                    duck1.getId(),
                    duck1.getDescription()
                ),
                new DuckResult(
                    duck2.getId(),
                    duck2.getDescription()
                )
            ),
            false
        );
        duckRepo.deleteAll();
    }

    @Test
    public void aggregateListWithQuery2Test() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .scan("com.tvd12.ezydata.mongodb.testing.result")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        Duck duck3 = new Duck();
        duck3.setId(new DuckId(3, "hello"));
        duck3.setAge(11);
        duck3.setDescription("hello");
        duckRepo.save(duck1, duck2, duck3);

        // when
        List<DuckResult2> actual = duckRepo.aggregateListWithQuery2(
            EzyQLQuery.builder()
                .query(
                    "[" +
                        "{ $sort: { id: 1 }}," +
                        "{ $match: { age: ?0 }}" +
                        "]"
                )
                .parameter(0, 10)
                .build()
        );

        // then
        Asserts.assertEquals(
            actual,
            Arrays.asList(
                new DuckResult2(
                    duck1.getId(),
                    duck1.getAge(),
                    duck1.getDescription()
                ),
                new DuckResult2(
                    duck2.getId(),
                    duck2.getAge(),
                    duck2.getDescription()
                )
            ),
            false
        );
        duckRepo.deleteAll();
    }

    @Test
    public void updateWithQueryTest() {
        // given
        EzyMongoRepositoryImplementer.setDebug(true);
        EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .databaseName(databaseName)
            .scan("com.tvd12.ezydata.mongodb.testing.bean")
            .scan("com.tvd12.ezydata.mongodb.testing.result")
            .build();

        DuckImplementationRepo duckRepo = databaseContext
            .getRepository(DuckImplementationRepo.class);
        duckRepo.deleteAll();
        Duck duck1 = new Duck();
        duck1.setId(new DuckId(1, "foo"));
        duck1.setAge(10);
        duck1.setDescription("test");
        Duck duck2 = new Duck();
        duck2.setId(new DuckId(2, "bar"));
        duck2.setAge(10);
        duck2.setDescription("hello");
        Duck duck3 = new Duck();
        duck3.setId(new DuckId(3, "hello"));
        duck3.setAge(11);
        duck3.setDescription("hello");
        duckRepo.save(duck1, duck2, duck3);

        // when
        int actual = duckRepo.updateWithQuery(
            EzyQLQuery.builder()
                .query("{$query: {age : ?0}, $update: {$set: {description: ?1}}}")
                .parameter(0, 10)
                .parameter(1, "'updated'")
                .build()
        );

        // then
        Asserts.assertEquals(actual, 2);
        Asserts.assertEquals(
            duckRepo.countWithQuery(
                EzyQLQuery.builder()
                    .query("{description: 'updated'}")
                    .build()
            ),
            2L
        );
        duckRepo.deleteAll();
    }
}
