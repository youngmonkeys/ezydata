package com.tvd12.ezydata.jpa.test.repo;

import javax.transaction.Transactional;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface EmployeeRepo extends EzyDatabaseRepository<String, Employee> {
	
	@Transactional
	@EzyQuery("update Employee e set e.firstName = ?1 where e.id = ?0")
	int updateEmployee(String id, String firstName);
	
}
