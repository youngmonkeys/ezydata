package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleRepositoryImplementerTest extends BaseTest {

    @Test(expectedExceptions = IllegalStateException.class)
    public void test() {
        ExEzySimpleRepositoryImplement implementer = new ExEzySimpleRepositoryImplement(PersonRepo.class);
        implementer.implement(new MongoTemplate());
    }

    public static class ExEzySimpleRepositoryImplement extends EzyAbstractRepositoryImplementer {

        public ExEzySimpleRepositoryImplement(Class<?> clazz) {
            super(clazz);
        }

        @Override
        protected void setRepoComponent(Object repo, Object template) {
        }

        @Override
        protected Class<?> getSuperClass() {
            throw new IllegalStateException("has no super class");
        }

    }

}
