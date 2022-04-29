package com.tvd12.ezydata.database.test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class EzyDatabaseContextTest {

    @Test
    public void closeTest() {
        InternalContext sut = new InternalContext();
        sut.close();
    }


    private static class InternalContext implements EzyDatabaseContext {

        @Override
        public <T> T getRepository(String name) {
            return null;
        }

        @Override
        public <T> T getRepository(Class<T> repoType) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Map<Class, Object> getRepositories() {
            return null;
        }

        @Override
        public Map<String, Object> getRepositoriesByName() {
            return null;
        }

        @Override
        public EzyQueryEntity getQuery(String queryName) {
            return null;
        }

        @Override
        public Object deserializeResult(Object result, Class<?> resultType) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public List deserializeResultList(Object result, Class<?> resultItemType) {
            return null;
        }

    }
}
