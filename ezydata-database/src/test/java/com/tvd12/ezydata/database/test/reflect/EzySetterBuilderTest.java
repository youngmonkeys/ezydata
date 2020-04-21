package com.tvd12.ezydata.database.test.reflect;

import java.util.function.BiConsumer;

import com.tvd12.ezydata.database.reflect.EzySetterBuilder;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;

import lombok.Setter;

public class EzySetterBuilderTest {

	@SuppressWarnings("unchecked")
	public void test() {
		EzyClass clazz = new EzyClass(A.class);
		EzyField field = clazz.getField("value");
		EzySetterBuilder.setDebug(true);
		BiConsumer<A,String> setter = new EzySetterBuilder()
			.field(field)
			.build();
		A a = new A();
		setter.accept(a, "dung");
		System.out.println(a.value);
	}
	
	public static void main(String[] args) {
		new EzySetterBuilderTest().test();
	}
	
	@Setter
	public static class A {
		protected String value;
	}
	
}
