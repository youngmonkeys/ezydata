package com.tvd12.ezydata.mongodb.testing.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.tvd12.ezydata.mongodb.repository.EzyMongoMaxIdRepository;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.bson.BsonDocument;
import org.bson.Document;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

@Test
public class EzyMongoMaxIdRepositoryTest {

    public void incrementAndGetZeroDueToDocumentResultIsNull() {
        // given
        //noinspection unchecked
        MongoCollection<Document> collection = mock(MongoCollection.class);
        EzyMongoMaxIdRepository sut = new EzyMongoMaxIdRepository(
            collection
        );

        // when
        String key = RandomUtil.randomShortAlphabetString();
        int delta = RandomUtil.randomSmallInt();
        long actual = sut.incrementAndGet(key, delta);

        // then
        Asserts.assertZero(actual);
        verify(collection, times(1)).findOneAndUpdate(
            any(BsonDocument.class),
            any(BsonDocument.class),
            any(FindOneAndUpdateOptions.class)
        );
    }

    public void incrementAndGetZeroDueToDocumentValueIsNull() {
        // given
        //noinspection unchecked
        MongoCollection<Document> collection = mock(MongoCollection.class);
        when(
            collection.findOneAndUpdate(
                any(BsonDocument.class),
                any(BsonDocument.class),
                any(FindOneAndUpdateOptions.class)
            )
        ).thenReturn(Document.parse("{}"));
        EzyMongoMaxIdRepository sut = new EzyMongoMaxIdRepository(
            collection
        );

        // when
        String key = RandomUtil.randomShortAlphabetString();
        int delta = RandomUtil.randomSmallInt();
        long actual = sut.incrementAndGet(key, delta);

        // then
        Asserts.assertZero(actual);
        verify(collection, times(1)).findOneAndUpdate(
            any(BsonDocument.class),
            any(BsonDocument.class),
            any(FindOneAndUpdateOptions.class)
        );
    }
}
