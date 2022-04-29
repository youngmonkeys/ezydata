package com.tvd12.ezydata.database.repository;

public interface EzyMaxIdRepository {

     Long incrementAndGet(String key);
        
     Long incrementAndGet(String key, int delta);
    
}
