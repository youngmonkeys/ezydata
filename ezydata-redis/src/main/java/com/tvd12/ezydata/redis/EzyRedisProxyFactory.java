package com.tvd12.ezydata.redis;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Id;

import com.tvd12.ezydata.database.annotation.EzyCachedKey;
import com.tvd12.ezydata.database.annotation.EzyCachedValue;
import com.tvd12.ezydata.database.util.EzyCachedValueAnnotations;
import com.tvd12.ezydata.redis.annotation.EzyRedisMessage;
import com.tvd12.ezydata.redis.loader.EzyJedisClientPoolLoader;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;
import com.tvd12.ezydata.redis.util.EzyRedisMessageAnnotations;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.codec.EzyBindingEntityCodec;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;

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
		return EzyRedisProxy.builder()
				.settings(settings)
				.redisClient(clientPool.getClient())
				.entityCodec(entityCodec)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisProxyFactory> {

		protected EzyReflection reflection;
		protected Set<String> packagesToScan;
		protected Properties properties;
		protected EzyRedisSettings settings;
		protected EzyEntityCodec entityCodec;
		protected EzyRedisClientPool clientPool;
		protected EzyRedisSettingsBuilder settingsBuilder;
		
		public Builder() {
			this.packagesToScan = new HashSet<>();
		}
		
		public Builder scan(String packageName) {
			packagesToScan.add(packageName);
			return this;
		}
		
		public Builder scan(String... packageNames) {
			return scan(Sets.newHashSet(packageNames));
		}
		
		public Builder scan(Iterable<String> packageNames) {
			for(String packageName : packageNames)
				this.scan(packageName);
			return this;	
		}
		
		public Builder properties(Properties properties) {
			this.properties = properties;
			return this;
		}
		
		public Builder settings(EzyRedisSettings settings) {
			this.settings = settings;
			return this;
		}
		
		public Builder settingsBuilder(EzyRedisSettingsBuilder settingsBuilder) {
			this.settingsBuilder = settingsBuilder;
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
			if(packagesToScan.size() > 0)
				this.reflection = new EzyReflectionProxy(packagesToScan);
			this.prepareSettings();
			this.prepareEntityCodec();
			this.prepareClientPool();
			return new EzyRedisProxyFactory(this);
		}
		
		private void prepareSettings() {
			if(settings != null)
				return;
			if(settingsBuilder == null)
				settingsBuilder = new EzyRedisSettingsBuilder();
			settingsBuilder.properties(properties);
			if(reflection != null) {
				Set<Class<?>> cachedClasses = reflection.getAnnotatedClasses(EzyCachedValue.class);
				for(Class<?> cachedClass : cachedClasses) {
					EzyClass clazz = new EzyClass(cachedClass);
					EzyField idField = clazz.getField(f ->
						f.isAnnotated(EzyCachedKey.class) ||
						f.isAnnotated(EzyId.class) ||
						f.isAnnotated(Id.class)
					)
						.orElseThrow(() -> new IllegalArgumentException(
							"unknow key type of cached value type: " + cachedClass.getName() +
							", annotate key field with @EzyCachedKey or @EzyId or @Id"
						));
					String mapName = EzyCachedValueAnnotations.getMapName(cachedClass);
					settingsBuilder.mapSettingBuilder(mapName)
						.keyType(idField.getType())
						.valueType(cachedClass);
				}
				Set<Class<?>> messageClasses = reflection.getAnnotatedClasses(EzyRedisMessage.class);
				for(Class<?> messageClass : messageClasses) {
					String channelName = EzyRedisMessageAnnotations.getChannelName(messageClass);
					settingsBuilder.channelSettingBuilder(channelName)
						.messageType(messageClass);
				}
			}
			this.settings = settingsBuilder.build();
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void prepareEntityCodec() {
			if(entityCodec != null)
				return;
			EzyBindingContext bindingContext = EzyBindingContext.builder()
					.addAllClasses(reflection)
					.addClasses((Set)reflection.getAnnotatedClasses(EzyCachedValue.class))
					.build();
			entityCodec = EzyBindingEntityCodec.builder()
					.marshaller(bindingContext.newMarshaller())
					.unmarshaller(bindingContext.newUnmarshaller())
					.messageSerializer(new MsgPackSimpleSerializer())
					.messageDeserializer(new MsgPackSimpleDeserializer())
					.build();
		}
		
		private void prepareClientPool() {
			if(clientPool != null)
				return;
			clientPool = new EzyJedisClientPoolLoader()
					.properties(properties)
					.load();
		}
		
	}
	
}
