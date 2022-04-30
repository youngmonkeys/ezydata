package com.tvd12.ezydata.elasticsearch.action;

import org.elasticsearch.client.RequestOptions;

import java.util.List;
import java.util.Set;

public interface EzyEsIndexAction extends EzyEsAction {

    List<Object> getObjects();

    Set<String> getIndexes();

    RequestOptions getRequestOptions();
}
