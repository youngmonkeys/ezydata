package com.tvd12.ezydata.redis.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyRedisChannelSettingBuilder implements EzyBuilder<EzyRedisChannelSetting> {

	protected Class<?> messageType;
	protected int subThreadPoolSize = 1;
	protected EzyRedisSettingsBuilder parent;
	
	public EzyRedisChannelSettingBuilder() {}
	
	public EzyRedisChannelSettingBuilder(EzyRedisSettingsBuilder parent) {
		this.parent = parent;
	}
	
	public EzyRedisChannelSettingBuilder messageType(Class<?> messageType) {
		this.messageType = messageType;
		return this;
	}
	
	public EzyRedisChannelSettingBuilder subThreadPoolSize(int subThreadPoolSize) {
		this.subThreadPoolSize = subThreadPoolSize;
		return this;
	}
	
	public EzyRedisSettingsBuilder parent() {
		return parent;
	}
	
	@Override
	public EzyRedisChannelSetting build() {
		EzyRedisSimpleChannelSetting setting = new EzyRedisSimpleChannelSetting();
		setting.setMessageType(messageType);
		setting.setSubThreadPoolSize(subThreadPoolSize);
		return setting;
	}


}
