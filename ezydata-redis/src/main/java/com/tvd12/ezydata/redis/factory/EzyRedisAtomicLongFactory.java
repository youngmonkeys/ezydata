package com.tvd12.ezydata.redis.factory;

import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;

import redis.clients.jedis.Jedis;

public class EzyRedisAtomicLongFactory {

	protected final Jedis jedis;
	protected final EzyRedisSettings settings;
	
	protected EzyRedisAtomicLongFactory(Builder builder) {
		this.jedis = builder.jedis;
		this.settings = builder.settings;
	}
	
	public EzyRedisAtomicLong newAtomicLong(String name) {
		String mapName = settings.getAtomicLongMapName();
		if(EzyStrings.isNoContent(mapName))
			throw new IllegalArgumentException("has no setting for atomic long map name");
		return EzyRedisAtomicLong.builder()
				.name(name)
				.jedis(jedis)
				.mapName(mapName)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisAtomicLongFactory> {
		
		protected Jedis jedis;
		protected EzyRedisSettings settings;
		
		public Builder jedis(Jedis jedis) {
			this.jedis = jedis;
			return this;
		}
		
		public Builder settings(EzyRedisSettings settings) {
			this.settings = settings;
			return this;
		}
		
		@Override
		public EzyRedisAtomicLongFactory build() {
			return new EzyRedisAtomicLongFactory(this);
		}
		
	}
	
}
