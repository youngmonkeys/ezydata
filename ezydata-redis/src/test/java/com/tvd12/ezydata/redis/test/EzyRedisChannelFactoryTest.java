package com.tvd12.ezydata.redis.test;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.factory.EzyRedisChannelFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;

public class EzyRedisChannelFactoryTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void newMapFailed() {
		// given
		EzyRedisChannelFactory factory = EzyRedisChannelFactory.builder()
				.settings(new EzyRedisSettingsBuilder().build())
				.build();
		
		// when
		// then
		factory.newChannel("no channel");
	}
	
}
