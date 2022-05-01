package com.tvd12.ezydata.mongodb.testing.query;

import com.tvd12.ezydata.database.query.EzyQueryMethod;
import com.tvd12.ezydata.mongodb.query.EzyMongoQueryMethodConverter;
import com.tvd12.ezydata.mongodb.testing.bean.Employee;
import com.tvd12.ezydata.mongodb.testing.bean.EmployeeRepo;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import org.testng.annotations.Test;

public class EzyMongoQueryMethodConverterTest {

    private final EzyMongoQueryMethodConverter sut =
        new EzyMongoQueryMethodConverter();

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

    @Test
    public void convertWith2Fields() {
        EzyClass repoClass = new EzyClass(EmployeeRepo.class);
        EzyMethod method = repoClass.getMethod(
            "findByEmailAndPhoneNumber");
        EzyQueryMethod queryMethod = new EzyQueryMethod(method);
        String queryString = sut.toQueryString(Employee.class, queryMethod);
        System.out.println(queryString);
    }
}
