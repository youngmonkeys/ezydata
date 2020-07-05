package com.tvd12.ezydata.mongodb.testing;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoryImplementer;
import com.tvd12.ezydata.mongodb.testing.bean.Duck;
import com.tvd12.ezydata.mongodb.testing.bean.DuckId;
import com.tvd12.ezydata.mongodb.testing.bean.DuckRepo;
import com.tvd12.ezydata.mongodb.testing.bean.FoodRepo;
import com.tvd12.ezydata.mongodb.testing.bean.Person;
import com.tvd12.ezydata.mongodb.testing.bean.PersonRepo;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.Next;

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
		System.out.println(foodRepo.fetchOneMatch());
		System.out.println(foodRepo.fetchOneMatch2());
		
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
		Duck duck3 = new Duck();
		DuckId duckId3 = new DuckId(3, "no");
		duck3.setId(duckId3);
		duck3.setAge(12);
		duck3.setDescription("world");
		duckRepo.save(Arrays.asList(duck1, duck2, duck3));
		
		System.out.println("found duck1: " + duckRepo.findById(duckId1));
		System.out.println("found duck2: " + duckRepo.findDuckById(duckId1));
		assert duckRepo.findListByIds(Sets.newHashSet(duckId1, duckId2)).size() >= 2;
		assert duckRepo.findByField("age", 11) != null;
		assert duckRepo.findListByField("age", 10).size() >= 1;
		assert duckRepo.findListByField("age", 10, 0, 1).size() == 1;
		assert duckRepo.findAll().size() >= 2;
		assert duckRepo.findAll(0, 1).size() == 1;
		assert duckRepo.findListByAge(10, Next.fromSkipLimit(0, 10)).size() >= 2;
		assert duckRepo.findListByAge2(10).size() >= 2;
		System.out.println("findListByAge: " + duckRepo.findListByAge(10, Next.fromSkipLimit(0, 2)));
		assert duckRepo.countByAge(10, Next.fromSkipLimit(0, 2)) >= 2;
		long count = duckRepo.count();
		assert duckRepo.deleteByAge(12) == 1;
		assert duckRepo.deleteByAge2(12) == 0;
		assert duckRepo.count() == count - 1;
		assert duckRepo.deleteByIds(Sets.newHashSet(duckId1)) == 1;
		assert duckRepo.count() == count - 2;
		duckRepo.deleteAll();
		assert duckRepo.count() == 0;
		try {
			duckRepo.updateByAge();
		}
		catch (Exception e) {
			assert e instanceof IllegalArgumentException;
		}
	}
	
	
}
