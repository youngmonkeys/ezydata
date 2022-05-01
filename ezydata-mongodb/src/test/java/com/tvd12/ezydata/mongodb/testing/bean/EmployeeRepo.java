package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.util.EzyNext;

import java.util.List;

@EzyAutoImpl
public interface EmployeeRepo extends EzyMongoRepository<String, Employee> {
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

    int countByEmail(String email);
}
