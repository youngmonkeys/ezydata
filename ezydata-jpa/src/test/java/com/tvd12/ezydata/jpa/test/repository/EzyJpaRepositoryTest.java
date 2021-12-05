package com.tvd12.ezydata.jpa.test.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContext;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.repository.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.BaseJpaTest;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.repo.EmployeeRepo;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyJpaRepositoryTest extends BaseJpaTest {
    
    @Test
    public void saveOneFailed() {
        // given
        UserRepo sut = new UserRepo();
        
        EntityManager entityManager = mock(EntityManager.class);
        
        Exception exception = new RuntimeException("just test");
        when(entityManager.merge(any())).thenThrow(exception);

        EntityTransaction transaction = mock(EntityTransaction.class);
        when(entityManager.getTransaction()).thenReturn(transaction);
        
        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);
        
        // when
        Throwable e = Asserts.assertThrows(() -> sut.save(new User()));
        
        // then
        Asserts.assertEquals(e, exception);
        verify(entityManager, times(1)).getTransaction();
        verify(transaction, times(1)).begin();
        verify(transaction, times(1)).rollback();
    }
    
    @Test
    public void saveManyFailed() {
        // given
        UserRepo sut = new UserRepo();
        
        EntityManager entityManager = mock(EntityManager.class);
        
        Exception exception = new RuntimeException("just test");
        when(entityManager.merge(any())).thenThrow(exception);

        EntityTransaction transaction = mock(EntityTransaction.class);
        when(entityManager.getTransaction()).thenReturn(transaction);
        
        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);
        
        // when
        Throwable e = Asserts.assertThrows(() -> sut.save(Arrays.asList(new User())));
        
        // then
        Asserts.assertEquals(e, exception);
        verify(entityManager, times(1)).getTransaction();
        verify(transaction, times(1)).begin();
        verify(transaction, times(1)).rollback();
    }
    
    @Test
    public void findByQueryTest() {
        // given
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(UserRepo.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        UserRepo userRepo = databaseContext.getRepository(UserRepo.class);
        userRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        User user = new User();
        user.setEmail(email);
        userRepo.save(user);
        
        
        String query = "select e from User e where e.email = ?0";
        
        // when
        User actual = MethodInvoker.create()
            .object(userRepo)
            .method("findByQueryString")
            .param(String.class, query)
            .param(Object[].class, new Object[] { email })
            .call();
        
        // then
        Asserts.assertEquals(actual, user);
        userRepo.deleteAll();
    }
    
    @Test
    public void findByQueryNotFoundTest() {
        // given
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryClass(UserRepo.class)
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        UserRepo userRepo = databaseContext.getRepository(UserRepo.class);
        userRepo.deleteAll();
        
        String email = "don't know@youngmonkeys.org";
        String query = "select e from User e where e.email = ?0";
        
        // when
        User actual = MethodInvoker.create()
            .object(userRepo)
            .method("findByQueryString")
            .param(String.class, query)
            .param(Object[].class, new Object[] { email })
            .call();
        
        // then
        Asserts.assertNull(actual);
        userRepo.deleteAll();
    }
    
    @Test
    public void findOptional() {
        // given
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryInterface(EmployeeRepo.class)
                .scan("com.tvd12.ezydata.jpa.test.entity")
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        EmployeeRepo employeeRepo = databaseContext.getRepository(EmployeeRepo.class);
        employeeRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        Employee employee = new Employee();
        employee.setEmployeeId("dzung");
        employee.setFirstName("Dep");
        employee.setLastName("Trai");
        employee.setEmail(email);
        employeeRepo.save(employee);
        
        
        // when
        Optional<Employee> actual = employeeRepo.findByEmailOptional(email);
        
        // then
        Asserts.assertEquals(actual.get(), employee);
        employeeRepo.deleteAll();
    }
    
    @Test
    public void findOptionalEmpty() {
        // given
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryInterface(EmployeeRepo.class)
                .scan("com.tvd12.ezydata.jpa.test.entity")
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        EmployeeRepo employeeRepo = databaseContext.getRepository(EmployeeRepo.class);
        employeeRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        
        
        // when
        Optional<Employee> actual = employeeRepo.findByEmailOptional(email);
        
        // then
        Asserts.assertEquals(actual, Optional.empty());
        employeeRepo.deleteAll();
    }
    
    @Test
    public void deleteTest() {
        // given
        EzyDatabaseContext databaseContext = new EzyJpaDatabaseContextBuilder()
                .repositoryInterface(EmployeeRepo.class)
                .scan("com.tvd12.ezydata.jpa.test.entity")
                .entityManagerFactory(ENTITY_MANAGER_FACTORY)
                .build();
        EmployeeRepo employeeRepo = databaseContext.getRepository(EmployeeRepo.class);
        employeeRepo.deleteAll();
        
        String email = "monkey@youngmonkeys.org";
        Employee employee = new Employee();
        employee.setEmployeeId("dzung");
        employee.setFirstName("Dep");
        employee.setLastName("Trai");
        employee.setEmail(email);
        employeeRepo.save(employee);
        
        
        // when
        int actual = employeeRepo.deleteByEmail(email);
        
        // then
        Asserts.assertEquals(actual, 1);
        Asserts.assertZero(employeeRepo.count());
    }
    
    @Test
    public void deleteByQueryStringFailed() {
        // given
        UserRepo sut = new UserRepo();
        
        EntityManager entityManager = mock(EntityManager.class);

        EntityTransaction transaction = mock(EntityTransaction.class);
        when(entityManager.getTransaction()).thenReturn(transaction);
        
        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);
        
        String queryString = "delete e from User e";
        
        Query query = mock(Query.class);
        when(entityManager.createQuery(queryString)).thenReturn(query);
        
        Exception exception = new RuntimeException("just test");
        when(query.executeUpdate()).thenThrow(exception);
        
        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(sut)
                .method("deleteByQueryString")
                .param(String.class, queryString)
                .param(Object[].class, new Object[] { "" })
                .call()
        );
        
        // then
        Asserts.assertEquals(e.getCause().getCause(), exception);
        verify(entityManager, times(1)).createQuery(queryString);
        verify(entityManager, times(1)).getTransaction();
        verify(query, times(1)).executeUpdate();
        verify(transaction, times(1)).begin();
        verify(transaction, times(1)).rollback();
    }
    
    @Test
    public void getEntityTypeFailed() {
        // given
        // when
        Throwable e = Asserts.assertThrows(() ->
            new InternalRepo()
        );
        
        // then
        Asserts.assertEqualsType(e, UnimplementedOperationException.class);
    }
    
    @SuppressWarnings("rawtypes")
    public static class InternalRepo extends EzyJpaRepository {}
}
