package com.tvd12.ezydata.elasticsearch;

import com.tvd12.ezydata.elasticsearch.handler.EzyEsActionHandler;
import com.tvd12.ezydata.elasticsearch.handler.EzyEsUncaughtExceptionHandler;
import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("rawtypes")
public interface EzyEsCallerBuilder extends EzyBuilder<EzyEsCaller> {

    EzyEsSimpleCallerBuilder maxQueueSize(int maxQueueSize);

    EzyEsSimpleCallerBuilder threadPoolSize(int threadPoolSize);

    EzyEsSimpleCallerBuilder scanIndexedClasses(String packageToScan);

    EzyEsSimpleCallerBuilder clientProxy(EzyEsClientProxy clientProxy);

    EzyEsCallerBuilder addActionHandler(String actionType, EzyEsActionHandler handler);

    EzyEsSimpleCallerBuilder uncaughtExceptionHandler(EzyEsUncaughtExceptionHandler uncaughtExceptionHandler);
}
