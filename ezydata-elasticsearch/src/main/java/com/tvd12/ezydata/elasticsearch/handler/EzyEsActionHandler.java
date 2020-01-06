package com.tvd12.ezydata.elasticsearch.handler;

import com.tvd12.ezydata.elasticsearch.action.EzyEsAction;

public interface EzyEsActionHandler<A extends EzyEsAction, R> {
	
	R handle(A action) throws Exception;
	
}
