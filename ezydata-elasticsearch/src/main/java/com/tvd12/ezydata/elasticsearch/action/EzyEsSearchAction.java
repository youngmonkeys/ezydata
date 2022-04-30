package com.tvd12.ezydata.elasticsearch.action;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;

public interface EzyEsSearchAction extends EzyEsAction {

    SearchRequest getSearchRequest();

    RequestOptions getRequestOptions();

    Class<?> getResponseItemType();
}
