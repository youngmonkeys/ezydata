package com.tvd12.ezydata.jpa.test;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class EzyJpaRepositoriesImplementerTest extends BaseJpaTest {

	protected EzyDatabaseContext databaseContext;
	
	protected EzyJpaRepositoriesImplementerTest() {
		databaseContext = new EzyJpaDatabaseContextBuilder()
				.repositoryInterfaces(Object.class)
				.repositoryInterfaces(InterfaceA.class)
				.repositoryInterfaces(UserXRepo.class)
				.entityManagerFactory(ENTITY_MANAGER_FACTORY)
				.build();
	}

	@Test
	public void test() {
		EzyAbstractRepositoryImplementer.setDebug(true);
		UserXRepo userRepo = (UserXRepo) databaseContext.getRepository(UserXRepo.class); 
		System.out.println(userRepo.count());
		System.out.println(userRepo.findByEmail("dzung@gmail.com"));
		List<UserIdFullNameResult> list = userRepo.findListOfIdAndFullName();
		System.out.println(list);
	}
	
	public static interface InterfaceA {
		
	}
	
	public static interface UserXRepo extends EzyDatabaseRepository<String, User> {
		
		@EzyQuery("select e from User e where e.email = ?0")
		User findByEmail(String email);
		
		@EzyQuery(
				value = "select e.id, e.fullName from User e where e.email = ?0",
				resultType = UserIdFullNameResult.class)
		UserIdFullNameResult findByEmail2(String email);
		
		@EzyQuery(
				value = "select e.id, e.fullName from User e",
				resultType = UserIdFullNameResult.class
		)
		List<UserIdFullNameResult> findListOfIdAndFullName();
		
	}
}
