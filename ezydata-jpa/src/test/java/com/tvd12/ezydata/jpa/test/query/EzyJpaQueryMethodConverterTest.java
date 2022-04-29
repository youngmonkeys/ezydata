package com.tvd12.ezydata.jpa.test.query;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.query.EzyQueryMethod;
import com.tvd12.ezydata.jpa.query.EzyJpaQueryMethodConverter;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.repo.EmployeeRepo;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;

public class EzyJpaQueryMethodConverterTest {

    private EzyJpaQueryMethodConverter sut = new EzyJpaQueryMethodConverter();
    
    @Test
    public void convert() {
        EzyClass repoClass = new EzyClass(EmployeeRepo.class);
        EzyMethod method = repoClass.getMethod(
                "findByEmployeeIdAndEmailInOrPhoneNumberInAndBankAccountNo");
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        String queryString = sut.toQueryString(Employee.class, queryMethod);
        System.out.println(queryString);
    }
    
    @Test
    public void convertWithOneField() {
        EzyClass repoClass = new EzyClass(EmployeeRepo.class);
        EzyMethod method = repoClass.getMethod(
                "findByEmail");
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        String queryString = sut.toQueryString(Employee.class, queryMethod);
        System.out.println(queryString);
    }
    
}
