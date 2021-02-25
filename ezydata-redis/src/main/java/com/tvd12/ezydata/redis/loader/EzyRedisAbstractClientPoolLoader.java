package com.tvd12.ezydata.redis.loader;

import com.tvd12.ezydata.database.util.EzyDatabasePropertiesKeeper;
import com.tvd12.ezydata.redis.EzyRedisClientPool;

public abstract class EzyRedisAbstractClientPoolLoader
	extends EzyDatabasePropertiesKeeper<EzyRedisAbstractClientPoolLoader> 
	implements EzyRedisClientPoolLoader {
	
	protected String host;
	protected int port;
	protected String uri;
	
	public EzyRedisAbstractClientPoolLoader host(String host) {
		this.host = host;
		return this;
	}
	
	public EzyRedisAbstractClientPoolLoader port(int port) {
		this.port = port;
		return this;
	}
	
	public EzyRedisAbstractClientPoolLoader uri(String uri) {
		this.uri = uri;
		return this;
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
