package com.tvd12.ezydata.database.query;

import java.util.Map;

public interface EzyQueryManager {
	
	EzyQueryEntity getQuery(String name);
	
	Map<String, EzyQueryEntity> getQueries();
	
}
