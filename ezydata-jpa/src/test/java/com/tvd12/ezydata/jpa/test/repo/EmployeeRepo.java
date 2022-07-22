package com.tvd12.ezydata.jpa.test.repo;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.result.EmployeeIdAndFirstNameResult;
import com.tvd12.ezydata.jpa.test.result.EmployeeIdResult;
import com.tvd12.ezydata.jpa.test.result.EmployeeResult;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.database.annotation.EzyTransactional;
import com.tvd12.ezyfox.util.EzyNext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    @EzyQuery("delete from Employee e where e.firstName = ?0")
    void deleteByFirstName(String firstName);

    @EzyQuery("delete from Employee e where e.firstName = ?0")
    int deleteByFirstName2(String firstName);

    @EzyQuery("select e from Employee e")
    List<Employee> findList(EzyNext next);

    Employee findByEmail(String email);

    @EzyQuery(
        "select e from Employee e where e.email = ?0 and e.firstName = ?1"
    )
    Employee findByEmail(String email, String firstName);

    @EzyQuery("select e from Employee e where e.email = ?0")
    Optional<Employee> findByEmailOptional(String email);

    Employee findBy();

    Employee findByEmailAndPhoneNumber(String email, String phoneNumber);

    List<Employee> findByEmployeeIdAndEmailInOrPhoneNumberInAndBankAccountNo(
        String employeeId,
        List<String> emails,
        List<String> phoneNumbers,
        String bankAccountNo,
        EzyNext next
    );

    int countByEmail(String email);

    @EzyQuery(
        value = "select * from ezyfox_jpa_employee where employeeId = ?0",
        nativeQuery = true
    )
    Employee findByEmployeeId(String employeeId);

    @EzyQuery(
        value = "select employeeId from ezyfox_jpa_employee where employeeId = ?0",
        nativeQuery = true
    )
    EmployeeIdResult findEmployeeIdByEmployeeId(String employeeId);

    @EzyQuery(
        value = "select employeeId from ezyfox_jpa_employee where employeeId = ?0",
        nativeQuery = true
    )
    Optional<EmployeeIdResult> findEmployeeIdByEmployeeIdOptional(String employeeId);

    @EzyQuery(
        value = "select employeeId, firstName from ezyfox_jpa_employee where employeeId = ?0",
        nativeQuery = true
    )
    Optional<EmployeeIdAndFirstNameResult> findEmployeeIdAndFirstNameByEmployeeIdOptional(
        String employeeId
    );

    @EzyQuery(
        value = "select employeeId from ezyfox_jpa_employee where employeeId = ?0",
        nativeQuery = true
    )
    Optional<?> findEmployeeIdByEmployeeIdOptionalNoType(String employeeId);

    @EzyQuery(
        value = "select * from ezyfox_jpa_employee where email = ?0",
        nativeQuery = true
    )
    List<Employee> findListByEmail(String email);

    @EzyTransactional
    int deleteByEmail(String email);

    @EzyQuery(
        value = "select * from ezyfox_jpa_employee where email = ?0",
        nativeQuery = true,
        resultType = Object.class
    )
    List<Object> findListByEmailNotEntityType(String email);

    @EzyQuery(
        value = "select * from ezyfox_jpa_employee where email = ?0",
        nativeQuery = true,
        resultType = Object.class
    )
    Object findOneByEmailObjectType(String email);

    @EzyQuery(
        value = "select * from ezyfox_jpa_employee where email = ?0",
        nativeQuery = true,
        resultType = EmployeeResult.class
    )
    Optional<Employee> findOptionalByEmailNotEntityType(String email);
}
