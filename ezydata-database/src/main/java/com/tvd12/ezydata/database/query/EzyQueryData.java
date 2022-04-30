package com.tvd12.ezydata.database.query;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EzyQueryData extends EzyQLQuery {

    @Getter
    private final Map<String, Object> parameterMap;

    protected EzyQueryData(Builder builder) {
        super(builder);
        this.parameterMap = builder.parameterMap != null
            ? builder.parameterMap
            : Collections.emptyMap();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected String createValue() {
        return query;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public static class Builder extends EzyQLQuery.Builder {

        private Map<String, Object> parameterMap;

        @Override
        public Builder query(String query) {
            return (Builder) super.query(query);
        }

        public Builder parameterCount(int count) {
            return (Builder) super.parameterCount(count);
        }

        public Builder parameter(String name, Object value) {
            if (parameterMap == null) {
                this.parameterMap = new HashMap<>();
            }
            this.parameterMap.put(name, value);
            return this;
        }

        public Builder parameter(int index, boolean value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, byte value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, char value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, double value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, float value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, int value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, long value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, short value) {
            return (Builder) super.parameter(index, (Object) value);
        }

        public Builder parameter(int index, Object value) {
            return (Builder) super.parameter(index, value);
        }

        public Builder parameters(Map<String, Object> parameters) {
            if (parameterMap == null) {
                this.parameterMap = new HashMap<>();
            }
            this.parameterMap.putAll(parameters);
            return this;
        }

        public Builder parameters(List<Object> parameters) {
            int parameterCount = 0;
            for (Object param : parameters) {
                if (param != null) {
                    ++parameterCount;
                }
            }
            parameterCount(parameterCount);
            for (int paramIndex = 0, i = 0; i < parameters.size(); ++i) {
                Object param = parameters.get(i);
                if (param != null) {
                    parameter(paramIndex++, param);
                }
            }
            return this;
        }

        public Builder parameterConverter(Function<Object, Object> parameterConverter) {
            return (Builder) super.parameterConverter(parameterConverter);
        }

        @Override
        public EzyQueryData build() {
            return new EzyQueryData(this);
        }
    }
}
