package com.tvd12.ezydata.redis.test;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.EzyRedisProxyFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;

public class EzyRedisProxyFactoryTest {

	@Test
	public void prepareSettingsWithReflectionIsNullTest() {
		// given
		EzyRedisProxyFactory factory = new EzyRedisProxyFactory.Builder()
				.settingsBuilder(new EzyRedisSettingsBuilder())
				.build();
		
		// when
		// then
		assert factory.newRedisProxy() != null;
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void prepareSettingsWithErrorEntityTest() {
		new EzyRedisProxyFactory.Builder()
				.scan("com.tvd12.ezydata.redis.test.error_entity")
				.build();
	}
	
	@Test
	public void prepareMapNameTranslator2TimesTest() {
	    // given
	    EzyRedisProxyFactory.Builder factoryBuilder = new EzyRedisProxyFactory.Builder()
            .settingsBuilder(new EzyRedisSettingsBuilder());
	    
	    // when
	    // then
	    factoryBuilder.build();
	    factoryBuilder.build();
	}
	
}
