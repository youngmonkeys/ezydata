package com.tvd12.ezydata.redis.loader;

import com.tvd12.ezydata.database.util.EzyDatabasePropertiesKeeper;
import com.tvd12.ezydata.redis.EzyRedisClientPool;

@SuppressWarnings("unchecked")
public abstract class EzyRedisAbstractClientPoolLoader<T extends EzyRedisAbstractClientPoolLoader<T>>
    extends EzyDatabasePropertiesKeeper<T> 
    implements EzyRedisClientPoolLoader {
    
    protected String host;
    protected int port;
    protected String uri;
    
    public T host(String host) {
        this.host = host;
        return (T)this;
    }
    
    public T port(int port) {
        this.port = port;
        return (T)this;
    }
    
    public T uri(String uri) {
        this.uri = uri;
        return (T)this;
    }
    
    @Override
    public final EzyRedisClientPool load() {
        this.preLoad();
        return doLoad();
    }
    
    private void preLoad() {
        if(properties.containsKey(URI))
            this.uri = properties.getProperty(URI);
        if(properties.containsKey(HOST))
            this.host = properties.getProperty(HOST);
        if(properties.containsKey(PORT))
            this.port = Integer.parseInt(properties.getProperty(PORT));
    }
    
    protected abstract EzyRedisClientPool doLoad();
    
}
