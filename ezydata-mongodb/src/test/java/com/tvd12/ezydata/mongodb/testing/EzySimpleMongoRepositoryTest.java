package com.tvd12.ezydata.mongodb.testing;

import org.bson.BsonDocument;
import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.repository.EzySimpleMongoRepository;
import com.tvd12.ezydata.mongodb.testing.bean.Duck;
import com.tvd12.ezydata.mongodb.testing.bean.DuckId;
import com.tvd12.properties.file.reader.BaseFileReader;

public class EzySimpleMongoRepositoryTest extends MongodbTest {

	@Test
	public void test() {
		EzyMongoDatabaseContext ctx = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.properties(new BaseFileReader().read("mongodb_config.properties"))
				.build();
		RepoImpl repo = new RepoImpl();
		repo.setDatabaseContext(ctx);
		assert repo.bsonDocumentToEntity(null) == null;
	}
	
	
	public static class RepoImpl extends EzySimpleMongoRepository<DuckId, Duck> {
		
		@Override
		public Duck bsonDocumentToEntity(BsonDocument document) {
			return super.bsonDocumentToEntity(document);
		}
	}
}
