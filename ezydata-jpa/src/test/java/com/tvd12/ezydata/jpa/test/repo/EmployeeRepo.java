package com.tvd12.ezydata.jpa.test.repo;

import java.util.List;

import javax.transaction.Transactional;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.util.EzyNext;

@EzyAutoImpl
public interface EmployeeRepo extends EzyDatabaseRepository<String, Employee> {
	
	@Transactional
	@EzyQuery("update Employee e set e.firstName = ?1 where e.id = ?0")
	int updateEmployee(String id, String firstName);
	
	@EzyQuery("select count(e) from Employee e")
	int countAll();
	
	@EzyQuery("select count(e) from Employee e")
	long countAll2();
	
	@EzyQuery("select e from Employee e where e.firstName = ?0")
	List<Employee> fetchListByFirstName(String firstName);
	
	@EzyQuery(value = "select e from Employee e where e.firstName = ?0", resultType = Employee.class)
	List<Employee> fetchListByFirstName2(String firstName);
	
	@EzyQuery("select e from Employee e where e.firstName = ?0")
	Employee fetchByFirstName(String firstName);
	
	@EzyQuery(value = "select e from Employee e where e.firstName = ?0", resultType = Employee.class)
	Employee fetchByFirstName2(String firstName);
	
	@EzyQuery("delete frome Employee where e.firstName = ?0")
	void deleteByFirstName(String firstName);
	
	@EzyQuery("select e from Employee e")
	List<Employee> findList(EzyNext next);
	
	Employee findByEmail(String email);
	
	Employee findBy();
	
	Employee findByEmailAndPhoneNumber(String email, String phoneNumber);
	
	List<Employee> findByEmployeeIdAndEmailInOrPhoneNumberInAndBankAccountNo(
			String employeeId,
			List<String> emails,
			List<String> phoneNumbers,
			String bankAccountNo,
			EzyNext next
	);
	
}
