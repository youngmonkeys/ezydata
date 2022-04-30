package com.tvd12.ezydata.elasticsearch.action;

import com.tvd12.ezydata.elasticsearch.callback.EzyEsActionCallback;
import com.tvd12.ezydata.elasticsearch.handler.EzyEsActionHandler;

@SuppressWarnings("rawtypes")
public interface EzyEsActionWrapper {

    EzyEsAction getAction();

    EzyEsActionHandler getHandler();

    EzyEsActionCallback getCallback();
}
