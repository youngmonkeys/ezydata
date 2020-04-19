package com.tvd12.ezydata.database.query;

import java.util.function.Function;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyQLQueryFactory {

	protected final Function<Object, Object> parameterConveter;
	
	public EzyQLQueryFactory() {
		this(null);
	}
	
	public EzyQLQueryFactory(Function<Object, Object> parameterConveter) {
		this.parameterConveter = parameterConveter;
	}
	
	public EzyQLQuery.Builder newQueryBuilder() {
		return EzyQLQuery.builder()
				.parameterConveter(parameterConveter);
	}
	
	public EzyQLQuery.Builder newQueryBuilder(int parameterCount) {
		return EzyQLQuery.builder()
				.parameterCount(parameterCount)
				.parameterConveter(parameterConveter);
	}
	
	public EzyQLQuery newQuery(String query, Object... parameters) {
		return newQueryBuilder(parameters.length)
				.query(query)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyQLQueryFactory> {
		
		protected Function<Object, Object> parameterConveter;
		
		public Builder parameterConveter(Function<Object, Object> parameterConveter) {
			this.parameterConveter = parameterConveter;
			return this;
		}
		
		@Override
		public EzyQLQueryFactory build() {
			return new EzyQLQueryFactory(parameterConveter);
		}
		
	}
	
}
