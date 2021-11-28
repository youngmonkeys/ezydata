package com.tvd12.ezydata.database.test.bean;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyAbstractRepositoriesImplementerTest {

    @Test
    public void isAutoImplRepoInterfaceTest() {
        // given
        Internal sut = new Internal();
        sut.repositoryInterfaces((EzyReflection)null);
        
        // when
        boolean actual = MethodInvoker.create()
                .object(sut)
                .method("isAutoImplRepoInterface")
                .param(InternalRepo.class)
                .call();
        
        // then
        Asserts.assertFalse(actual);
    }
    
    @EzyRepository
    private static interface InternalRepo extends EzyDatabaseRepository<Long, Person> {}
    
    private static class Internal extends EzyAbstractRepositoriesImplementer {

        @Override
        protected EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf) {
            return null;
        }
    }
}
