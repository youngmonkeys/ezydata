package com.tvd12.ezydata.database.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.EzyDatabaseRepositoryWrapper;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzyQueryRegister;
import com.tvd12.ezydata.database.query.EzyQueryString;
import com.tvd12.ezydata.database.query.EzySimpleQueryManager;
import com.tvd12.ezydata.database.test.bean.Person;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyAbstractRepositoryImplementerTest {

    @Test
    public void test() {
        EzySimpleQueryManager queryManager = new EzySimpleQueryManager();
        queryManager.addQuery(EzyQueryEntity.builder()
                .name("test")
                .value("select e from E e")
                .resultType(Person.class)
                .build());
        EzyClass clazz = new EzyClass(RepoA.class);
        Impl impl = new Impl(clazz);
        impl.setQueryManager(queryManager);
        impl.getQueryString(clazz.getMethod("find1"));
        impl.getQueryString(clazz.getMethod("find2"));
        try {
            impl.getQueryString(clazz.getMethod("find3"));
        }
        catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            impl.getQueryString(clazz.getMethod("find4"));
        }
        catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    @Test
    public void doimplementWithoutEzyDatabaseContext() {
        // given
        EzyClass clazz = new EzyClass(RepoA.class);

        EzyDatabaseRepositoryWrapper repositoryWrapper = mock(EzyDatabaseRepositoryWrapper.class);
        when(repositoryWrapper.wrap(any())).thenAnswer(it ->
            it.getArguments()[0]
        );

        Impl impl = new Impl(clazz);
        impl.setRepositoryWrapper(repositoryWrapper);

        EzyAbstractRepositoryImplementer.setDebug(true);

        // when
        Object actual = impl.implement(new Object());

        // then
        Asserts.assertNotNull(actual);
        EzyAbstractRepositoryImplementer.setDebug(false);
    }

    @Test
    public void doimplementWithoutEzyDatabaseContextAware() {
        // given
        EzyClass clazz = new EzyClass(RepoA.class);
        
        EzyDatabaseRepositoryWrapper repositoryWrapper = mock(EzyDatabaseRepositoryWrapper.class);
        when(repositoryWrapper.wrap(any())).thenAnswer(it ->
            it.getArguments()[0]
        );      
        
        Impl2 impl = new Impl2(clazz);
        impl.setRepositoryWrapper(repositoryWrapper);
        
        EzyDatabaseContext databaseContext = mock(EzyDatabaseContext.class);
        
        // when
        Object actual = impl.implement(databaseContext);
        
        // then
        Asserts.assertNotNull(actual);
    }

    @Test
    public void registerQueryNull() {
        // given
        EzyClass clazz = new EzyClass(RepoA.class);
        
        EzyDatabaseRepositoryWrapper repositoryWrapper = mock(EzyDatabaseRepositoryWrapper.class);
        when(repositoryWrapper.wrap(any())).thenAnswer(it ->
            it.getArguments()[0]
        );
        
        EzyQueryRegister queryManager = mock(EzyQueryRegister.class);
        
        Impl impl = new Impl(clazz);
        impl.setRepositoryWrapper(repositoryWrapper);
        impl.setQueryManager(queryManager);
        
        // when
        Object actual = impl.implement(new Object());
        
        // then
        Asserts.assertNotNull(actual);
    }

    @Test
    public void getQueryStringWithoutAnnotationTest() {
        // given
        EzyClass clazz = new EzyClass(RepoA.class);
        
        EzyDatabaseRepositoryWrapper repositoryWrapper = mock(EzyDatabaseRepositoryWrapper.class);
        when(repositoryWrapper.wrap(any())).thenAnswer(it ->
            it.getArguments()[0]
        );
        
        EzyQueryRegister queryManager = mock(EzyQueryRegister.class);
        
        String queryString = "select e from Person e where e.name = ?0";
        EzyQueryMethodConverter queryMethodConverter = mock(EzyQueryMethodConverter.class);
        when(queryMethodConverter.toQueryString(any(), any())).thenReturn(queryString);
        
        
        Impl impl = new Impl(clazz);
        impl.setRepositoryWrapper(repositoryWrapper);
        impl.setQueryManager(queryManager);
        impl.setQueryMethodConverter(queryMethodConverter);
        
        EzyMethod method = getMethod("findByName", String.class);
        
        // when
        EzyQueryString actual = MethodInvoker.create()
                .object(impl)
                .method("getQueryString")
                .param(method)
                .invoke(EzyQueryString.class);
        
        // then
        Asserts.assertEquals(actual.getQueryString(), queryString);
    }

    @Test
    public void isPaginationMethodTest() {
        // given
        EzyClass clazz = new EzyClass(RepoA.class);
        
        Impl impl = new Impl(clazz);
        
        EzyMethod method = getMethod("findByName", String.class);
        
        // when
        boolean actual = MethodInvoker.create()
                .object(impl)
                .method("isPaginationMethod")
                .param(method)
                .invoke(Boolean.class);
        
        // then
        Asserts.assertFalse(actual);
    }

    @Test
    public void getResultTypeTest() {
        // given
        EzyClass clazz = new EzyClass(RepoB.class);
        
        Impl impl = new Impl(clazz);
        
        EzyMethod method = getMethod(RepoB.class, "findListByName", String.class);
        
        // when
        Class<?> actual = MethodInvoker.create()
                .object(impl)
                .method("getResultType")
                .param(method)
                .invoke(Class.class);
        
        // then
        Asserts.assertEquals(actual, Person.class);
    }

    @Test
    public void getResultTypeOneTest() {
        // given
        EzyClass clazz = new EzyClass(RepoB.class);
        
        Impl impl = new Impl(clazz);
        
        EzyMethod method = getMethod(RepoB.class, "findByName", String.class);
        
        // when
        Class<?> actual = MethodInvoker.create()
                .object(impl)
                .method("getResultType")
                .param(method)
                .invoke(Class.class);
        
        // then
        Asserts.assertEquals(actual, Person.class);
    }

    @Test
    public void getResultTypeFromAnnotationTest() {
        // given
        EzyClass clazz = new EzyClass(RepoB.class);
        
        Impl impl = new Impl(clazz);
        
        EzyMethod method = getMethod(RepoB.class, "findPersonByName", String.class);
        
        // when
        Class<?> actual = MethodInvoker.create()
                .object(impl)
                .method("getResultType")
                .param(method)
                .invoke(Class.class);
        
        // then
        Asserts.assertEquals(actual, Person.class);
    }

    @Test
    public void getResultTypeSuccessButObjectTest() {
        // given
        EzyClass clazz = new EzyClass(RepoB.class);
        
        Impl impl = new Impl(clazz);
        
        EzyMethod method = getMethod(RepoB.class, "findPersonListByName", String.class);
        
        // when
        Class<?> actual = MethodInvoker.create()
                .object(impl)
                .method("getResultType")
                .param(method)
                .invoke(Class.class);
        
        // then
        Asserts.assertEquals(actual, List.class);
    }

    private EzyMethod getMethod(String name, Class<?>... parameters) {
        return getMethod(RepoA.class, name, parameters);
    }

    private EzyMethod getMethod(Class<?> repoClass, String name, Class<?>... parameters) {
        try {
            return new EzyMethod(repoClass.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static interface RepoA extends EzyDatabaseRepository<Integer, Person> {

        @EzyQuery("select e from E e")
        void find1();

        @EzyQuery(name = "test")
        void find2();

        @EzyQuery
        void find3();

        @EzyQuery(name = "test no one")
        void find4();

        void helloWorld();

        void findByName(String name);
    }

    public static interface RepoB extends EzyDatabaseRepository<Integer, Person> {
        
        Person findByName(String name);
        
        List<Person> findListByName(String name);
        
        @EzyQuery(resultType = Person.class)
        Person findPersonByName(String name);
        
        List<?> findPersonListByName(String name);
    }

    public static class Impl extends EzyAbstractRepositoryImplementer {

        public Impl(EzyClass clazz) {
            super(clazz);
        }

        @Override
        protected Class<?> getSuperClass() {
            return DbRepository.class;
        }

        @Override
        protected EzyQueryString getQueryString(EzyMethod method) {
            return super.getQueryString(method);
        }
    }

    public static class Impl2 extends EzyAbstractRepositoryImplementer {

        public Impl2(EzyClass clazz) {
            super(clazz);
        }

        @Override
        protected Class<?> getSuperClass() {
            return BaseDbRepository.class;
        }
        
        @Override
        protected EzyQueryString getQueryString(EzyMethod method) {
            return super.getQueryString(method);
        }
    }
}
