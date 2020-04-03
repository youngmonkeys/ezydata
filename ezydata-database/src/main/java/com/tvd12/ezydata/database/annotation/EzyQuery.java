package com.tvd12.ezydata.database.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EzyQuery {

	String name() default "";
	String value() default "";
	boolean nativeQuery() default false;
	Class<?> resultType() default Object.class;
	
}
