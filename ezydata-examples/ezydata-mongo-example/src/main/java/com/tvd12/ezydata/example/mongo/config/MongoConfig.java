package com.tvd12.ezydata.example.mongo.config;

import java.util.Properties;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContext;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseContextBuilder;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezyfox.bean.EzyBeanConfig;
import com.tvd12.ezyfox.bean.EzySingletonFactory;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfox.bean.annotation.EzyPropertiesBean;
import com.tvd12.ezyfox.util.EzyPropertiesAware;

import lombok.Setter;

@Setter
@EzyConfigurationBefore
@EzyPropertiesBean(value = MongoProperties.class, prefix = "database.mongo")
public class MongoConfig implements EzyBeanConfig, EzyPropertiesAware {

    @EzyAutoBind
    private MongoProperties mongoProperties;

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

    private EzyMongoDatabaseContext databaseContext() {
        return new EzyMongoDatabaseContextBuilder()
            .databaseName(mongoProperties.getDatabase())
            .mongoClient(mongoClient())
            .properties(properties)
            .scan("com.tvd12.kotlin.examples.mongo.entity")
            .scan("com.tvd12.kotlin.examples.mongo.repository")
            .scan("com.tvd12.kotlin.examples.mongo.result")
            .build();
    }

    private MongoClient mongoClient() {
        return new EzySimpleMongoClientLoader()
            .uri(mongoProperties.getUri())
            .load();
    }

}