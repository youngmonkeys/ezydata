package com.tvd12.ezydata.mongodb.testing;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.testing.bean.Category;
import com.tvd12.ezydata.mongodb.testing.bean.CategoryRepo;
import com.tvd12.ezydata.mongodb.testing.bean.Customer;
import com.tvd12.ezydata.mongodb.testing.bean.CustomerRepo;
import com.tvd12.ezydata.mongodb.testing.bean.EzyCollectionIdCollection1;
import com.tvd12.ezydata.mongodb.testing.bean.EzyCollectionIdCollection2;
import com.tvd12.ezydata.mongodb.testing.bean.EzyCollectionIdCollectionRepo1;
import com.tvd12.ezydata.mongodb.testing.bean.EzyCollectionIdCollectionRepo2;
import com.tvd12.ezydata.mongodb.testing.bean.EzyCollectionIdCompositeId1;
import com.tvd12.ezydata.mongodb.testing.bean.EzyCollectionIdCompositeId2;
import com.tvd12.ezydata.mongodb.testing.bean.EzyIdCollection1;
import com.tvd12.ezydata.mongodb.testing.bean.EzyIdCollection2;
import com.tvd12.ezydata.mongodb.testing.bean.EzyIdCollectionRepo1;
import com.tvd12.ezydata.mongodb.testing.bean.EzyIdCollectionRepo2;
import com.tvd12.ezydata.mongodb.testing.bean.EzyIdCompositeId1;
import com.tvd12.ezydata.mongodb.testing.bean.EzyIdCompositeId2;
import com.tvd12.ezydata.mongodb.testing.bean.Person;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.test.assertion.Asserts;

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
		
		EzyIdCollectionRepo1 idCollectionRepo1 = databaseContext.getRepository(EzyIdCollectionRepo1.class);
		idCollectionRepo1.save(new EzyIdCollection1(new EzyIdCompositeId1("Hello"), "World"));
		Asserts.assertEquals(
				idCollectionRepo1.findById(new EzyIdCompositeId1("Hello")), 
				new EzyIdCollection1(new EzyIdCompositeId1("Hello"), "World")
		);
		
		EzyIdCollectionRepo2 idCollectionRepo2 = databaseContext.getRepository(EzyIdCollectionRepo2.class);
		idCollectionRepo2.save(new EzyIdCollection2(new EzyIdCompositeId2("Hello"), "World"));
		Asserts.assertEquals(
				idCollectionRepo2.findById(new EzyIdCompositeId2("Hello")), 
				new EzyIdCollection2(new EzyIdCompositeId2("Hello"), "World")
		);
		
		EzyCollectionIdCollectionRepo1 collectionIdCollectionRepo1 = 
				databaseContext.getRepository(EzyCollectionIdCollectionRepo1.class);
		collectionIdCollectionRepo1.save(
				new EzyCollectionIdCollection1(new EzyCollectionIdCompositeId1("Hello"), "World"));
		Asserts.assertEquals(
				collectionIdCollectionRepo1.findById(new EzyCollectionIdCompositeId1("Hello")), 
				new EzyCollectionIdCollection1(new EzyCollectionIdCompositeId1("Hello"), "World")
		);
		
		EzyCollectionIdCollectionRepo2 collectionIdCollectionRepo2 = 
				databaseContext.getRepository(EzyCollectionIdCollectionRepo2.class);
		collectionIdCollectionRepo2.save(
				new EzyCollectionIdCollection2(new EzyCollectionIdCompositeId2("Hello"), "World"));
		Asserts.assertEquals(
				collectionIdCollectionRepo2.findById(new EzyCollectionIdCompositeId2("Hello")), 
				new EzyCollectionIdCollection2(new EzyCollectionIdCompositeId2("Hello"), "World")
		);
		
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
