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
    protected final static Object[] EMPTY_ARRAY = new Object[0];

    protected EzyQLQuery(Builder builder) {
        this.query = builder.query;
        this.parameterConveter = getParameterConveter(builder);
        this.parameters = builder.parameters != null ? builder.parameters : EMPTY_ARRAY;
        this.value = createValue();
    }

    protected String createValue() {
        try {
            return EzyStrings.replace(query, parameters, parameterConveter);
        }
        catch (Exception e) {
            throw new EzyCreateQueryException("can not create query", e);
        }
    }

    protected Function<Object, Object> getParameterConveter(Builder builder) {
        return builder.parameterConveter;
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
                    parameters[i] = old[i];
            }
            return this;
        }

        public Builder parameter(int index, boolean value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, byte value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, char value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, double value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, float value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, int value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, long value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, short value) {
            return parameter(index, (Object)value);
        }

        public Builder parameter(int index, Object value) {
            parameterCount(index + 1);
            parameters[index] = value;
            return this;
        }

        public Builder parameterConveter(Function<Object, Object> parameterConveter) {
            this.parameterConveter = parameterConveter;
            return this;
        }

        @Override
        public EzyQLQuery build() {
            return new EzyQLQuery(this);
        }

    }

}
