package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.builder.EzyBuilder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EzyQueryConditionGroup {
    protected final List<EzyQueryCondition> conditions;

    public EzyQueryConditionGroup(List<EzyQueryCondition> conditions) {
        this.conditions = conditions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int size() {
        return conditions.size();
    }

    public static class Builder implements EzyBuilder<EzyQueryConditionGroup> {
        protected final List<EzyQueryCondition> conditions = new ArrayList<>();

        public Builder addCondition(EzyQueryCondition condition) {
            this.conditions.add(condition);
            return this;
        }

        @Override
        public EzyQueryConditionGroup build() {
            return new EzyQueryConditionGroup(conditions);
        }
    }
}
