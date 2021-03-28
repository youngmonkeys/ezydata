package com.tvd12.ezydata.example.jpa.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.jpa.EzyJpaDatabaseContextBuilder;
import com.tvd12.ezydata.jpa.loader.EzyJpaDataSourceLoader;
import com.tvd12.ezydata.jpa.loader.EzyJpaEntityManagerFactoryLoader;
import com.tvd12.ezyfox.bean.EzyBeanConfig;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfox.util.EzyPropertiesAware;

import lombok.Setter;


@Setter
@EzyConfigurationBefore
public class JpaConfig implements EzyBeanConfig, EzyPropertiesAware {

    private Properties properties;

    @EzyAutoBind
    private EzySingletonFactory singletonFactory;

    @Override
    public void config() {
        EzyDatabaseContext databaseContext = databaseContext();
        databaseContext.getRepositoriesByName().forEach((name, repo) ->
            singletonFactory.addSingleton(name, repo)
        );
    }

    private EzyDatabaseContext databaseContext() {
        return new EzyJpaDatabaseContextBuilder()
            .properties(properties)
            .entityManagerFactory(entityManagerFactory())
            .scan("com.tvd12.ezydata.example.jpa.entity")
            .scan("com.tvd12.ezydata.example.jpa.repository")
            .scan("com.tvd12.ezydata.example.jpa.result")
            .build();
    }

    private EntityManagerFactory entityManagerFactory() {
        return new EzyJpaEntityManagerFactoryLoader()
            .entityPackage("com.tvd12.ezydata.example.jpa.entity")
            .dataSource(dataSource())
            .properties(properties)
            .load("Test");
    }

    private DataSource dataSource() {
        return new EzyJpaDataSourceLoader()
            .properties(properties, "datasource")
            .load();
    }

}