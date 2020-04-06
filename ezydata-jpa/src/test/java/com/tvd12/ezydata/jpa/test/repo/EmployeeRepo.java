package com.tvd12.ezydata.jpa.test.repo;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface EmployeeRepo extends EzyDatabaseRepository<String, Employee> {
}
