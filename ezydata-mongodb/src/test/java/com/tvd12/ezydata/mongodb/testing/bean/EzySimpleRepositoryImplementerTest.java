package com.tvd12.ezydata.mongodb.testing.bean;

import java.util.Arrays;

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
		
		DuckRepo duckRepo = databaseContext.getRepository(DuckRepo.class);
		DuckId duckId1 = new DuckId(1, "foo");
		Duck duck1 = new Duck();
		duck1.setId(duckId1);
		duck1.setAge(10);
		duck1.setDescription("test");
		DuckId duckId2 = new DuckId(2, "bar");
		Duck duck2 = new Duck();
		duck2.setId(duckId2);
		duck2.setAge(11);
		duck2.setDescription("hello");
		duckRepo.save(Arrays.asList(duck1, duck2));
		
		System.out.println("found duck1: " + duckRepo.findById(duckId1));
		System.out.println("found duck2: " + duckRepo.findDuckById(duckId1));
	}
	
	
}
