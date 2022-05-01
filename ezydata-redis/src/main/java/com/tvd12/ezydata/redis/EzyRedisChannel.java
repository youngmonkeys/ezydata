package com.tvd12.ezydata.redis;

import com.tvd12.ezydata.redis.concurrent.EzyRedisThreadFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisChannelSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyRedisChannel<T> {

    protected final String channelName;
    protected final byte[] channelNameBytes;
    protected final Class messageType;
    protected final EzyRedisClient redisClient;
    protected final EzyEntityCodec entityCodec;
    protected final EzyRedisChannelSetting setting;
    protected final int subThreadPoolSize;
    protected final String subThreadPoolName;
    protected volatile boolean subscribed;
    protected List<Consumer<T>> subscribers;
    protected ExecutorService subExecutorService;

    public EzyRedisChannel(Builder builder) {
        this.setting = builder.setting;
        this.redisClient = builder.redisClient;
        this.entityCodec = builder.entityCodec;
        this.channelName = builder.channelName;
        this.messageType = setting.getMessageType();
        this.channelNameBytes = channelName.getBytes();
        this.subThreadPoolSize = setting.getSubThreadPoolSize();
        this.subThreadPoolName = "channel-subscriber-" + channelName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long publish(T message) {
        byte[] messageBytes = entityCodec.serialize(message);
        return redisClient.publish(channelNameBytes, messageBytes);
    }

    public void addSubscriber(Consumer<T> subscriber) {
        synchronized (this) {
            if (!subscribed) {
                this.subscribed = true;
                this.subExecutorService = newSubExecutorService();
                this.subscribers = Collections.synchronizedList(new ArrayList<>());
                this.subscribe();
            }
        }
        this.subscribers.add(subscriber);
    }

    protected ExecutorService newSubExecutorService() {
        ThreadFactory threadFactory
            = EzyRedisThreadFactory.create(subThreadPoolName);
        ExecutorService executorService
            = Executors.newFixedThreadPool(subThreadPoolSize, threadFactory);
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
        return executorService;
    }

    protected void subscribe() {
        EzyRedisSubscriber subscriber = (channel, messageBytes) -> {
            T message = (T) entityCodec.deserialize(messageBytes, messageType);
            for (Consumer<T> subscriber1 : subscribers) {
                subscriber1.accept(message);
            }
        };
        for (int i = 0; i < subThreadPoolSize; ++i) {
            subExecutorService.execute(() -> redisClient.subscribe(channelNameBytes, subscriber));
        }
    }

    public static class Builder implements EzyBuilder<EzyRedisChannel> {
        protected String channelName;
        protected EzyRedisClient redisClient;
        protected EzyEntityCodec entityCodec;
        protected EzyRedisChannelSetting setting;

        public Builder channelName(String channelName) {
            this.channelName = channelName;
            return this;
        }

        public Builder redisClient(EzyRedisClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        public Builder setting(EzyRedisChannelSetting setting) {
            this.setting = setting;
            return this;
        }

        @Override
        public EzyRedisChannel build() {
            return new EzyRedisChannel<>(this);
        }
    }
}
