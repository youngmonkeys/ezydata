package com.tvd12.ezydata.elasticsearch.action;

import com.tvd12.ezydata.elasticsearch.callback.EzyEsActionCallback;
import com.tvd12.ezydata.elasticsearch.handler.EzyEsActionHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("rawtypes")
public class EzyEsSimpleActionWrapper implements EzyEsActionWrapper {

    protected final EzyEsAction action;
    protected final EzyEsActionHandler handler;
    protected final EzyEsActionCallback callback;
}
