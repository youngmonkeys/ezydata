package com.tvd12.ezydata.database.test;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzyQueryString;
import com.tvd12.ezydata.database.query.EzySimpleQueryManager;
import com.tvd12.ezydata.database.test.bean.Person;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;

public class EzyAbstractRepositoryImplementerTest {

	@Test
	public void test() {
		EzySimpleQueryManager queryManager = new EzySimpleQueryManager();
		queryManager.addQuery(EzyQueryEntity.builder()
				.name("test")
				.value("select e from E e")
				.resultType(Person.class)
				.build());
		EzyClass clazz = new EzyClass(RepoA.class);
		Impl impl = new Impl(clazz);
		impl.setQueryManager(queryManager);
		impl.getQueryString(clazz.getMethod("find1"));
		impl.getQueryString(clazz.getMethod("find2"));
		try {
			impl.getQueryString(clazz.getMethod("find3"));
		}
		catch (Exception e) {
			assert e instanceof IllegalArgumentException;
		}
		try {
			impl.getQueryString(clazz.getMethod("find4"));
		}
		catch (Exception e) {
			assert e instanceof IllegalArgumentException;
		}
	}
	
	public static interface RepoA extends EzyDatabaseRepository<Integer, Person> {
		
		@EzyQuery("select e from E e")
		void find1();
		
		@EzyQuery(name = "test")
		void find2();
		
		@EzyQuery
		void find3();
		
		@EzyQuery(name = "test no one")
		void find4();
		
		void helloWorld();
		
	}
	
	
	public static class Impl extends EzyAbstractRepositoryImplementer {

		public Impl(EzyClass clazz) {
			super(clazz);
		}

		@Override
		protected Class<?> getSuperClass() {
			return DbRepository.class;
		}
		
		@Override
		protected EzyQueryString getQueryString(EzyMethod method) {
			return super.getQueryString(method);
		}
	}
}
