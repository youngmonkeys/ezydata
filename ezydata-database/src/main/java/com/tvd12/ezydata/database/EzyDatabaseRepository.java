package com.tvd12.ezydata.database;

import com.tvd12.ezyfox.database.repository.EzyEmptyRepository;
import com.tvd12.ezyfox.database.service.EzyCrudService;
import com.tvd12.ezyfox.reflect.EzyClass;

public interface EzyDatabaseRepository<I, E> extends 
		EzyEmptyRepository<I, E>, 
		EzyCrudService<I, E> {
	
	String PREFIX_FIND = "find";
	String PREFIX_FIND_LIST = "findList";
	String PREFIX_FETCH_ONE = "fetch";
	String PREFIX_FETCH_LIST = "fetchList";
	String PREFIX_UPDATE = "update";
	String PREFIX_DELETE = "delete";
	String PREFIX_COUNT = "count";
	
	String PREFIX_FIND_BY = "findBy";
	String PREFIX_COUNT_BY = "countBy";
	
	EzyClass CLASS = new EzyClass(EzyDatabaseRepository.class);
	
}
