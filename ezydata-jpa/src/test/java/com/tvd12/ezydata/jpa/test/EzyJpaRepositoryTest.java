package com.tvd12.ezydata.jpa.test;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.repo.EmployeeRepo;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;

public class EzyJpaRepositoryTest extends BaseJpaTest {

	@Test
	public void test() throws Exception {
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
		
		Employee employee2 = new Employee();
		employee2.setEmployeeId("employee2");
		employee2.setFirstName("Foo");
		employee2.setLastName("Bar");
		Employee employee3 = new Employee();
		employee3.setEmployeeId("employee3");
		employee3.setFirstName("Hello");
		employee3.setLastName("World");
		employeeRepo.save(Arrays.asList(employee2, employee3));
		assert employeeRepo.findById("employee2") != null;
		assert employeeRepo.findListByIds(Arrays.asList("employee2", "employee3")).size() >= 2;
		assert employeeRepo.findByField("firstName", "Foo") != null;
		assert employeeRepo.findListByField("firstName", "Hello").size() >= 1;
		assert employeeRepo.findListByField("firstName", "Hello", 0, 2).size() >= 1;
		assert employeeRepo.findAll().size() >= 0;
		assert employeeRepo.findAll(0, 1).size() == 1;
		long count = employeeRepo.count();
		employeeRepo.delete("employee2");
		assert employeeRepo.count() == (count - 1);
		assert employeeRepo.deleteByIds(Arrays.asList("employee3")) >= 1;
		employeeRepo.deleteAll();
		assert employeeRepo.count() == 0;
		Thread.sleep(1000L);
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void invalidMethodPrefixTest() {
		new EzyJpaDatabaseContextBuilder()
				.repositoryInterface(ARepo.class)
				.build();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void invalidCountMethodTest() {
		new EzyJpaDatabaseContextBuilder()
				.repositoryInterface(BRepo.class)
				.build();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void invalidDeleteMethodTest() {
		new EzyJpaDatabaseContextBuilder()
				.repositoryInterface(CRepo.class)
				.build();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void invalidQueryNameTest() {
		new EzyJpaDatabaseContextBuilder()
				.repositoryInterface(DRepo.class)
				.build();
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void invalidQueryName2Test() {
		new EzyJpaDatabaseContextBuilder()
				.repositoryInterface(ERepo.class)
				.build();
	}
	
	@Test
	public void validQueryName() {
		EzyQueryEntity query1 = EzyQueryEntity.builder()
				.name("test")
				.value("select e.id, e.fullName from User e")
				.resultType(UserIdFullNameResult.class)
				.build();
		new EzyJpaDatabaseContextBuilder()
				.addQuery(query1)
				.repositoryInterface(ERepo.class)
				.entityManagerFactory(ENTITY_MANAGER_FACTORY)
				.build();
	}
	
	private static interface ARepo extends EzyDatabaseRepository<String, Employee> {
		@EzyQuery("select e from Employee e")
		Employee getEmployee();
	}
	
	private static interface BRepo extends EzyDatabaseRepository<String, Employee> {
		@EzyQuery("select e from Employee e")
		Employee countEmployee();
	}
	
	private static interface CRepo extends EzyDatabaseRepository<String, Employee> {
		@EzyQuery("select e from Employee e")
		Employee deleteEmployee();
	}
	
	private static interface DRepo extends EzyDatabaseRepository<String, Employee> {
		@EzyQuery
		int deleteEmployee();
	}
	
	private static interface ERepo extends EzyDatabaseRepository<String, Employee> {
		@EzyQuery(name = "test")
		int deleteEmployee();
	}
}
