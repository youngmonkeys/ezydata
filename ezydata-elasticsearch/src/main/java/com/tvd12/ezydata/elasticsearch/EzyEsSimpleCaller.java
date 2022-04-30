package com.tvd12.ezydata.elasticsearch;

import com.tvd12.ezydata.elasticsearch.action.EzyEsAction;
import com.tvd12.ezydata.elasticsearch.action.EzyEsActionWrapper;
import com.tvd12.ezydata.elasticsearch.action.EzyEsSimpleActionWrapper;
import com.tvd12.ezydata.elasticsearch.callback.EzyEsActionCallback;
import com.tvd12.ezydata.elasticsearch.concurrent.EzyEsActionHandleLoop;
import com.tvd12.ezydata.elasticsearch.concurrent.EzyEsActionQueue;
import com.tvd12.ezydata.elasticsearch.handler.EzyEsActionHandler;
import com.tvd12.ezyfox.exception.EzyProxyException;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Getter;

import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyEsSimpleCaller extends EzyLoggable implements EzyEsCaller {

    @Getter
    protected final EzyEsClientProxy clientProxy;
    protected final EzyEsActionQueue actionQueue;
    protected final EzyEsActionHandleLoop actionHandleLoop;
    protected final Map<String, EzyEsActionHandler> actionHandlers;

    protected EzyEsSimpleCaller(EzyEsSimpleCallerBuilder builder) {
        this.actionHandlers = builder.actionHandlers;
        this.clientProxy = builder.clientProxy;
        this.actionQueue = new EzyEsActionQueue(builder.maxQueueSize);
        this.actionHandleLoop = new EzyEsActionHandleLoop(
            builder.threadPoolSize,
            actionQueue,
            builder.uncaughtExceptionHandler);
    }

    public static EzyEsCallerBuilder builder() {
        return new EzyEsSimpleCallerBuilder();
    }

    @Override
    public <T> T sync(EzyEsAction action) {
        EzyEsActionHandler actionHandler = actionHandlers.get(action.getActionType());
        if (actionHandler == null) {
            throw new IllegalArgumentException("has no action handler for type: " + action.getActionType());
        }
        try {
            return (T) actionHandler.handle(action);
        } catch (Exception e) {
            throw new EzyProxyException(e);
        }
    }

    @Override
    public void async(EzyEsAction action, EzyEsActionCallback callback) {
        EzyEsActionHandler actionHandler = actionHandlers.get(action.getActionType());
        if (actionHandler == null) {
            throw new IllegalArgumentException("has no action handler for type: " + action.getActionType());
        }
        EzyEsActionWrapper wrapper = new EzyEsSimpleActionWrapper(action, actionHandler, callback);
        actionQueue.addAction(wrapper);
    }

    @Override
    public void start() {
        actionHandleLoop.start();
    }

    @Override
    public void shutdown() {
        actionHandleLoop.stop();
    }
}
