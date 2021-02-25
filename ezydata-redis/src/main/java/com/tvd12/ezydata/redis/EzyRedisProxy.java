package com.tvd12.ezydata.redis;

import com.tvd12.ezydata.redis.factory.EzyRedisAtomicLongFactory;
import com.tvd12.ezydata.redis.factory.EzyRedisChannelFactory;
import com.tvd12.ezydata.redis.factory.EzyRedisMapFactory;
import com.tvd12.ezydata.redis.manager.EzyRedisAtomicLongProvider;
import com.tvd12.ezydata.redis.manager.EzyRedisChannelProvider;
import com.tvd12.ezydata.redis.manager.EzyRedisMapProvider;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class EzyRedisProxy {

	protected final EzyRedisSettings settings;
	protected final EzyRedisClient redisClient;
	protected final EzyEntityCodec entityCodec;
	protected final EzyRedisMapFactory mapFactory;
	protected final EzyRedisMapProvider mapProvider;
	protected final EzyRedisChannelFactory channelFactory;
	protected final EzyRedisChannelProvider channelProvider;
	protected final EzyRedisAtomicLongFactory atomicLongFactory;
	protected final EzyRedisAtomicLongProvider atomicLongProvider;
	
	protected EzyRedisProxy(Builder builder) {
		this.settings = builder.settings;
		this.redisClient = builder.redisClient;
		this.entityCodec = builder.entityCodec;
		this.mapFactory = newMapFactory();
		this.mapProvider = newMapProvider();
		this.channelFactory = newChannelFactory();
		this.channelProvider = newChannelProvider();
		this.atomicLongFactory = newAtomicLongFactory();
		this.atomicLongProvider = newAtomicLongProvider();
	}
	
	protected EzyRedisMapFactory newMapFactory() {
		return EzyRedisMapFactory.builder()
				.settings(settings)
				.redisClient(redisClient)
				.entityCodec(entityCodec)
				.build();
	}
	
	protected EzyRedisMapProvider newMapProvider() {
		return EzyRedisMapProvider.builder()
				.mapFactory(mapFactory)
				.build();
	}
	
	protected EzyRedisChannelFactory newChannelFactory() {
		return EzyRedisChannelFactory.builder()
				.settings(settings)
				.redisClient(redisClient)
				.entityCodec(entityCodec)
				.build();
	}
	
	protected EzyRedisChannelProvider newChannelProvider() {
		return EzyRedisChannelProvider.builder()
				.channelFactory(channelFactory)
				.build();
	}
	
	protected EzyRedisAtomicLongFactory newAtomicLongFactory() {
		return EzyRedisAtomicLongFactory.builder()
				.settings(settings)
				.redisClient(redisClient)
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
	
	public <K,V> EzyRedisMap<K, V> getMap(
			String name, Class<K> keyType, Class<V> valueType) {
		return mapProvider.getMap(name, keyType, valueType);
	}
	
	public <T> EzyRedisChannel<T> getChannel(String name) {
		return channelProvider.getChannel(name);
	}
	
	public <T> EzyRedisChannel<T> getChannel(String name, Class<T> messageType) {
		return channelProvider.getChannel(name, messageType);
	}
	
	public EzyRedisAtomicLong getAtomicLong(String name) {
		return atomicLongProvider.getAtomicLong(name);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisProxy> {
		
		protected EzyRedisSettings settings;
		protected EzyRedisClient redisClient;
		protected EzyEntityCodec entityCodec;
		
		public Builder settings(EzyRedisSettings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder redisClient(EzyRedisClient redisClient) {
			this.redisClient = redisClient;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		@Override
		public EzyRedisProxy build() {
			return new EzyRedisProxy(this);
		}
		
	}
	
}
