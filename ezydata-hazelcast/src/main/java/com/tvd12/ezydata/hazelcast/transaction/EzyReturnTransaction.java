package com.tvd12.ezydata.hazelcast.transaction;

import com.tvd12.ezyfox.function.EzyExceptionFunction;

public interface EzyReturnTransaction<T,R> extends EzyTransaction {
	
	R apply(EzyExceptionFunction<T, R> func) throws Exception;
	
}
