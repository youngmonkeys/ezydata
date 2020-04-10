package com.tvd12.ezydata.redis;

public interface EzyRedisSubscriber {
	
	void onMessage(byte[] channel, byte[] messageBytes);
	
}
