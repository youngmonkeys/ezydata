package com.tvd12.ezydata.database.query;

import com.tvd12.ezydata.database.constant.EzyDatabaseContants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzyQueryCondition {
	protected String field;
	protected EzyQueryOperation operation;
	
	public static EzyQueryCondition parse(String str) {
		String field = str;
		EzyQueryOperation operation = EzyQueryOperation.EQUAL;
		if(str.endsWith(EzyDatabaseContants.IN)) {
			field = str.substring(
					0, 
					str.length() - EzyDatabaseContants.IN.length()
			);
			operation = EzyQueryOperation.IN;
		}
		field = field.substring(0, 1).toLowerCase() + field.substring(1);
		return new EzyQueryCondition(field, operation);
	}
}
