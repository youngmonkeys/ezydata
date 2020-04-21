package com.tvd12.ezydata.database.test.reflect;

import com.tvd12.ezydata.database.reflect.EzyGetterBuilder;
import com.tvd12.ezydata.database.reflect.EzyObjectProxy;
import com.tvd12.ezydata.database.reflect.EzyObjectProxyProvider;

import lombok.Getter;
import lombok.Setter;

public class EzyObjectProxyProviderTest {

	public void test() {
		EzyGetterBuilder.setDebug(true);
		EzyObjectProxyProvider provider = new EzyObjectProxyProvider();
		EzyObjectProxy objectProxy = provider.getObjectProxy(A.class);
		A a = new A();
		objectProxy.setProperty(a, "id", 10);
		objectProxy.setProperty(a, "a", true);
		objectProxy.setProperty(a, "b", (byte)1);
		objectProxy.setProperty(a, "c", 'a');
		objectProxy.setProperty(a, "d", 11D);
		objectProxy.setProperty(a, "e", 12F);
		objectProxy.setProperty(a, "f", 15L);
		objectProxy.setProperty(a, "g", (short)16);
		objectProxy.setProperty(a, "value", "dzung");
		System.out.println((int)objectProxy.getProperty(a, "id"));
		System.out.println((boolean)objectProxy.getProperty(a, "a"));
		System.out.println((byte)objectProxy.getProperty(a, "b"));
		System.out.println((char)objectProxy.getProperty(a, "c"));
		System.out.println((double)objectProxy.getProperty(a, "d"));
		System.out.println((float)objectProxy.getProperty(a, "e"));
		System.out.println((long)objectProxy.getProperty(a, "f"));
		System.out.println((short)objectProxy.getProperty(a, "g"));
		System.out.println((String)objectProxy.getProperty(a, "value"));
	}
	
	public static void main(String[] args) {
		new EzyObjectProxyProviderTest().test();
	}
	
	@Getter
	@Setter
	public static class A {
		private boolean a;
		private byte b;
		private char c;
		private double d;
		private float e;
		private int id;
		private long f;
		private short g;
		protected String value;
	}
	
}
