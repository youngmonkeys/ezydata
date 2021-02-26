package com.tvd12.ezydata.mongodb.testing;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.testing.bean.Category;
import com.tvd12.ezydata.mongodb.testing.bean.CategoryRepo;
import com.tvd12.ezydata.mongodb.testing.bean.Customer;
import com.tvd12.ezydata.mongodb.testing.bean.CustomerRepo;
import com.tvd12.ezydata.mongodb.testing.bean.Person;

public class EzyMongoDatabaseContextTest extends MongodbTest {

	@Test
	public void test() {
		EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.maxIdCollectionName("___max_id___")
				.scan("com.tvd12.ezydata.mongodb.testing.bean")
				.build();
		assert databaseContext.getClient() == mongoClient;
		assert databaseContext.getDatabase().getName().equals(databaseName);
		CustomerRepo customerRepo = databaseContext.getRepository(CustomerRepo.class);
		Customer customer = new Customer();
		customer.setName("dzung");
		customerRepo.save(customer);
		System.out.println(customer);
		CategoryRepo categoryRepo = databaseContext.getRepository(CategoryRepo.class);
		Category category1 = new Category("category1");
		Category category2 = new Category("category2");
		assert category1.getName() == null;
		assert category2.getName() == null;
		categoryRepo.save(Arrays.asList(category1, category2));
		assert category1.getName() != null;
		assert category2.getName() != null;
		
		try {
			new EzyMongoDatabaseContextBuilder()
				.repositoryInterface(RepoA.class)
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.build();
		}
		catch (Exception e) {
			assert e instanceof IllegalStateException;
		}
		
		try {
			new EzyMongoDatabaseContextBuilder()
				.repositoryInterface(RepoB.class)
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.build();
		}
		catch (Exception e) {
			assert e instanceof IllegalStateException;
		}
		
		try {
			new EzyMongoDatabaseContextBuilder()
				.repositoryInterface(RepoC.class)
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.build();
		}
		catch (Exception e) {
			assert e instanceof IllegalStateException;
		}
		
		databaseContext = new EzyMongoDatabaseContextBuilder()
			.repositoryInterface(RepoD.class)
			.mongoClient(mongoClientLoader().load())
			.databaseName(databaseName)
			.dataConverter(EzyMongoDataConverter.builder().build())
			.build();
		databaseContext.close();
	}
	
	public static interface RepoA extends EzyMongoRepository<Integer, Person> {
		
		@EzyQuery("{}")
		void countInvalid();
		
	}
	
	public static interface RepoB extends EzyMongoRepository<Integer, Person> {
		
		@EzyQuery("{}")
		Class<?> deleteInvalid();
		
	}
	
	public static interface RepoC extends EzyMongoRepository<Integer, Person> {
		
		@EzyQuery("{}")
		Class<?> invalid();
		
	}
	
	public static interface RepoD extends EzyMongoRepository<Integer, Person> {
		
		@EzyQuery(resultType = Person.class, value = "{}")
		Person findByName();
		
		@EzyQuery("{}")
		int countByName1();
		
		@EzyQuery("{}")
		long countByName2();
		
		@EzyQuery("{}")
		List<Person> findListByName();
		
		@EzyQuery(resultType = Person.class, value = "{}")
		Person fetchByName();
		
		@EzyQuery("{}")
		Person fetchListByName();
		
	}
	
}
