package com.tvd12.ezydata.elasticsearch.testing;

public class EzyEsRestClientBuilderTest {

//    public static void main(String[] args) {
//        new EzyEsRestClientBuilderTest().test();
//    }

//    public void test() {
//        RestHighLevelClient highLevelClient = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http")));
//        EzyIdFetchers idFetchers = newDataIdFetchers();
//        EzyBindingContext bindingContext = newEzyBindingContext();
//        EzyMarshaller marshaller = bindingContext.newMarshaller();
//        EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
//        EzyIndexedDataClasses indexedDataClasses = newIndexedDataClasses();
//        EzyEsSimpleRestClient esClient = new EzyEsSimpleRestClient();
//        esClient.setMarshaller(marshaller);
//        esClient.setIdFetchers(idFetchers);
//        esClient.setUnmarshaller(unmarshaller);
//        esClient.setHighLevelClient(highLevelClient);
//        esClient.setIndexedDataClasses(indexedDataClasses);
//        esClient.indexUpdateMany(new One(1, "hello", "world"));
//        esClient.indexUpdateMany(new One(2, "come in", "man"));
//        SearchRequest searchRequest = new SearchRequest();
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        searchRequest.source(sourceBuilder);
//        EzyEsSearchOneResponse<One> responseOne = esClient.searchOne(searchRequest, One.class);
//        System.out.println(responseOne);
//        EzyEsSearchManyResponse<One> responseMany = esClient.searchMany(searchRequest, One.class);
//        System.out.println(responseMany);
//        try {
//            highLevelClient.close();
//        }
//        catch(Exception e) {
//        }
//    }
//
//    private EzyBindingContext newEzyBindingContext() {
//        return EzyBindingContext.builder()
//                .scan("com.tvd12.ezydata.elasticsearch.testing.data")
//                .build();
//    }
//
//    private EzyIdFetchers newDataIdFetchers() {
//        return EzyIndexedDataIdFetchers.builder()
//                .scan("com.tvd12.ezydata.elasticsearch.testing.data")
//                .build();
//    }
//
//    private EzyIndexedDataClasses newIndexedDataClasses() {
//        return EzyIndexedDataClasses.builder()
//                .scan("com.tvd12.ezydata.elasticsearch.testing.data")
//                .build();
//    }
}
