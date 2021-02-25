package com.tvd12.ezydata.redis.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.properties.file.util.PropertiesUtil;

public class EzyRedisSettingsBuilder implements EzyBuilder<EzyRedisSettings> {

	protected String atomicLongMapName;
	protected Map<String, EzyRedisMapSetting> mapSettings;
	protected Map<String, EzyRedisChannelSetting> channelSettings;
	protected Map<String, EzyRedisMapSettingBuilder> mapSettingBuilders;
	protected Map<String, EzyRedisChannelSettingBuilder> channelSettingBuilders;
	
	public EzyRedisSettingsBuilder() {
		this.mapSettings = new HashMap<>();
		this.mapSettingBuilders = new HashMap<>();
		this.channelSettings = new HashMap<>();
		this.channelSettingBuilders = new HashMap<>();
		this.atomicLongMapName = "___ezydata.atomic_longs___";
	}
	
	public EzyRedisSettingsBuilder atomicLongMapName(String atomicLongMapName) {
		if(atomicLongMapName != null)
			this.atomicLongMapName = atomicLongMapName;
		return this;
	}
	
	public EzyRedisSettingsBuilder addMapSetting(String mapName, EzyRedisMapSetting setting) {
		this.mapSettings.compute(mapName, (k, v) -> {
			if(v == null)
				return setting;
			return new EzyRedisMapSettingBuilder()
					.keyType(setting.getKeyType())
					.valueType(setting.getValueType())
					.build();
		});
		return this;
	}
	
	public EzyRedisMapSettingBuilder mapSettingBuilder(String mapName) {
		EzyRedisMapSettingBuilder builder = mapSettingBuilders.get(mapName);
		if(builder == null) {
			builder = new EzyRedisMapSettingBuilder(this);
			mapSettingBuilders.put(mapName, builder);
		}
		return builder;
	}
	
	public EzyRedisSettingsBuilder addChannelSetting(String channelName, EzyRedisChannelSetting setting) {
		this.channelSettings.compute(channelName, (k, v) -> {
			if(v == null)
				return setting;
			return new EzyRedisChannelSettingBuilder()
					.messageType(setting.getMessageType())
					.subThreadPoolSize(setting.getSubThreadPoolSize())
					.build();
		});
		return this;
	}
	
	public EzyRedisChannelSettingBuilder channelSettingBuilder(String channelName) {
		EzyRedisChannelSettingBuilder builder = channelSettingBuilders.get(channelName);
		if(builder == null) {
			builder = new EzyRedisChannelSettingBuilder(this);
			channelSettingBuilders.put(channelName, builder);
		}
		return builder;
	}
	
	public EzyRedisSettingsBuilder properties(Properties properties) {
		atomicLongMapName(properties.getProperty(EzyRedisSettings.ATOMIC_LONG_MAP_NAME));
		Map<String, Properties> mapsProperties = 
				PropertiesUtil.getPropertiesMap(
						PropertiesUtil.getPropertiesByPrefix(properties, EzyRedisSettings.MAPS));
		for(String mapName : mapsProperties.keySet()) {
			Properties mapProperties = mapsProperties.get(mapName);	
			mapSettingBuilder((String)mapName)
				.keyType(mapProperties.getProperty(EzyRedisSettings.MAP_KEY_TYPE))
				.valueType(mapProperties.getProperty(EzyRedisSettings.MAP_VALUE_TYPE));
		}
		Map<String, Properties> channelsProperties = 
				PropertiesUtil.getPropertiesMap(
						PropertiesUtil.getPropertiesByPrefix(properties, EzyRedisSettings.CHANNELS));
		for(String channelName : channelsProperties.keySet()) {
			Properties channelProperties = channelsProperties.get(channelName);
			channelSettingBuilder((String)channelName)
				.messageType(channelProperties.getProperty(EzyRedisSettings.CHANNEL_MESSAGE_TYPE))
				.subThreadPoolSize(channelProperties.getProperty(EzyRedisSettings.CHANNEL_THREAD_POOL_SIZE));
		}
		return this;
	}
	
	@Override
	public EzyRedisSettings build() {
		buildMapSettings();
		buildChannelSettings();
		EzyRedisSimpleSettings settings = new EzyRedisSimpleSettings();
		settings.addMapSettings(mapSettings);
		settings.addChannelSettings(channelSettings);
		settings.setAtomicLongMapName(atomicLongMapName);
		return settings;
	}
	
	protected void buildMapSettings() {
		for(String mapName : mapSettingBuilders.keySet()) {
			EzyRedisMapSettingBuilder b = mapSettingBuilders.get(mapName);
			addMapSetting(mapName, b.build());
		}
	}
	
	protected void buildChannelSettings() {
		for(String channelName : channelSettingBuilders.keySet()) {
			EzyRedisChannelSettingBuilder b = channelSettingBuilders.get(channelName);
			addChannelSetting(channelName, b.build());
		}
	}
	
}
