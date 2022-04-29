package com.tvd12.ezydata.redis.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyRedisSimpleMapSetting implements EzyRedisMapSetting {

    protected Class<?> keyType;
    protected Class<?> valueType;

}
