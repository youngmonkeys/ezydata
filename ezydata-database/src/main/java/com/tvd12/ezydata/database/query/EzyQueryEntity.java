package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.util.EzyPair;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzyQueryEntity {

	protected final String name;
	protected final String value;
	@Setter
	protected boolean nativeQuery;
	@Setter
	protected Class<?> resultType;
	protected final String storageName;
	
	protected EzyQueryEntity(Builder builder) {
		this.name = builder.name;
		this.value = builder.value;
		this.resultType = builder.resultType;
		this.storageName = builder.storageName;
		this.nativeQuery = builder.nativeQuery;
		if(EzyStrings.isNoContent(name))
			throw new IllegalArgumentException("query name can't be null or empty");
		if(EzyStrings.isNoContent(value))
			throw new IllegalArgumentException("query value can't be null or empty");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj == this)
			return true;
		if(obj instanceof EzyPair)
			return name.equals(((EzyQueryEntity)obj).name);
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyQueryEntity> {
		
		protected String name;
		protected String value;
		protected String storageName;
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
		
		public Builder storageName(String storageName) {
			this.storageName = storageName;
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
