package com.tvd12.ezydata.mongodb.testing.bean;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoryImplementer;
import com.tvd12.ezydata.mongodb.testing.MongodbTest;

public class EzySimpleRepositoryImplementerTest extends MongodbTest {

	@Test
	public void test() {
		EzyMongoRepositoryImplementer.setDebug(true);
		EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.scan("com.tvd12.ezydata.mongodb.testing.bean")
				.build();
		PersonRepo repo = databaseContext.getRepository(PersonRepo.class);
		Person person = new Person();
		person.setId(1);
		person.setName("dzung");
		repo.save(person);
		System.out.println(repo.findById(1));
		System.out.println(repo.findByName("dzung"));
		
		FoodRepo foodRepo = databaseContext.getRepository(FoodRepo.class);
		System.out.println(foodRepo.fetchListMatch());
		
		foodRepo.updateCategory("hello");
		foodRepo.delete(4);
		System.out.println(foodRepo.countById(0));
	}
	
	
}
