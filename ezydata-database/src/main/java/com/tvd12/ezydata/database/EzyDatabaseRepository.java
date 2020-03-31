package com.tvd12.ezydata.database;

import com.tvd12.ezyfox.database.query.EzyFindAndModifyOptions;
import com.tvd12.ezyfox.database.query.EzyUpdateOperations;
import com.tvd12.ezyfox.database.repository.EzyEmptyRepository;
import com.tvd12.ezyfox.database.service.EzyCrudService;
import com.tvd12.ezyfox.function.EzyApply;

public interface EzyDatabaseRepository<I, E> extends 
		EzyEmptyRepository<I, E>, 
		EzyCrudService<I, E> {
	
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
	
	@Override
	default void updateOneById(I id, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default void updateOneById(I id, EzyApply<EzyUpdateOperations<E>> operations, boolean upsert) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations,
	        boolean upsert) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default void updateManyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations,
	        EzyApply<EzyFindAndModifyOptions> options) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		throw new UnsupportedOperationException("unsupport this operation");
	}

	@Override
	default E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations,
	        EzyApply<EzyFindAndModifyOptions> options) {
		throw new UnsupportedOperationException("unsupport this operation");
	}
	
	
	
}
