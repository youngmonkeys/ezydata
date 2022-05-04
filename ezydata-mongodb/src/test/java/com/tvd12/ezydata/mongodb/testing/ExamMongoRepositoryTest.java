package com.tvd12.ezydata.mongodb.testing;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.testing.bean.Exam;
import com.tvd12.ezydata.mongodb.testing.bean.ExamRepository;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;
import org.testng.annotations.Test;

import java.util.Collections;

public class ExamMongoRepositoryTest extends MongodbTest {

    @Test
    public void test() {
        // given
        EzyMongoDatabaseContext ctx = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .scan("com.tvd12.ezydata.mongodb.testing")
            .propertiesFile("mongodb_config.properties")
            .build();
        ExamRepository repo = ctx.getRepository(ExamRepository.class);

        ObjectId examId = new ObjectId();
        ObjectId eventId = new ObjectId();
        BsonObjectId secondId = new BsonObjectId();
        String content = RandomUtil.randomShortAlphabetString();

        Exam exam = new Exam();
        exam.setId(examId);
        exam.setEventId(eventId);
        exam.setContent(content);
        exam.setRefIds(Lists.newArrayList(new ObjectId(), new ObjectId()));
        exam.setSecondId(secondId);

        // when
        repo.save(exam);

        // then
        Exam fromDb = repo.findById(examId);
        System.out.println(fromDb);
        Asserts.assertEquals(exam, fromDb);
        Asserts.assertEquals(exam, repo.findByField("eventId", eventId));
        Asserts.assertEquals(
            Lists.newArrayList(exam),
            repo.findListByIds(Collections.singletonList(examId))
        );
        Asserts.assertEquals(exam, repo.findByField("secondId", secondId));
    }

    @Test
    public void containsByIdTest() {
        // given
        EzyMongoDatabaseContext ctx = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .scan("com.tvd12.ezydata.mongodb.testing")
            .propertiesFile("mongodb_config.properties")
            .build();
        ExamRepository repo = ctx.getRepository(ExamRepository.class);

        ObjectId examId = new ObjectId();
        ObjectId eventId = new ObjectId();
        BsonObjectId secondId = new BsonObjectId();
        String content = RandomUtil.randomShortAlphabetString();

        Exam exam = new Exam();
        exam.setId(examId);
        exam.setEventId(eventId);
        exam.setContent(content);
        exam.setRefIds(Lists.newArrayList(new ObjectId(), new ObjectId()));
        exam.setSecondId(secondId);
        repo.save(exam);

        // when
        boolean actual1 = repo.containsById(examId);
        boolean actual2 = repo.containsById(new ObjectId());

        // then
        Asserts.assertTrue(actual1);
        Asserts.assertFalse(actual2);
    }

    @Test
    public void containsByFieldTest() {
        // given
        EzyMongoDatabaseContext ctx = new EzyMongoDatabaseContextBuilder()
            .mongoClient(mongoClient)
            .scan("com.tvd12.ezydata.mongodb.testing")
            .propertiesFile("mongodb_config.properties")
            .build();
        ExamRepository repo = ctx.getRepository(ExamRepository.class);

        ObjectId examId = new ObjectId();
        ObjectId eventId = new ObjectId();
        BsonObjectId secondId = new BsonObjectId();
        String content = RandomUtil.randomShortAlphabetString();

        Exam exam = new Exam();
        exam.setId(examId);
        exam.setEventId(eventId);
        exam.setContent(content);
        exam.setRefIds(Lists.newArrayList(new ObjectId(), new ObjectId()));
        exam.setSecondId(secondId);
        repo.save(exam);

        // when
        boolean actual1 = repo.containsByField("eventId", eventId);
        boolean actual2 = repo.containsByField("eventId", "I don't know");

        // then
        Asserts.assertTrue(actual1);
        Asserts.assertFalse(actual2);
    }
}
