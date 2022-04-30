package com.tvd12.ezydata.hazelcast.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE})
public @interface EzyMapServiceAutoImpl {

    /**
     * Bean name.
     *
     * @return the bean name
     */
    String value();

    /**
     * Bean name.
     *
     * @return the bean name
     */
    String name() default "";
}
