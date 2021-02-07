package com.tvd12.ezydata.database.test.naming;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.naming.EzyNamingCase;
import com.tvd12.ezydata.database.naming.EzySimpleNameTranslator;

public class EzySimpleNameTranslatorTest {

	@Test
	public void natureCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.ignoredSuffix(null)
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("HelloWorld");
		assert sut.getNamingCase() == EzyNamingCase.NATURE;
	}
	
	@Test
	public void upperCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.namingCase(EzyNamingCase.UPPER)
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("HELLOWORLD");
	}
	
	@Test
	public void lowerCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.namingCase(EzyNamingCase.LOWER)
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("helloworld");
	}
	
	@Test
	public void camelCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.namingCase(EzyNamingCase.of("CAMEL"))
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("helloWorld");
	}
	
	@Test
	public void dashCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.namingCase(EzyNamingCase.DASH)
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("hello-world");
	}
	
	@Test
	public void dotCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.namingCase(EzyNamingCase.DOT)
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld_Foo-Bar").equals("hello.world.foo.bar");
	}
	
	@Test
	public void underscoreCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.namingCase(EzyNamingCase.UNDERSCORE)
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("hello_world");
	}
	
	@Test
	public void ingnoreSuffixCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.ignoredSuffix("World")
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("Hello");
	}
	
	@Test
	public void ignoreSuffixFitSizeCase() {
		// given
		EzySimpleNameTranslator sut = EzySimpleNameTranslator.builder()
				.ignoredSuffix("HelloWorld")
				.build();
		
		// when
		// then
		assert sut.translate("HelloWorld").equals("HelloWorld");
	}
	
}
