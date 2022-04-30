package com.tvd12.ezydata.database.query;

import com.tvd12.ezydata.database.exception.EzyCreateQueryException;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;
import lombok.Getter;

import java.util.function.Function;

public class EzyQLQuery {

    @Getter
    protected final String query;
    @Getter
    protected final String value;
    protected final Object[] parameters;
    protected final Function<Object, Object> parameterConverter;

    protected static final Object[] EMPTY_ARRAY = new Object[0];

    protected EzyQLQuery(Builder builder) {
        this.query = builder.query;
        this.parameterConverter = getParameterConverter(builder);
        this.parameters = builder.parameters != null ? builder.parameters : EMPTY_ARRAY;
        this.value = createValue();
    }

    public static Builder builder() {
        return new Builder();
    }

    protected String createValue() {
        try {
            return EzyStrings.replace(query, parameters, parameterConverter);
        } catch (Exception e) {
            throw new EzyCreateQueryException("can not create query", e);
        }
    }

    protected Function<Object, Object> getParameterConverter(Builder builder) {
        return builder.parameterConverter;
    }

    public String toString() {
        return "query: " + query + "\n" +
            "parameters: " + EzyStrings.join(parameters, ",") + "\n" +
            "value: " + value;
    }

    public static class Builder implements EzyBuilder<EzyQLQuery> {

        protected String query;
        protected Object[] parameters;
        protected Function<Object, Object> parameterConverter;

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder parameterCount(int count) {
            if (parameters == null) {
                parameters = new Object[count];
            } else if (parameters.length < count) {
                Object[] old = parameters;
                parameters = new Object[count];
                System.arraycopy(old, 0, parameters, 0, old.length);
            }
            return this;
        }

        public Builder parameter(int index, boolean value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, byte value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, char value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, double value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, float value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, int value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, long value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, short value) {
            return parameter(index, (Object) value);
        }

        public Builder parameter(int index, Object value) {
            parameterCount(index + 1);
            parameters[index] = value;
            return this;
        }

        public Builder parameterConverter(Function<Object, Object> parameterConverter) {
            this.parameterConverter = parameterConverter;
            return this;
        }

        @Override
        public EzyQLQuery build() {
            return new EzyQLQuery(this);
        }
    }
}
