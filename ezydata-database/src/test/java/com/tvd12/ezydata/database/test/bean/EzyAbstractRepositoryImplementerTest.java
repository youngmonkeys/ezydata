package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class EzyAbstractRepositoryImplementerTest {

    @Test
    public void isAutoImplementMethodTest() throws Exception {
        // given
        ExEzyRepositoryImplementer instance = new ExEzyRepositoryImplementer(
            ExRepo.class
        );

        // when
        boolean answer = MethodInvoker.create()
            .object(instance)
            .method("isAutoImplementMethod")
            .param(
                EzyMethod.class,
                new EzyMethod(
                    ExRepo.class.getDeclaredMethod("findAll")
                )
            )
            .invoke(Boolean.class);

        // then
        Asserts.assertFalse(answer);
    }

    public static class ExEzyRepositoryImplementer
        extends EzyAbstractRepositoryImplementer {

        public ExEzyRepositoryImplementer(Class<?> clazz) {
            super(clazz);
        }

        @Override
        protected Class<?> getSuperClass() {
            return EzyDatabaseRepository.class;
        }
    }

    public interface ExRepo
        extends EzyDatabaseRepository<Long, Person> {

        @Override
        default List<Person> findAll() {
            return Collections.emptyList();
        }
    }
}
