package com.tvd12.ezydata.redis.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EzyRedisMessage {

    String channel() default "";

    String value() default "";
}
