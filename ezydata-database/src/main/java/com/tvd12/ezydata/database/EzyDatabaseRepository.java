package com.tvd12.ezydata.database;

import com.tvd12.ezyfox.database.query.EzyFindAndModifyOptions;
import com.tvd12.ezyfox.database.query.EzyUpdateOperations;
import com.tvd12.ezyfox.database.repository.EzyEmptyRepository;
import com.tvd12.ezyfox.database.service.EzyCrudService;
import com.tvd12.ezyfox.function.EzyApply;

public interface EzyDatabaseRepository<I, E> extends 
		EzyEmptyRepository<I, E>, 
		EzyCrudService<I, E> {
	
	String PREFIX_FIND_ONE = "find";
	String PREFIX_FIND_LIST = "findList";
	String PREFIX_FETCH_ONE = "fetch";
	String PREFIX_FETCH_LIST = "fetchList";
	String PREFIX_UPDATE = "update";
	String PREFIX_DELETE = "delete";
	String PREFIX_COUNT = "count";
	
	@Deprecated
	@Override
	default void updateOneById(I id, E entity) {
		throw new UnsupportedOperationException("unsupport this operation");
	}
	
	@Deprecated
	@Override
	default void updateOneById(I id, E entity, boolean upsert) {
		throw new UnsupportedOperationException("unsupport this operation");
	}
	
	@Deprecated
	@Override
	default void updateOneByField(String field, Object value, E entity) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default void updateOneByField(String field, Object value, E entity, boolean upsert) {
		throw new UnsupportedOperationException("unsupport this operation");
	}
	
	@Deprecated
	@Override
	default void updateOneById(I id, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default void updateOneById(I id, EzyApply<EzyUpdateOperations<E>> operations, boolean upsert) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations,
	        boolean upsert) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default void updateManyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations,
	        EzyApply<EzyFindAndModifyOptions> options) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Deprecated
	@Override
	default E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations,
	        EzyApply<EzyFindAndModifyOptions> options) {
		throw new UnsupportedOperationException("unsupport this operation");
	}
	
	
	
}
