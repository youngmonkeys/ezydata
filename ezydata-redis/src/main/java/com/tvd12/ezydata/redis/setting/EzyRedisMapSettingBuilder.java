package com.tvd12.ezydata.redis.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyRedisMapSettingBuilder implements EzyBuilder<EzyRedisMapSetting> {

	protected Class<?> keyType;
	protected Class<?> valueType;
	protected EzyRedisSettingsBuilder parent;
	
	public EzyRedisMapSettingBuilder() {}
	
	public EzyRedisMapSettingBuilder(EzyRedisSettingsBuilder parent) {
		this.parent = parent;
	}
	
	public EzyRedisMapSettingBuilder keyType(Class<?> keyType) {
		this.keyType = keyType;
		return this;
	}
	
	public EzyRedisMapSettingBuilder valueType(Class<?> valueType) {
		this.valueType = valueType;
		return this;
	}
	
	public EzyRedisSettingsBuilder parent() {
		return parent;
	}
	
	@Override
	public EzyRedisMapSetting build() {
		EzyRedisSimpleMapSetting setting = new EzyRedisSimpleMapSetting();
		setting.setKeyType(keyType);
		setting.setValueType(valueType);
		return setting;
	}


}
