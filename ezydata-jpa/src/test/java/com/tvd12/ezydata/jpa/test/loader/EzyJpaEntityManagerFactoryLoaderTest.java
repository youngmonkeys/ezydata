package com.tvd12.ezydata.jpa.test.loader;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.testng.annotations.Test;

import com.tvd12.ezydata.jpa.loader.EzyJpaEntityManagerFactoryLoader;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.reflect.MethodUtil;

public class EzyJpaEntityManagerFactoryLoaderTest {

    @Test
    public void test() {
        // given
        String jpaVersion = "2.2";
        PersistenceUnitTransactionType transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
        String mappingFileName = "test-mapping-file.xml";
        String entityPacakge = "com.tvd12.ezydata.jpa.test.entity";
        EzyJpaEntityManagerFactoryLoader sut = new EzyJpaEntityManagerFactoryLoader()
            .jpaVersion(jpaVersion)
            .transactionType(transactionType)
            .mappingFileName(Arrays.asList(mappingFileName))
            .entityPackages(Arrays.asList(entityPacakge));
        
        // when
        // then
        Asserts.assertEquals(FieldUtil.getFieldValue(sut, "jpaVersion"), jpaVersion);
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "transactionType"),
            PersistenceUnitTransactionType.RESOURCE_LOCAL
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "mappingFileNames"),
            Arrays.asList(mappingFileName),
            false
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "entityPackages"),
            Arrays.asList(entityPacakge),
            false
        );
    }
    
    @Test
    public void doLoadByDefaultJpa() {
        // given
        EzyJpaEntityManagerFactoryLoader sut = new EzyJpaEntityManagerFactoryLoader();
        
        // when
        EntityManagerFactory load = sut.load("UsersDB");
        
        // then
        Asserts.assertNotNull(load);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void PersistenceUnitInfoImplTest() throws Exception {
        // given
        Class clazz = Class.forName(
            EzyJpaEntityManagerFactoryLoader.class.getName() + "$PersistenceUnitInfoImpl");
        
        Constructor constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        
        EzyJpaEntityManagerFactoryLoader loader = new EzyJpaEntityManagerFactoryLoader();
        
        Object sut = constructor.newInstance(loader, "Test");
        
        // when
        String persistenceProviderClassName = MethodUtil.invokeMethod(
            "getPersistenceProviderClassName",
            sut
        );
        String persistenceXMLSchemaVersion = MethodUtil.invokeMethod(
            "getPersistenceXMLSchemaVersion",
            sut
        );
        ClassTransformer classTransformer = mock(ClassTransformer.class);
        MethodInvoker.create()
            .object(sut)
            .method("addTransformer")
            .param(ClassTransformer.class, classTransformer)
            .invoke();
        
        // then
        Asserts.assertEquals(persistenceProviderClassName, HibernatePersistenceProvider.class.getName());
        Asserts.assertEquals(persistenceXMLSchemaVersion, "2.2");
    }
}
