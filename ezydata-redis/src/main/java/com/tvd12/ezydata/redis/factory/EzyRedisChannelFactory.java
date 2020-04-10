package com.tvd12.ezydata.redis.factory;

import com.tvd12.ezydata.redis.EzyRedisChannel;
import com.tvd12.ezydata.redis.EzyRedisClient;
import com.tvd12.ezydata.redis.setting.EzyRedisChannelSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class EzyRedisChannelFactory {

	protected final EzyRedisSettings settings;
	protected final EzyRedisClient redisClient;
	protected final EzyEntityCodec entityCodec;
	
	protected EzyRedisChannelFactory(Builder builder) {
		this.settings = builder.settings;
		this.redisClient = builder.redisClient;
		this.entityCodec = builder.entityCodec;
	}
	
	@SuppressWarnings("unchecked")
	public <T> EzyRedisChannel<T> newChannel(String name) {
		EzyRedisChannelSetting channelSetting = settings.getChannelSeting(name);
		if(channelSetting == null)
			throw new IllegalArgumentException("has no setting for channel: " + name);
		return EzyRedisChannel.builder()
				.channelName(name)
				.setting(channelSetting)
				.redisClient(redisClient)
				.entityCodec(entityCodec)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisChannelFactory> {
		
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
		public EzyRedisChannelFactory build() {
			return new EzyRedisChannelFactory(this);
		}
		
	}
	
}
