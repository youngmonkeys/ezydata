package com.tvd12.ezydata.elasticsearch;

import com.tvd12.ezydata.elasticsearch.action.EzyEsAction;
import com.tvd12.ezydata.elasticsearch.callback.EzyEsActionCallback;
import com.tvd12.ezyfox.util.EzyShutdownable;
import com.tvd12.ezyfox.util.EzyStartable;

@SuppressWarnings("rawtypes")
public interface EzyEsCaller extends EzyStartable, EzyShutdownable {

    <T> T sync(EzyEsAction action);

    void async(EzyEsAction action, EzyEsActionCallback callback);

    EzyEsClientProxy getClientProxy();

    default void async(EzyEsAction action) {
        async(action, null);
    }

    default <T> T call(EzyEsAction action) {
        return sync(action);
    }

}
