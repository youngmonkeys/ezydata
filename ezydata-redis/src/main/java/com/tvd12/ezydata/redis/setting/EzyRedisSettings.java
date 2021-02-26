package com.tvd12.ezydata.redis.setting;

public interface EzyRedisSettings {
	
	String ATOMIC_LONG_MAP_NAME 			= "redis.atomic_long_map_name";
	String MAPS 							= "redis.maps";
	String MAP_NAMING_CASE   				= "redis.map_naming.case";
    String MAP_NAMING_IGNORED_SUFFIX   		= "redis.map_naming.ignored_suffix";
	String CHANNELS 						= "redis.channels";
	String MAP_KEY_TYPE 					= "key_type";
	String MAP_VALUE_TYPE 					= "value_type";
	String CHANNEL_THREAD_POOL_SIZE 		= "thread_pool_size";
	String CHANNEL_MESSAGE_TYPE 			= "message_type";

	String getAtomicLongMapName();
	
	EzyRedisMapSetting getMapSeting(String mapName);
	
	EzyRedisChannelSetting getChannelSeting(String channelName);
	
	static EzyRedisSettingsBuilder builder() {
		return new EzyRedisSettingsBuilder();
	}
}
