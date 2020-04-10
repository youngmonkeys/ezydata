package com.tvd12.ezydata.redis.setting;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.builder.EzyBuilder;

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
	
	public void addMapSetting(String mapName, EzyRedisMapSetting setting) {
		this.mapSettings.put(mapName, setting);
	}
	
	public EzyRedisMapSettingBuilder mapSettingBuilder(String mapName) {
		EzyRedisMapSettingBuilder builder = mapSettingBuilders.get(mapName);
		if(builder == null) {
			builder = new EzyRedisMapSettingBuilder(this);
			mapSettingBuilders.put(mapName, builder);
		}
		return builder;
	}
	
	public void addChannelSetting(String channelName, EzyRedisChannelSetting setting) {
		this.channelSettings.put(channelName, setting);
	}
	
	public EzyRedisChannelSettingBuilder channelSettingBuilder(String channelName) {
		EzyRedisChannelSettingBuilder builder = channelSettingBuilders.get(channelName);
		if(builder == null) {
			builder = new EzyRedisChannelSettingBuilder(this);
			channelSettingBuilders.put(channelName, builder);
		}
		return builder;
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
			mapSettings.put(mapName, b.build());
		}
	}
	
	protected void buildChannelSettings() {
		for(String channelName : channelSettingBuilders.keySet()) {
			EzyRedisChannelSettingBuilder b = channelSettingBuilders.get(channelName);
			channelSettings.put(channelName, b.build());
		}
	}
	
}
