package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;

import lombok.Getter;

@Getter
public class EzyQueryEntity {

	protected final String name;
	protected final String value;
	protected final boolean nativeQuery;
	protected final Class<?> resultType;
	
	protected EzyQueryEntity(Builder builder) {
		this.name = builder.name;
		this.value = builder.value;
		this.nativeQuery = builder.nativeQuery;
		this.resultType = builder.resultType;
		if(EzyStrings.isNoContent(name))
			throw new IllegalArgumentException("query name can't be null or empty");
		if(EzyStrings.isNoContent(value))
			throw new IllegalArgumentException("query value can't be null or empty");
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyQueryEntity> {
		
		protected String name;
		protected String value;
		protected boolean nativeQuery;
		protected Class<?> resultType;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder value(String value) {
			this.value = value;
			return this;
		}
		
		public Builder nativeQuery(boolean nativeQuery) {
			this.nativeQuery = nativeQuery;
			return this;
		}
		
		public Builder resultType(Class<?> resultType) {
			this.resultType = resultType;
			return this;
		}
		
		@Override
		public EzyQueryEntity build() {
			return new EzyQueryEntity(this);
		}
		
	}
	
}
