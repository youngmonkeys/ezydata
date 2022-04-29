package com.tvd12.ezydata.redis.test;

import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.codec.EzyBindingEntityCodec;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleSerializer;

import redis.clients.jedis.Jedis;

public class EzyRedisBaseTest {

    protected final static Jedis JEDIS;
    protected final static EzyEntityCodec ENTITY_CODEC;

    static {
        JEDIS = newJedis();
        ENTITY_CODEC = newEntityCodec();
    }

    private static Jedis newJedis() {
        Jedis jedis = new Jedis();
        return jedis;
    }

    private static EzyEntityCodec newEntityCodec() {
        EzyBindingContext bindingContext = EzyBindingContext.builder()
                .scan("com.tvd12.ezydata.redis.test")
                .build();
        EzyEntityCodec codec = EzyBindingEntityCodec.builder()
                .marshaller(bindingContext.newMarshaller())
                .unmarshaller(bindingContext.newUnmarshaller())
                .messageSerializer(new MsgPackSimpleSerializer())
                .messageDeserializer(new MsgPackSimpleDeserializer())
                .build();
        return codec;
    }

}
