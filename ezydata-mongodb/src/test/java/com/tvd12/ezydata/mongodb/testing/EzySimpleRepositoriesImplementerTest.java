package com.tvd12.ezydata.mongodb.testing;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.bean.EzyRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.bean.EzyMongoRepositoryImplementer;
import com.tvd12.ezydata.mongodb.query.EzyMongoQueryMethodConverter;
import com.tvd12.ezydata.mongodb.testing.bean.NothingInterface;
import com.tvd12.ezydata.mongodb.testing.bean.PersonRepo2;
import com.tvd12.ezyfox.collect.Sets;

public class EzySimpleRepositoriesImplementerTest extends MongodbTest {

	@Test
	public void test() {
		EzyMongoDatabaseContext databaseContext = new EzyMongoDatabaseContextBuilder()
				.mongoClient(mongoClient)
				.databaseName(databaseName)
				.scan("com.tvd12.ezydata.mongodb.testing.bean")
				.build();
		
		EzyMongoRepositoryImplementer.setDebug(true);
		EzyRepositoriesImplementer implementer = new ExEzySimpleRepositoriesImplementer()
				.queryMethodConverter(new EzyMongoQueryMethodConverter())
				.scan("com.tvd12.ezydata.mongodb.testing.bean")
				.scan("com.tvd12.ezydata.mongodb.testing.bean", "com.tvd12.ezydata.mongodb.testing.bean")
				.scan(Sets.newHashSet("com.tvd12.ezydata.mongodb.testing.bean"))
				.repositoryInterface(PersonRepo2.class)
				.repositoryInterface(Class.class)
				.repositoryInterface(NothingInterface.class)
				.repositoryInterfaces(PersonRepo2.class, PersonRepo2.class)
				.repositoryInterfaces(Sets.newHashSet(PersonRepo2.class));
		
		Map<Class<?>, Object> repos = implementer.implement(databaseContext);
		System.out.println("repos: " + repos);
	}
	
	public static class ExEzySimpleRepositoriesImplementer extends EzyAbstractRepositoriesImplementer {

		@Override
		protected EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf) {
			return new ExEzySimpleRepositoryImplementer(itf);
		}
		
	}
	
	public static class ExEzySimpleRepositoryImplementer extends EzyMongoRepositoryImplementer {

		public ExEzySimpleRepositoryImplementer(Class<?> clazz) {
			super(clazz);
		}

		@Override
		protected void setRepoComponent(Object repo, Object template) {
		}

		@Override
		protected Class<?> getSuperClass() {
			return ExEzyMongoRepository.class;
		}
		
	}
}
