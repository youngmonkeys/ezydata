package com.tvd12.ezydata.redis.setting;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class EzyRedisSimpleSettings implements EzyRedisSettings {

	@Getter
	protected String atomicLongMapName;
	protected Map<String, EzyRedisMapSetting> mapSettings;
	
	public EzyRedisSimpleSettings() {
		this.mapSettings = new HashMap<>();
		this.atomicLongMapName = "___ezydata.atomic_longs___";
	}
	
	@Override
	public EzyRedisMapSetting getMapSeting(String mapName) {
		EzyRedisMapSetting setting = mapSettings.get(mapName);
		return setting;
	}

	public void addMapSetting(String mapName, EzyRedisMapSetting setting) {
		this.mapSettings.put(mapName, setting);
	}
	
}
