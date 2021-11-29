package com.tvd12.ezydata.mongodb.testing.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.types.ObjectId;
import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.converter.EzyMongoDataToBsonValue;
import com.tvd12.ezydata.mongodb.testing.bean.Person;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.test.assertion.Asserts;

public class EzyMongoDataToBsonValueTest {

	@Test
	public void test() {
		EzyMongoDataToBsonValue converter = new EzyMongoDataToBsonValue();
		converter.addConverter(Person.class, p -> {
			BsonDocument doc = new BsonDocument();
			doc.put("name", new BsonString(((Person)p).getName()));
			return doc;
		});
		BsonString bsonString = new BsonString("hello");
		assert converter.convert(bsonString) == bsonString;
		System.out.println("iterable: " + converter.convert(Arrays.asList(1, 2, 3)));
		System.out.println("map: " + converter.convert(EzyMapBuilder.mapBuilder()
				.put("hello", 1)
				.put("world", 2)
				.build()));
		System.out.println("map2: " + converter.convert(EzyMapBuilder.mapBuilder()
				.put(new BsonDocument("name", new BsonString("hello")), 10)
				.put(new BsonString("myId"), 2)
				.put(10, "100")
				.build()));
		System.out.println("ezyarray: " + converter.convert(EzyEntityFactory.newArrayBuilder()
				.append("hello", 1, "world")
				.build()));
		System.out.println("array: " + converter.convert(new Object[] {"foo", 2L, "bar"}));
		
		System.out.println("boolean: " + converter.convert(true));
		System.out.println("byte: " + converter.convert((byte)1));
		System.out.println("char: " + converter.convert('a'));
		System.out.println("double: " + converter.convert(3.0D));
		System.out.println("float: " + converter.convert(4.0F));
		System.out.println("long: " + converter.convert(5L));
		System.out.println("short: " + converter.convert((short)6));
		System.out.println("boolean[]: " + converter.convert(new boolean[] {true, false}));
		System.out.println("byte[]: " + converter.convert(new byte[] {1, 2}));
		System.out.println("char[]: " + converter.convert(new char[] {'a', 'b'}));
		System.out.println("double[]: " + converter.convert(new double[] {1D, 2D}));
		System.out.println("float[]: " + converter.convert(new float[] {1F, 2F}));
		System.out.println("integer[]: " + converter.convert(new int[] {1, 2}));
		System.out.println("long[]: " + converter.convert(new long[] {1L, 2L}));
		System.out.println("short[]: " + converter.convert(new short[] {1, 2}));
		System.out.println("enum[]: " + converter.convert(EnumA.HELLO));
		try {
			converter.convert(new Object());
		}
		catch (Exception e) {
			assert e instanceof IllegalArgumentException;
		}
		System.out.println("Class: " + converter.convert(getClass()));
		System.out.println("UUID: " + converter.convert(UUID.randomUUID()));
		System.out.println("BigInteger: " + converter.convert(new BigInteger("10")));
		System.out.println("BigDecimal: " + converter.convert(new BigDecimal("11.1")));
		System.out.println("Date: " + converter.convert(new Date()));
		System.out.println("LocalDate: " + converter.convert(LocalDate.now()));
		System.out.println("LocalDateTime: " + converter.convert(LocalDateTime.now()));
		
		ObjectId objectId = new ObjectId();
		Asserts.assertEquals(converter.convert(objectId), new BsonObjectId(objectId));
	}
	
	public static enum EnumA {
		HELLO,
		WORLD
	}
	
}
