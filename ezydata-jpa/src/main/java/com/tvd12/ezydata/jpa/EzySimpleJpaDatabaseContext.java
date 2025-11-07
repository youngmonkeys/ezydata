package com.tvd12.ezydata.jpa;

import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Setter
public class EzySimpleJpaDatabaseContext
    extends EzySimpleDatabaseContext
    implements EzyJpaDatabaseContext {

    protected EntityManagerFactory entityManagerFactory;

    @Override
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }
}
