package com.tvd12.ezydata.mongodb.testing.util;

import com.tvd12.ezydata.mongodb.util.BsonDocuments;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.util.RandomUtil;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.testng.annotations.Test;

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

    @Test
    public void doesNotDecorateIdValue() {
        // given
        BsonDocument document = new BsonDocument();
        String id = RandomUtil.randomShortAlphabetString();
        String idUnderscore = RandomUtil.randomShortAlphabetString();
        document.put("id", new BsonString(id));
        document.put("_id", new BsonString(idUnderscore));

        // when
        BsonDocuments.decorateIdValue(document);

        // then
        Asserts.assertEquals(document.getString("id").getValue(), id);
    }

    @Test
    public void decorateIdValueButIdIsNull() {
        // given
        BsonDocument document = new BsonDocument();

        // when
        BsonDocuments.decorateIdValue(document);

        // then
        Asserts.assertNull(document.get("id", null));
    }

    @Test
    public void decorateIdValueFailed() {
        // given
        BsonDocument document = new BsonDocument() {
            @Override
            public BsonValue put(String key, BsonValue value) {
                if (key.equals("_id")) {
                    return super.put(key, value);
                } else {
                    throw new IllegalStateException("just test");
                }
            }
        };
        String idUnderscore = RandomUtil.randomShortAlphabetString();
        document.put("_id", new BsonString(idUnderscore));

        // when
        BsonDocuments.decorateIdValue(document);

        // then
        Asserts.assertNull(document.get("id", null));
    }
}
