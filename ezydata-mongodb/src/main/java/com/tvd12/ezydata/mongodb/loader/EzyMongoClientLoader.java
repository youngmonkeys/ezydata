/**
 * 
 */
package com.tvd12.ezydata.mongodb.loader;

import com.mongodb.MongoClient;

public interface EzyMongoClientLoader {

    String PROPERTY_NAME_PREFIX = "database.mongo";
    String URI                = "database.mongo.uri";
    String HOST             = "database.mongo.host";
    String PORT             = "database.mongo.port";
    String USERNAME         = "database.mongo.username";
    String PASSWORD         = "database.mongo.password";
    String DATABASE         = "database.mongo.database";
    String COLLECTION_NAMING_CASE                   = "database.mongo.collection.naming.case";
    String COLLECTION_NAMING_IGNORED_SUFFIX           = "database.mongo.collection.naming.ignored_suffix";
    
    MongoClient load();
    
}
