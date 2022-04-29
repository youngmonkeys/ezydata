package com.tvd12.ezydata.elasticsearch.callback;

import com.tvd12.ezydata.elasticsearch.action.EzyEsAction;

public interface EzyEsActionCallback<T> {

    default void onSuccess(EzyEsAction action, T response) {
    }
    
    default void onError(EzyEsAction action, Throwable exception) {
    }
    
}
