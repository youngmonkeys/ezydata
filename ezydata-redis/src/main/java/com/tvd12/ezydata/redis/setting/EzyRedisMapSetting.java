package com.tvd12.ezydata.redis.setting;

@SuppressWarnings("rawtypes")
public interface EzyRedisMapSetting {
	
	Class getKeyType();

	Class getValueType();

}
