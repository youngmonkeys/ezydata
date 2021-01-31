package com.tvd12.ezydata.database.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzyQueryString {

	protected String queryString;
	protected boolean nativeQuery;
	
	public EzyQueryString(String queryString) {
		this(queryString, false);
	}
	
}
