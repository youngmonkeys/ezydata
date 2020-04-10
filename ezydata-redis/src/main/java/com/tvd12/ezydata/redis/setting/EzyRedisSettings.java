package com.tvd12.ezydata.redis.setting;

public interface EzyRedisSettings {

	String getAtomicLongMapName();
	
	EzyRedisMapSetting getMapSeting(String mapName);
	
	EzyRedisChannelSetting getChannelSeting(String channelName);
	
	static EzyRedisSettingsBuilder builder() {
		return new EzyRedisSettingsBuilder();
	}
}
