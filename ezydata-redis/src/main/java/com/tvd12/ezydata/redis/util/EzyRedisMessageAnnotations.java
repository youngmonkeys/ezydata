package com.tvd12.ezydata.redis.util;

import com.tvd12.ezydata.redis.annotation.EzyRedisMessage;
import com.tvd12.ezyfox.io.EzyStrings;

public final class EzyRedisMessageAnnotations {
	
	private EzyRedisMessageAnnotations() {}
	
	public static String getChannelName(Class<?> messageClass) {
		return getChannelName(messageClass.getAnnotation(EzyRedisMessage.class));
	}
	
	public static String getChannelName(EzyRedisMessage anno) {
		String channelName = anno.value();
		if(EzyStrings.isNoContent(channelName))
			channelName = anno.channel();
		return channelName;
	}
	
}
