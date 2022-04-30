package com.tvd12.ezydata.elasticsearch;

import lombok.Getter;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class EzyEsRestClientProxy implements EzyEsClientProxy {

    @Getter
    protected final RestHighLevelClient elasticsearchClient;

    public EzyEsRestClientProxy(RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public BulkResponse bulk(
        BulkRequest bulkRequest,
        RequestOptions options
    ) throws IOException {
        return elasticsearchClient.bulk(bulkRequest, options);
    }

    @Override
    public IndexResponse index(
        IndexRequest indexRequest,
        RequestOptions options
    ) throws IOException {
        return elasticsearchClient.index(indexRequest, options);
    }

    @Override
    public SearchResponse search(
        SearchRequest searchRequest,
        RequestOptions options
    ) throws IOException {
        return elasticsearchClient.search(searchRequest, options);
    }
}
