package com.tvd12.ezydata.elasticsearch.testing;

import com.tvd12.ezydata.elasticsearch.EzyEsCaller;
import com.tvd12.ezydata.elasticsearch.EzyEsRestClientProxy;
import com.tvd12.ezydata.elasticsearch.EzyEsSimpleCaller;
import com.tvd12.ezydata.elasticsearch.action.EzyEsSimpleSearchAction;
import com.tvd12.ezydata.elasticsearch.testing.data.Person;
import com.tvd12.ezydata.elasticsearch.testing.data.TestPerson;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

public class SearchTestPersonTest {

    public static void main(String[] args) {
        new SearchTestPersonTest().test();
    }

    public void test() {
        RestHighLevelClient highLevelClient = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
        EzyEsCaller client = EzyEsSimpleCaller.builder()
            .scanIndexedClasses("com.tvd12.ezydata.elasticsearch.testing.data")
            .clientProxy(new EzyEsRestClientProxy(highLevelClient))
            .build();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("test");
        List<Person> persons = client.call(new EzyEsSimpleSearchAction()
            .searchRequest(searchRequest)
            .responseItemType(TestPerson.class));
        System.out.println(persons);
        try {
            highLevelClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
