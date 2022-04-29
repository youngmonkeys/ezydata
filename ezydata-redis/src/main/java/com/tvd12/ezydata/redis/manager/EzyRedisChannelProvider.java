package com.tvd12.ezydata.redis.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezydata.redis.EzyRedisChannel;
import com.tvd12.ezydata.redis.factory.EzyRedisChannelFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyRedisChannelProvider {

    protected final Map<String, EzyRedisChannel> channels;
    protected final EzyRedisChannelFactory channelFactory;
    
    protected EzyRedisChannelProvider(Builder builder) {
        this.channels = new HashMap<>();
        this.channelFactory = builder.channelFactory;
    }
    
    public <T> EzyRedisChannel<T> getChannel(String name) {
        EzyRedisChannel<T> channel = channels.get(name);
        if(channel == null)
            channel = newChannel(name);
        return channel;
    }
    
    public <T> EzyRedisChannel<T> getChannel(String name, Class<T> messageType) {
        EzyRedisChannel<T> channel = channels.get(name);
        if(channel == null)
            channel = newChannel(name, messageType);
        return channel;
    }
    
    protected <T> EzyRedisChannel<T> newChannel(String name) {
        synchronized (channels) {
            EzyRedisChannel<T> channel = channels.get(name);
            if(channel == null) {
                channel = channelFactory.newChannel(name);
                channels.put(name, channel);
            }
            return channel;
        }
    }
    
    protected <T> EzyRedisChannel<T> newChannel(String name, Class<T> messageType) {
        synchronized (channels) {
            EzyRedisChannel<T> channel = channels.get(name);
            if(channel == null) {
                channel = channelFactory.newChannel(name, messageType);
                channels.put(name, channel);
            }
            return channel;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyRedisChannelProvider> {
        
        protected EzyRedisChannelFactory channelFactory;
        
        public Builder channelFactory(EzyRedisChannelFactory channelFactory) {
            this.channelFactory = channelFactory;
            return this;
        }
        
        @Override
        public EzyRedisChannelProvider build() {
            return new EzyRedisChannelProvider(this);
        }
        
    }
    
}
