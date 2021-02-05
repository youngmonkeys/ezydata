package com.tvd12.ezydata.hazelcast.transaction.impl;

import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionalMap;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezyfox.function.EzyExceptionFunction;

public class EzySimpleMapReturnTransaction<K,V,R>
		extends EzySimpleTransaction
		implements EzyMapReturnTransaction<K, V, R> {

	protected final String mapName;
	
	public EzySimpleMapReturnTransaction(
			TransactionContext context, String mapName) {
		super(context);
		this.mapName = mapName;
	}
	
	@Override
	public R apply(EzyExceptionFunction<TransactionalMap<K, V>, R> func) throws Exception {
		TransactionalMap<K, V> map = context.getMap(mapName);
		return func.apply(map);
	}
	
}
