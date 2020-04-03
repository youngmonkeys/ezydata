package com.tvd12.ezydata.database.query;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.util.EzyLoggable;

public class EzySimpleQueryManager
		extends EzyLoggable 
		implements EzyQueryManager {

	protected final Map<String, EzyQueryEntity> queries;
	
	public EzySimpleQueryManager() {
		this.queries = new HashMap<>();
	}
	
	@Override
	public EzyQueryEntity getQuery(String name) {
		EzyQueryEntity query = queries.get(name);
		if(query == null)
			throw new IllegalArgumentException("has no query with name: " + name);
		return query;
	}
	
	@Override
	public Map<String, EzyQueryEntity> getQueries() {
		return new HashMap<>(queries);
	}
	
	public void addQuery(EzyQueryEntity query) {
		this.queries.put(query.getName(), query);
	}

}
