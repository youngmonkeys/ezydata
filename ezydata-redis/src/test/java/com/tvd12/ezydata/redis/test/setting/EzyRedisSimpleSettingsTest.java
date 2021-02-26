package com.tvd12.ezydata.redis.test.setting;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.setting.EzyRedisSimpleChannelSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisSimpleMapSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisSimpleSettings;

public class EzyRedisSimpleSettingsTest {

	@Test
	public void test() {
		// given
		EzyRedisSimpleSettings sut = new EzyRedisSimpleSettings();
		sut.addMapSetting("map", new EzyRedisSimpleMapSetting());
		sut.addChannelSetting("channel", new EzyRedisSimpleChannelSetting());
		
		// when
		// then
		assert sut.getMapSeting("map") != null;
		assert sut.getChannelSeting("channel") != null;
		
	}
	
}
