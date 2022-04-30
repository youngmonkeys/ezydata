package com.tvd12.ezydata.elasticsearch.testing;

import com.tvd12.ezydata.elasticsearch.EzyEsCaller;
import com.tvd12.ezydata.elasticsearch.EzyEsRestClientProxy;
import com.tvd12.ezydata.elasticsearch.EzyEsSimpleCaller;
import com.tvd12.ezydata.elasticsearch.action.EzyEsSimpleIndexAction;
import com.tvd12.ezydata.elasticsearch.testing.data.Person;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class IndexDataTest {

    public static void main(String[] args) {
        new IndexDataTest().test();
    }

    public void test() {
        RestHighLevelClient highLevelClient = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
        EzyEsCaller client = EzyEsSimpleCaller.builder()
            .scanIndexedClasses("com.tvd12.ezydata.elasticsearch.testing.data")
            .clientProxy(new EzyEsRestClientProxy(highLevelClient))
            .build();
        client.call(new EzyEsSimpleIndexAction()
            .object(new Person("dungtv", "itprono3@gmail.com", "0123456", 27)));
        try {
            highLevelClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
