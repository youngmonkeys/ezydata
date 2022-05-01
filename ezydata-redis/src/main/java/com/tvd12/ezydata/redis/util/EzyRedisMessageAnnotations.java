package com.tvd12.ezydata.redis.util;

import com.tvd12.ezydata.redis.annotation.EzyRedisMessage;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.message.util.EzyMessageAnnotations;

public final class EzyRedisMessageAnnotations {

    private EzyRedisMessageAnnotations() {}

    public static String getChannelName(Class<?> messageClass) {
        EzyRedisMessage anno = messageClass.getAnnotation(EzyRedisMessage.class);
        if (anno != null) {
            return getChannelName(anno);
        }
        return EzyMessageAnnotations.getChannelName(messageClass);
    }

    public static String getChannelName(EzyRedisMessage anno) {
        String channelName = anno.value();
        if (EzyStrings.isNoContent(channelName)) {
            channelName = anno.channel();
        }
        return channelName;
    }
}
