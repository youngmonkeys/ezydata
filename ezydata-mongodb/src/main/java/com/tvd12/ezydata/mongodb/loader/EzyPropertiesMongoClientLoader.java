package com.tvd12.ezydata.mongodb.loader;

import com.mongodb.*;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.Map;
import java.util.Properties;

public class EzyPropertiesMongoClientLoader
    extends EzyLoggable
    implements EzyMongoClientLoader {

    protected Properties properties;

    public EzyPropertiesMongoClientLoader() {
        this.properties = new Properties();
    }

    public static MongoClient load(Properties properties) {
        return new EzyPropertiesMongoClientLoader()
            .properties(properties)
            .load();
    }

    public EzyPropertiesMongoClientLoader host(String host) {
        this.properties.put(EzyMongoClientLoader.HOST, host);
        return this;
    }

    public EzyPropertiesMongoClientLoader port(int port) {
        this.properties.put(EzyMongoClientLoader.PORT, port);
        return this;
    }

    public EzyPropertiesMongoClientLoader username(String username) {
        this.properties.put(EzyMongoClientLoader.USERNAME, username);
        return this;
    }

    public EzyPropertiesMongoClientLoader password(String password) {
        this.properties.put(EzyMongoClientLoader.PASSWORD, password);
        return this;
    }

    public EzyPropertiesMongoClientLoader uri(String uri) {
        this.properties.put(EzyMongoClientLoader.URI, uri);
        return this;
    }

    @SuppressWarnings({"rawtypes"})
    public EzyPropertiesMongoClientLoader properties(Map map) {
        this.properties.putAll(map);
        return this;
    }

    public EzyPropertiesMongoClientLoader property(String name, Object value) {
        this.properties.put(name, value);
        return this;
    }

    @Override
    public MongoClient load() {
        this.preload();
        return this.createMongoClient();
    }

    protected void preload() {}

    protected MongoClient createMongoClient() {
        String uri = properties.getProperty(URI);
        if (uri != null) {
            return new MongoClient(new MongoClientURI(uri));
        }

        return new MongoClient(
            new ServerAddress(getHost(), getPort()),
            createCredential(),
            MongoClientOptions.builder().build());
    }

    protected MongoCredential createCredential() {
        return MongoCredential.createCredential(
            getUsername(),
            getDatabase(),
            getPassword().toCharArray());
    }

    protected String getHost() {
        return (String) properties.get(EzyMongoClientLoader.HOST);
    }

    protected int getPort() {
        return Integer.parseInt((String) properties.get(EzyMongoClientLoader.PORT));
    }

    protected String getUsername() {
        return (String) properties.get(EzyMongoClientLoader.USERNAME);
    }

    protected String getPassword() {
        return (String) properties.get(EzyMongoClientLoader.PASSWORD);
    }

    protected String getDatabase() {
        return (String) properties.get(EzyMongoClientLoader.DATABASE);
    }
}
