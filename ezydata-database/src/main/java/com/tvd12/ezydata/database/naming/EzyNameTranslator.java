package com.tvd12.ezydata.database.naming;

public interface EzyNameTranslator {
	
	String translate(String originalName);
	
	EzyNamingCase getNamingCase();
	
}
