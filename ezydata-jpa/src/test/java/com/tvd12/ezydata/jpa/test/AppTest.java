package com.tvd12.ezydata.jpa.test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.repo.EmployeeRepo;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class AppTest extends BaseJpaTest {
	
	public static void main(String[] args) throws Exception {
		EzyAbstractRepositoryImplementer.setDebug(true);
		EzyQueryEntity query1 = EzyQueryEntity.builder()
				.name("findListByEmail")
				.value("select e.id, e.fullName from User e")
				.resultType(UserIdFullNameResult.class)
				.build();
		EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
				.addQuery(query1)
				.repositoryClass(UserRepo.class)
				.scan("com.tvd12.ezydata.jpa.test.repo")
				.entityManagerFactory(ENTITY_MANAGER_FACTORY)
				.build();
		UserRepo userRepo = databaseContext.getRepository(UserRepo.class);
		System.out.println(userRepo.count());
		System.out.println(userRepo.findByEmail("dzung@gmail.com"));
		System.out.println(userRepo.findByField("email", "dzung@gmail.com"));
		System.out.println("findListByEmail2: " + userRepo.findListByEmail2("dzung@gmail.com"));
		System.out.println("countAll: " + userRepo.countAll(""));
		EmployeeRepo employeeRepo = databaseContext.getRepository(EmployeeRepo.class);
		Employee employee = new Employee();
		employee.setEmployeeId("dzung");
		employee.setFirstName("Dep");
		employee.setLastName("Trai");
		employeeRepo.save(employee);
		System.out.println(employeeRepo.updateEmployee("dzung", "Dung"));
		System.out.println("countAll employeeRepo: " + employeeRepo.countAll());
		System.out.println("countAll2 employeeRepo: " + employeeRepo.countAll2());
		Thread.sleep(2000L);
		databaseContext.close();
	}
	
}
