package com.tvd12.ezydata.database.query;

import java.util.function.Function;

import com.tvd12.ezydata.database.exception.EzyCreateQueryException;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;

import lombok.Getter;

public class EzyQLQuery {

	@Getter
	protected final String query;
	@Getter
	protected final String value;
	protected final Object[] parameters;
	protected final Function<Object, Object> parameterConveter;
	
	public EzyQLQuery(
			String query,
			Object[] parameters, 
			Function<Object, Object> parameterConveter) {
		this.query = query;
		this.parameters = parameters;
		this.parameterConveter = parameterConveter;
		this.value = createValue();
	}
	
	public int getPrameterCount() {
		return parameters.length;
	}
	
	public Object getParameter(int index) {
		return parameters[index];
	}
	
	protected String createValue() {
		int paramCount = parameters.length;
		String[] strs = new String[paramCount];
		int startStr = 0;
		int length = query.length();
		for(int i = 0 ; i < length ; ) {
			char ch = query.charAt(i ++);
			if(i < length && ch == '?') {
				int numberCharCount = 0;
				char[] numberChars = new char[3];
				ch = query.charAt(i);
				while(ch >= '0' && ch <= '9') {
					numberChars[numberCharCount ++] = ch;
					if((++i) > length)
						break;
					ch = query.charAt(i);
				}
				if(numberCharCount > 0) {
					String paramStr = new String(numberChars, 0, numberCharCount);
					int paramIndex = Integer.parseInt(paramStr);
					if(paramIndex > paramCount)
						throw new EzyCreateQueryException("not enough parameter values, required: " + paramIndex);
					strs[paramIndex] = query.substring(startStr, i);
					startStr = i;
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < paramCount ; ++i) {
			String value = getParameterValue(parameters[i]);
			builder.append(strs[i].replace("?" + i, value));
		}
		if(startStr < length)
			builder.append(query.substring(startStr));
		return builder.toString();
	}
	
	protected String getParameterValue(Object parameter) {
		Object value = parameter;
		if(parameterConveter != null)
			value = parameterConveter.apply(parameter);
		if(value == null)
			return null;
		if(value instanceof String)
			return "'" + value + "'";
		return String.valueOf(value);
	}
	
	public String toString() {
		return new StringBuilder()
				.append("query: ").append(query).append("\n")
				.append("parameters: ").append(EzyStrings.join(parameters, ",")).append("\n")
				.append("value: ").append(value)
				.toString();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyQLQuery> {
		
		protected String query;
		protected Object[] parameters;
		protected Function<Object, Object> parameterConveter;
		
		public Builder query(String query) {
			this.query = query;
			return this;
		}
		
		public Builder parameterCount(int count) {
			if(parameters == null) {
				parameters = new Object[count];
			}
			else if(parameters.length < count) {
				Object[] old = parameters;
				parameters = new Object[count];
				for(int i = 0 ; i < old.length ; ++i)
					parameters[i] = old;
			}
			return this;
		}
		
		public Builder parameter(int index, Object value) {
			parameterCount(index + 1);
			parameters[index] = 1;
			return this;
		}
		
		public Builder parameterConveter(Function<Object, Object> parameterConveter) {
			this.parameterConveter = parameterConveter;
			return this;
		}
		
		@Override
		public EzyQLQuery build() {
			return new EzyQLQuery(query, parameters, parameterConveter);
		}
		
	}
	
}