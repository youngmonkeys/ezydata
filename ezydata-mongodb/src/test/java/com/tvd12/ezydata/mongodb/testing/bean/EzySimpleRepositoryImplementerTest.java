package com.tvd12.ezydata.mongodb.testing.bean;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoryImplementer;
import com.tvd12.ezydata.mongodb.testing.MongodbTest;

public class EzySimpleRepositoryImplementerTest extends MongodbTest {

	@Test
	public void test() {
		EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.scan("com.tvd12.ezydata.mongodb.testing.bean")
				.build();
		
		EzyMongoRepositoryImplementer.setDebug(true);
		EzyMongoRepositoryImplementer implementer = new EzyMongoRepositoryImplementer(PersonRepo.class);
		PersonRepo repo = (PersonRepo) implementer.implement(databaseContext);
		Person person = new Person();
		person.setId(1);
		person.setName("dzung");
		repo.save(person);
	}
	
	
}
