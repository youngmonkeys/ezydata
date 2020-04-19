package com.tvd12.ezydata.mongodb.testing;

import java.io.InputStream;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.mongodb.loader.EzyInputStreamMongoClientLoader;
import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.test.base.BaseTest;

public class MongodbTest extends BaseTest {

	protected static String databaseName;
	protected static MongoClient mongoClient;
	
	static {
		databaseName = "test";
		InputStream inputStream = EzyAnywayInputStreamLoader.builder()
				.build()
				.load("mongodb_config.properties");
		EzyInputStreamMongoClientLoader loader = new EzyInputStreamMongoClientLoader()
				.inputStream(inputStream)
				.property(EzyMongoClientLoader.HOST, "127.0.0.1")
				.property(EzyMongoClientLoader.PORT, "27017")
				.properties(EzyMapBuilder.mapBuilder()
						.put(EzyMongoClientLoader.USERNAME, "root")
						.put(EzyMongoClientLoader.PASSWORD, "123456")
						.put(EzyMongoClientLoader.DATABASE, databaseName)
						.build());
		mongoClient = loader.load();
	}
	
	
}
