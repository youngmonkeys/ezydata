package com.tvd12.ezydata.database.query;

public interface EzyQueryMethodConverter {

    @SuppressWarnings("rawtypes")
    String toQueryString(Class entityClass, EzyQueryMethod method);

}
