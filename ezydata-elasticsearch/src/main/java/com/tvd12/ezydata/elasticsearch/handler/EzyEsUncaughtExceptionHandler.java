package com.tvd12.ezydata.elasticsearch.handler;

import com.tvd12.ezydata.elasticsearch.action.EzyEsAction;

public interface EzyEsUncaughtExceptionHandler {
	
	void uncaughtException(EzyEsAction action, Throwable e);
	
}
