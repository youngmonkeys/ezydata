package com.tvd12.ezydata.redis;

import com.tvd12.ezydata.redis.factory.EzyRedisAtomicLongFactory;
import com.tvd12.ezydata.redis.factory.EzyRedisMapFactory;
import com.tvd12.ezydata.redis.manager.EzyRedisAtomicLongProvider;
import com.tvd12.ezydata.redis.manager.EzyRedisMapProvider;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

import redis.clients.jedis.Jedis;

public class EzyRedisProxy {

	protected final Jedis jedis;
	protected final EzyRedisSettings settings;
	protected final EzyEntityCodec entityCodec;
	protected final EzyRedisMapFactory mapFactory;
	protected final EzyRedisMapProvider mapProvider;
	protected final EzyRedisAtomicLongFactory atomicLongFactory;
	protected final EzyRedisAtomicLongProvider atomicLongProvider;
	
	public EzyRedisProxy(Builder builder) {
		this.jedis = builder.jedis;
		this.settings = builder.settings;
		this.entityCodec = builder.entityCodec;
		this.mapFactory = newMapFactory();
		this.mapProvider = newMapProvider();
		this.atomicLongFactory = newAtomicLongFactory();
		this.atomicLongProvider = newAtomicLongProvider();
	}
	
	protected EzyRedisMapFactory newMapFactory() {
		return EzyRedisMapFactory.builder()
				.jedis(jedis)
				.settings(settings)
				.entityCodec(entityCodec)
				.build();
	}
	
	protected EzyRedisMapProvider newMapProvider() {
		return EzyRedisMapProvider.builder()
				.mapFactory(mapFactory)
				.build();
	}
	
	protected EzyRedisAtomicLongFactory newAtomicLongFactory() {
		return EzyRedisAtomicLongFactory.builder()
				.jedis(jedis)
				.settings(settings)
				.build();
	}
	
	protected EzyRedisAtomicLongProvider newAtomicLongProvider() {
		return EzyRedisAtomicLongProvider.builder()
				.atomicLongFactory(atomicLongFactory)
				.build();
	}
	
	public <K,V> EzyRedisMap<K, V> getMap(String name) {
		return mapProvider.getMap(name);
	}
	
	public EzyRedisAtomicLong getAtomicLong(String name) {
		return atomicLongProvider.getAtomicLong(name);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisProxy> {
		
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
		
		@Override
		public EzyRedisProxy build() {
			return new EzyRedisProxy(this);
		}
		
	}
	
}
