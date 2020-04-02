package com.tvd12.ezydata.jpa.test;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoriesImplementer;
import com.tvd12.ezydata.jpa.test.entity.User;

public class EzySimpleRepositoriesImplementerTest extends BaseJpaTest {

	@Test
	public void test() {
		EzyJpaRepositoriesImplementer implementer = new EzyJpaRepositoriesImplementer();
		implementer.repositoryInterfaces(Object.class);
		implementer.repositoryInterfaces(InterfaceA.class);
		implementer.repositoryInterfaces(UserXRepo.class);
		Map<Class<?>, Object> map = implementer.implement(ENTITY_MANAGER);
		UserXRepo userRepo = (UserXRepo) map.get(UserXRepo.class); 
		System.out.println(userRepo.count());
	}
	
	public static interface InterfaceA {
		
	}
	
	public static interface UserXRepo extends EzyDatabaseRepository<String, User> {
	}
}
