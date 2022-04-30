package com.tvd12.ezydata.jpa.test.repository;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContext;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.repository.EzyJpaRepository;
import com.tvd12.ezydata.jpa.test.BaseJpaTest;
import com.tvd12.ezydata.jpa.test.entity.Employee;
import com.tvd12.ezydata.jpa.test.entity.User;
import com.tvd12.ezydata.jpa.test.repo.EmployeeRepo;
import com.tvd12.ezydata.jpa.test.repo.UserRepo;
import com.tvd12.ezydata.jpa.test.result.UserIdFullNameResult;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
        Throwable e = Asserts.assertThrows(() -> sut.save(Collections.singletonList(new User())));

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
            .param(Object[].class, new Object[]{email})
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
            .param(Object[].class, new Object[]{email})
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
        Asserts.assertEquals(actual.orElse(null), employee);
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
    public void deleteByFirstNameTest() {
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
        int actual = employeeRepo.deleteByFirstName2("Dep");

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
                .param(Object[].class, new Object[]{""})
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
        Throwable e = Asserts.assertThrows(InternalRepo::new);

        // then
        Asserts.assertEqualsType(e, UnimplementedOperationException.class);
    }

    @Test
    public void findByQueryStringTest() {
        // given
        UserRepo sut = new UserRepo();

        EntityManager entityManager = mock(EntityManager.class);

        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);

        String queryString = "select e from User e";

        Query query = mock(Query.class);
        User user = new User();
        when(query.getResultList()).thenReturn(Collections.singletonList(user));
        when(entityManager.createQuery(queryString)).thenReturn(query);

        // when
        String email = RandomUtil.randomShortAlphabetString();
        User actual = sut.findByQueryString(
            queryString,
            new Object[] {email}
        );

        // then
        Asserts.assertEquals(actual, user);
        verify(entityManager, times(1)).createQuery(queryString);
        verify(query, times(1)).setParameter(0, email);
        verify(query, times(1)).setMaxResults(1);
        verify(query, times(1)).getResultList();
    }

    @Test
    public void findListByQueryStringTest() {
        // given
        UserRepo sut = new UserRepo();

        EntityManager entityManager = mock(EntityManager.class);

        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);

        String queryString = "select e from User e";

        Query query = mock(Query.class);
        User user = new User();
        when(query.getResultList()).thenReturn(Collections.singletonList(user));
        when(entityManager.createQuery(queryString)).thenReturn(query);

        // when
        int skip = RandomUtil.randomInt();
        int limit = RandomUtil.randomInt();
        String email = RandomUtil.randomShortAlphabetString();
        List<User> actual = sut.findListByQueryString(
            queryString,
            Collections.singletonMap("email", email),
            skip,
            limit
        );

        // then
        Asserts.assertEquals(actual, Collections.singletonList(user));
        verify(entityManager, times(1)).createQuery(queryString);
        verify(query, times(1)).setParameter("email", email);
        verify(query, times(1)).setFirstResult(skip);
        verify(query, times(1)).setMaxResults(limit);
        verify(query, times(1)).getResultList();
    }

    @Test
    public void fetchListByQueryStringTest() {
        // given
        UserRepo sut = new UserRepo();

        EntityManager entityManager = mock(EntityManager.class);

        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);

        String queryString = "select e from User e";

        Query query = mock(Query.class);
        int userId = RandomUtil.randomInt();
        String userFullName = RandomUtil.randomShortAlphabetString();
        UserIdFullNameResult result = new UserIdFullNameResult(
            userId,
            userFullName
        );
        List<Object[]> resultList = Collections.singletonList(
            new Object[] {userId, userFullName}
        );
        when(query.getResultList()).thenReturn(resultList);
        when(
            databaseContext.deserializeResultList(
                resultList,
                UserIdFullNameResult.class
            )
        ).thenReturn(Collections.singletonList(result));
        when(entityManager.createQuery(queryString)).thenReturn(query);

        // when
        int skip = RandomUtil.randomInt();
        int limit = RandomUtil.randomInt();
        String email = RandomUtil.randomShortAlphabetString();
        List<UserIdFullNameResult> actual = sut.fetchUserListByQueryString(
            queryString,
            new Object[] {email},
            skip,
            limit
        );

        // then
        Asserts.assertEquals(
            actual,
            Collections.singletonList(result),
            false
        );
        verify(entityManager, times(1)).createQuery(queryString);
        verify(query, times(1)).setParameter(0, email);
        verify(query, times(1)).setFirstResult(skip);
        verify(query, times(1)).setMaxResults(limit);
        verify(query, times(1)).getResultList();
        verify(databaseContext, times(1)).deserializeResultList(
            resultList,
            UserIdFullNameResult.class
        );
    }

    @Test
    public void fetchListByQueryStringAndParameterMapTest() {
        // given
        UserRepo sut = new UserRepo();

        EntityManager entityManager = mock(EntityManager.class);

        EzyJpaDatabaseContext databaseContext = mock(EzyJpaDatabaseContext.class);
        when(databaseContext.createEntityManager()).thenReturn(entityManager);
        sut.setDatabaseContext(databaseContext);

        String queryString = "select e from User e";

        Query query = mock(Query.class);
        int userId = RandomUtil.randomInt();
        String userFullName = RandomUtil.randomShortAlphabetString();
        UserIdFullNameResult result = new UserIdFullNameResult(
            userId,
            userFullName
        );
        List<Object[]> resultList = Collections.singletonList(
            new Object[] {userId, userFullName}
        );
        when(query.getResultList()).thenReturn(resultList);
        when(
            databaseContext.deserializeResultList(
                resultList,
                UserIdFullNameResult.class
            )
        ).thenReturn(Collections.singletonList(result));
        when(entityManager.createQuery(queryString)).thenReturn(query);

        // when
        int skip = RandomUtil.randomInt();
        int limit = RandomUtil.randomInt();
        String email = RandomUtil.randomShortAlphabetString();
        List<UserIdFullNameResult> actual = sut.fetchListByQueryString(
            queryString,
            Collections.singletonMap("email", email),
            skip,
            limit
        );

        // then
        Asserts.assertEquals(
            actual,
            Collections.singletonList(result),
            false
        );
        verify(entityManager, times(1)).createQuery(queryString);
        verify(query, times(1)).setParameter("email", email);
        verify(query, times(1)).setFirstResult(skip);
        verify(query, times(1)).setMaxResults(limit);
        verify(query, times(1)).getResultList();
        verify(databaseContext, times(1)).deserializeResultList(
            resultList,
            UserIdFullNameResult.class
        );
    }

    @SuppressWarnings("rawtypes")
    public static class InternalRepo extends EzyJpaRepository {}
}
