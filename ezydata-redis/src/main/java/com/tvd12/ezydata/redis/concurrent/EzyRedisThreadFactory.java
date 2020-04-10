package com.tvd12.ezydata.redis.concurrent;

import com.tvd12.ezyfox.concurrent.EzyThreadFactory;

public class EzyRedisThreadFactory extends EzyThreadFactory {
	
	protected EzyRedisThreadFactory(Builder builder) {
		super(builder);
	}
	
	public static EzyThreadFactory create(String poolName) {
		return builder().poolName(poolName).build();
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyThreadFactory.Builder {
		
		protected Builder() {
			super();
			this.prefix = "ezydata-redis";
		}
		
		@Override
		public EzyThreadFactory build() {
			return new EzyRedisThreadFactory(this);
		}
		
	}
	
}
