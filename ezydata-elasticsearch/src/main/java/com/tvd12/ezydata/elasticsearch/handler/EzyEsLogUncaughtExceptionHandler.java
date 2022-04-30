package com.tvd12.ezydata.elasticsearch.handler;

import com.tvd12.ezydata.elasticsearch.action.EzyEsAction;
import com.tvd12.ezyfox.util.EzyLoggable;

public class EzyEsLogUncaughtExceptionHandler
    extends EzyLoggable
    implements EzyEsUncaughtExceptionHandler {

    @Override
    public void uncaughtException(EzyEsAction action, Throwable e) {
        logger.error("call action ({}): {} error", action.getActionType(), action, e);
    }
}
