package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.builder.EzyBuilder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EzyQueryConditionChain {
    protected final List<EzyQueryConditionGroup> conditionGroups;

    public EzyQueryConditionChain(
        List<EzyQueryConditionGroup> conditionGroups) {
        this.conditionGroups = conditionGroups;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int size() {
        return conditionGroups.size();
    }

    public int getParameterCount() {
        return conditionGroups.stream()
            .mapToInt(it -> it.size())
            .sum();
    }

    public static class Builder implements EzyBuilder<EzyQueryConditionChain> {
        protected final List<EzyQueryConditionGroup> conditionGroups = new ArrayList<>();

        public Builder addConditionGroup(EzyQueryConditionGroup conditionGroup) {
            this.conditionGroups.add(conditionGroup);
            return this;
        }

        @Override
        public EzyQueryConditionChain build() {
            return new EzyQueryConditionChain(conditionGroups);
        }
    }
}
