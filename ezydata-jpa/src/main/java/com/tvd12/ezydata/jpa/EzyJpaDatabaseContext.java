package com.tvd12.ezydata.jpa;

import com.tvd12.ezydata.database.EzyDatabaseContext;

import javax.persistence.EntityManager;

public interface EzyJpaDatabaseContext extends EzyDatabaseContext {

    EntityManager createEntityManager();
}
