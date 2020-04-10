package com.tvd12.ezydata.redis;

import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class EzyRedisProxyFactory {

	protected final EzyRedisSettings settings;
	protected final EzyEntityCodec entityCodec;
	protected final EzyRedisClientPool clientPool;
	
	protected EzyRedisProxyFactory(Builder builder) {
		this.settings = builder.settings;
		this.entityCodec = builder.entityCodec;
		this.clientPool = builder.clientPool;
	}
	
	public EzyRedisProxy newRedisProxy() {
		if(clientPool == null)
			throw new IllegalStateException("must set client pool");
		return newRedisProxy(clientPool.getClient());
	}
	
	public EzyRedisProxy newRedisProxy(EzyRedisClient client) {
		return EzyRedisProxy.builder()
				.settings(settings)
				.redisClient(client)
				.entityCodec(entityCodec)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisProxyFactory> {
		
		protected EzyRedisSettings settings;
		protected EzyEntityCodec entityCodec;
		protected EzyRedisClientPool clientPool;
		
		public Builder settings(EzyRedisSettings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		public Builder clientPool(EzyRedisClientPool clientPool) {
			this.clientPool = clientPool;
			return this;
		}
		
		@Override
		public EzyRedisProxyFactory build() {
			return new EzyRedisProxyFactory(this);
		}
		
	}
	
}
