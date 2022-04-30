package com.tvd12.ezydata.elasticsearch.handler;

import com.tvd12.ezydata.elasticsearch.action.EzyEsSearchAction;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.util.EzyEntityObjects;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class EzyEsSearchActionHandler extends EzyEsAbstractActionHandler<EzyEsSearchAction, List> {

    @SuppressWarnings("unchecked")
    @Override
    public List handle(EzyEsSearchAction action) throws Exception {
        SearchRequest searchRequest = action.getSearchRequest();
        RequestOptions requestOptions = action.getRequestOptions();
        Class<?> responseItemType = action.getResponseItemType();
        SearchResponse response = clientProxy.search(searchRequest, requestOptions);
        SearchHits hits = response.getHits();
        List result = new ArrayList<>();
        hits.forEach(hit -> result.add(unmarshalHit(hit, responseItemType)));
        return result;
    }

    protected Object unmarshalHit(SearchHit hit, Class<?> responseItemType) {
        Map<String, Object> map = hit.getSourceAsMap();
        EzyObject source = EzyEntityObjects.newObject(map);
        return unmarshaller.unmarshal(source, responseItemType);
    }
}
