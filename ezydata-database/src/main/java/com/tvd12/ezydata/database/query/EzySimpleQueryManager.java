package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.util.EzyLoggable;

import java.util.HashMap;
import java.util.Map;

public class EzySimpleQueryManager
    extends EzyLoggable implements EzyQueryRegister {

    protected final Map<String, EzyQueryEntity> queries;

    public EzySimpleQueryManager() {
        this.queries = new HashMap<>();
    }

    @Override
    public EzyQueryEntity getQuery(String name) {
        EzyQueryEntity query = queries.get(name);
        return query;
    }

    @Override
    public Map<String, EzyQueryEntity> getQueries() {
        return new HashMap<>(queries);
    }

    @Override
    public void addQuery(EzyQueryEntity query) {
        this.queries.put(query.getName(), query);
    }

}
