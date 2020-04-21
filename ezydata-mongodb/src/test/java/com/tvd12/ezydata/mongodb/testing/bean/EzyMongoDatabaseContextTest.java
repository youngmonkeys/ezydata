package com.tvd12.ezydata.mongodb.testing.bean;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.testing.MongodbTest;

public class EzyMongoDatabaseContextTest extends MongodbTest {

	@Test
	public void test() {
		EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.scan("com.tvd12.ezydata.mongodb.testing.bean")
				.build();
		CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
		Customer customer = new Customer();
		customer.setName("dzung");
		customerRepo.save(customer);
		System.out.println(customer);
	}
	
}
