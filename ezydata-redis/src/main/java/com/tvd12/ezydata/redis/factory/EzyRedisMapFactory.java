package com.tvd12.ezydata.redis.factory;

import com.tvd12.ezydata.redis.EzyRedisMap;
import com.tvd12.ezydata.redis.setting.EzyRedisMapSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

import redis.clients.jedis.Jedis;

public class EzyRedisMapFactory {

	protected final Jedis jedis;
	protected final EzyRedisSettings settings;
	protected final EzyEntityCodec entityCodec;
	
	protected EzyRedisMapFactory(Builder builder) {
		this.jedis = builder.jedis;
		this.settings = builder.settings;
		this.entityCodec = builder.entityCodec;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> EzyRedisMap<K, V> newMap(String name) {
		EzyRedisMapSetting mapSetting = settings.getMapSeting(name);
		if(mapSetting == null)
			throw new IllegalArgumentException("has no setting for map: " + name);
		return EzyRedisMap.builder()
				.mapName(name)
				.setting(mapSetting)
				.jedis(jedis)
				.entityCodec(entityCodec)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisMapFactory> {
		
		protected Jedis jedis;
		protected EzyRedisSettings settings;
		protected EzyEntityCodec entityCodec;
		
		public Builder jedis(Jedis jedis) {
			this.jedis = jedis;
			return this;
		}
		
		public Builder settings(EzyRedisSettings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		@Override
		public EzyRedisMapFactory build() {
			return new EzyRedisMapFactory(this);
		}
		
	}
	
}
