package com.tvd12.ezydata.database.query;

import lombok.Getter;

public enum EzyQueryOperation {
	EQUAL("="),
	IN("in");
	
	@Getter
	private final String sign;
	
	private EzyQueryOperation(String sign) {
		this.sign = sign;
	}
}
