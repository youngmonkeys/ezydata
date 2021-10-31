package com.tvd12.ezydata.jpa.test;

import java.util.Arrays;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoryImplementer;
import com.tvd12.ezydata.jpa.loader.EzyJpaDataSourceLoader;
import com.tvd12.ezydata.jpa.loader.EzyJpaEntityManagerFactoryLoader;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.repo.EmployeeRepo;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;
import com.tvd12.ezyfox.util.EzyNext;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.test.assertion.Asserts;

public class EzyJpaByHibernateRepositoryTest extends BaseJpaTest {

	protected final Properties properties;
	
	public EzyJpaByHibernateRepositoryTest() {
		this.properties = new BaseFileReader()
				.read("application.yaml");
	}
	
	@Test
	public void test() throws Exception {
		EzyJpaRepositoryImplementer.setDebug(true);
		EzyDatabaseContext databaseContext = databaseContext();
		
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
		assert employeeRepo.findList(EzyNext.fromSkipLimit(0, 100)).size() >= 1;
		assert employeeRepo.findAll().size() >= 0;
		assert employeeRepo.findAll(0, 1).size() == 1;
		assert employeeRepo.findByEmail("dzung@youngmokeys.org") != null;
		assert employeeRepo.findByEmailAndPhoneNumber(
				"dzung@youngmokeys.org", 
				"123456789"
				) != null;
		long count = employeeRepo.count();
		assert employeeRepo.findByEmployeeIdAndEmailInOrPhoneNumberInAndBankAccountNo(
				"dzung", 
				Arrays.asList("dzung@youngmokeys.org"), 
				Arrays.asList("123456789"), 
				"abcdefgh", 
				EzyNext.fromSkipLimit(0, 100)
				).size() == count;
		Asserts.assertEquals(employeeRepo.findByEmployeeId("dzung"), employeeRepo.findById("dzung"));
		assert employeeRepo.findListByEmail("dzung@youngmokeys.org").get(0) instanceof Employee;
		assert employeeRepo.findEmployeeIdByEmployeeId("dzung").getEmployeeId().equals("dzung");
		assert employeeRepo.countByEmail("dzung@youngmokeys.org") == count;
		employeeRepo.delete("employee2");
		assert employeeRepo.count() == (count - 1);
		assert employeeRepo.deleteByIds(Arrays.asList("employee3")) >= 1;
		employeeRepo.deleteAll();
		assert employeeRepo.count() == 0;
		Thread.sleep(1000L);
	}
	
	private EzyDatabaseContext databaseContext() {
		EzyQueryEntity query1 = EzyQueryEntity.builder()
				.name("findListByEmail")
				.value("select e.id, e.fullName from User e")
				.resultType(UserIdFullNameResult.class)
				.build();
		return new EzyJpaDatabaseContextBuilder()
				.addQuery(query1)
				.repositoryClass(UserRepo.class)
				.scan("com.tvd12.ezydata.jpa.test.repo")
				.scan("com.tvd12.ezydata.jpa.test.result")
				.entityManagerFactory(entityManagerFactory())
				.build();
	}
	
	private EntityManagerFactory entityManagerFactory() {
		return new EzyJpaEntityManagerFactoryLoader()
				.entityPackage("com.tvd12.ezydata.jpa.test.entity")
				.dataSource(dataSource())
				.properties(properties)
				.load("Test");
	}
	
	private DataSource dataSource() {
		return new EzyJpaDataSourceLoader()
				.properties(properties, "datasource")
				.load();
	}
}
