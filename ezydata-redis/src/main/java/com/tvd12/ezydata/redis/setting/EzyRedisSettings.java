package com.tvd12.ezydata.redis.setting;

public interface EzyRedisSettings {

	String getAtomicLongMapName();
	
	EzyRedisMapSetting getMapSeting(String mapName);
}
