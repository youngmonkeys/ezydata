package com.tvd12.ezydata.mongodb.converter;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;

import com.tvd12.ezyfox.io.EzyDates;
import com.tvd12.ezyfox.io.EzyStrings;

public class EzyMongoBsonValueToString {

	public String convert(BsonValue value) {
		if(value instanceof BsonNull)
			return null;
		if(value instanceof BsonBoolean)
			return String.valueOf(((BsonBoolean)value).getValue());
		if(value instanceof BsonDouble)
			return String.valueOf(((BsonDouble)value).getValue());
		if(value instanceof BsonInt32)
			return String.valueOf(((BsonInt32)value).getValue());
		if(value instanceof BsonInt64)
			return String.valueOf(((BsonInt64)value).getValue());
		if(value instanceof BsonString)
			return "'" + ((BsonString)value).getValue() + "'";
		if(value instanceof BsonDocument)
			return ((BsonDocument)value).toJson();
		if(value instanceof BsonArray)
			return convertArray((BsonArray)value);
		if(value instanceof BsonDateTime)
			return "'" + EzyDates.format(((BsonDateTime)value).getValue()) + "'";
		return String.valueOf(value);
	}
	
	protected String convertArray(BsonArray array) {
		List<String> list = new ArrayList<>();
		for(BsonValue item : array)
			list.add(convert(item));
		return "[" + EzyStrings.join(list, ",") + "]";
	}
	
}
