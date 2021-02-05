package com.tvd12.ezydata.hazelcast.testing;

import org.testng.annotations.Test;

import com.tvd12.ezydata.hazelcast.EzySimpleMapConfigsFetcher;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.test.base.BaseTest;

public class EzySimpleMapConfigsFetcherTest extends BaseTest {

	@Test
	public void test() {
		EzySimpleMapConfigsFetcher fetcher = EzySimpleMapConfigsFetcher.builder()
				.mapNames(Sets.newHashSet("a", "b", "c"))
				.build();
		fetcher.get();
	}
	
}
