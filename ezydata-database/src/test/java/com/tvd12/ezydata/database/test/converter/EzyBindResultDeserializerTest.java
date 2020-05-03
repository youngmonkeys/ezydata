package com.tvd12.ezydata.database.test.converter;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.converter.EzyBindResultDeserializer;
import com.tvd12.ezyfox.binding.EzyBindingContext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class EzyBindResultDeserializerTest {

	@Test
	public void test() {
		EzyBindingContext bindingContext = EzyBindingContext.builder()
				.addArrayBindingClass(A.class)
				.build();
		EzyBindResultDeserializer deserializer = new EzyBindResultDeserializer(
				A.class, bindingContext.newUnmarshaller()
		);
		A a = (A) deserializer.deserialize(new Object[] {"hello", 123});
		System.out.println(a);
	}
	
	@Getter
	@Setter
	@ToString
	public static class A {
		private String name;
		private int value;
	}
	
}
