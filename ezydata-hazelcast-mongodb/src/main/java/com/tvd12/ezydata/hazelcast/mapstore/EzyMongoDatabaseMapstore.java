/**
 * 
 */
package com.tvd12.ezydata.hazelcast.mapstore;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.hazelcast.mapstore.EzyAbstractMapstore;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseAware;

import lombok.Setter;

/**
 * @author tavandung12
 *
 */
public abstract class EzyMongoDatabaseMapstore<K,V>
		extends EzyAbstractMapstore<K, V> 
		implements EzyMongoDatabaseAware {

	@Setter
    protected MongoDatabase database;
    
}
