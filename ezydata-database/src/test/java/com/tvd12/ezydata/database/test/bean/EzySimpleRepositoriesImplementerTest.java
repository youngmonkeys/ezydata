package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepositoryWrapper;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.bean.EzyRepositoriesImplementer;
import com.tvd12.ezydata.database.query.EzyQueryRegister;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class EzySimpleRepositoriesImplementerTest extends BaseTest {

    @Test
    public void test() {
        EzyAbstractRepositoryImplementer.setDebug(true);
        EzyRepositoriesImplementer implementer = new ExEzySimpleRepositoriesImplementer()
            .scan("com.tvd12.ezydata.database.test.bean")
            .scan("com.tvd12.ezydata.database.test.bean", "com.tvd12.ezydata.database.test.bean")
            .scan(Sets.newHashSet("com.tvd12.ezydata.database.test.bean"))
            .repositoryInterface(PersonRepo2.class)
            .repositoryInterface(Class.class)
            .repositoryInterface(NothingInterface.class)
            .repositoryInterfaces(PersonRepo2.class, PersonRepo2.class)
            .repositoryInterfaces(Sets.newHashSet(PersonRepo2.class));

        MongoTemplate template = new MongoTemplate();
        Map<Class<?>, Object> repos = implementer.implement(template);
        System.out.println("repos: " + repos);
        assert repos.size() == 4;

        implementer = new ExEzySimpleRepositoriesImplementer();
        repos = implementer.implement(template);
        assert repos.isEmpty();
    }

    @Test
    public void setPropertiesTest() {
        // given
        EzyQueryRegister queryManager = mock(EzyQueryRegister.class);
        EzyDatabaseRepositoryWrapper repositoryWrapper = mock(EzyDatabaseRepositoryWrapper.class);
        EzyReflection reflection = new EzyReflectionProxy("com.tvd12.ezydata.database.test.bean");
        EzyRepositoriesImplementer implementer = new ExEzySimpleRepositoriesImplementer()
            .repositoryInterfaces(reflection)
            .queryManager(queryManager)
            .repositoryWrapper(repositoryWrapper);

        // when
        // then
        Asserts.assertEquals(
            FieldUtil.getFieldValue(implementer, "queryManager"),
            queryManager
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(implementer, "repositoryWrapper"),
            repositoryWrapper
        );
        Asserts.assertEquals(
            FieldUtil.getFieldValue(implementer, "reflections"),
            Collections.singletonList(reflection),
            false
        );
    }

    public static class ExEzySimpleRepositoriesImplementer
        extends EzyAbstractRepositoriesImplementer {

        @Override
        protected EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf) {
            return new ExEzySimpleRepositoryImplementer(itf);
        }
    }

    public static class ExEzySimpleRepositoryImplementer
        extends EzyAbstractRepositoryImplementer {

        public ExEzySimpleRepositoryImplementer(Class<?> clazz) {
            super(clazz);
        }

        @Override
        protected void setRepoComponent(Object repo, Object template) {}

        @Override
        protected Class<?> getSuperClass() {
            return ExEzyDatabaseRepository.class;
        }
    }
}
