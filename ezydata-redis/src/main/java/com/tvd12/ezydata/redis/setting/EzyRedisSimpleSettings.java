package com.tvd12.ezydata.redis.setting;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class EzyRedisSimpleSettings implements EzyRedisSettings {

	@Getter
	@Setter
	protected String atomicLongMapName;
	protected Map<String, EzyRedisMapSetting> mapSettings;
	protected Map<String, EzyRedisChannelSetting> channelSettings;
	
	public EzyRedisSimpleSettings() {
		this.mapSettings = new HashMap<>();
		this.channelSettings = new HashMap<>();
		this.atomicLongMapName = "___ezydata.atomic_longs___";
	}
	
	@Override
	public EzyRedisMapSetting getMapSeting(String mapName) {
		EzyRedisMapSetting setting = mapSettings.get(mapName);
		return setting;
	}
	
	@Override
	public EzyRedisChannelSetting getChannelSeting(String channelName) {
		EzyRedisChannelSetting setting = channelSettings.get(channelName);
		return setting;
	}
	
	public void addMapSettings(Map<String, EzyRedisMapSetting> settings) {
		this.mapSettings.putAll(settings);
	}

	public void addMapSetting(String mapName, EzyRedisMapSetting setting) {
		this.mapSettings.put(mapName, setting);
	}
	
	public void addChannelSettings(Map<String, EzyRedisChannelSetting> settings) {
		this.channelSettings.putAll(settings);
	}

	public void addChannelSetting(String mapName, EzyRedisChannelSetting setting) {
		this.channelSettings.put(mapName, setting);
	}
}
