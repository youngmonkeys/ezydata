package com.tvd12.ezydata.database.test.bean;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.test.base.BaseTest;

public class EzySimpleRepositoryImplementerTest extends BaseTest {

    @Test(expectedExceptions = IllegalStateException.class)
    public void test() {
        ExEzySimpleRepositoryImplementer implementer = new ExEzySimpleRepositoryImplementer(PersonRepo.class);
        implementer.implement(new MongoTemplate());
    }

    public static class ExEzySimpleRepositoryImplementer extends EzyAbstractRepositoryImplementer {

        public ExEzySimpleRepositoryImplementer(Class<?> clazz) {
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
