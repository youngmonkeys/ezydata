package com.tvd12.ezydata.jpa.test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseJpaTest {

    protected final static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("UsersDB");
    }
}
