package com.tvd12.ezydata.redis.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.reflect.EzyClasses;

public class EzyRedisMapSettingBuilder implements EzyBuilder<EzyRedisMapSetting> {

    protected Class<?> keyType;
    protected Class<?> valueType;
    protected EzyRedisSettingsBuilder parent;
    
    public EzyRedisMapSettingBuilder() {}
    
    public EzyRedisMapSettingBuilder(EzyRedisSettingsBuilder parent) {
        this.parent = parent;
    }
    
    public EzyRedisMapSettingBuilder keyType(Class<?> keyType) {
        if(keyType != null)
            this.keyType = keyType;
        return this;
    }
    
    public EzyRedisMapSettingBuilder keyType(String keyType) {
        if(keyType != null)
            this.keyType = EzyClasses.getClass(keyType);
        return this;
    }
    
    public EzyRedisMapSettingBuilder valueType(Class<?> valueType) {
        if(valueType != null)
            this.valueType = valueType;
        return this;
    }
    
    public EzyRedisMapSettingBuilder valueType(String valueType) {
        if(valueType != null)
            this.valueType = EzyClasses.getClass(valueType);
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
