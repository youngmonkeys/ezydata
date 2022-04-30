package com.tvd12.ezydata.jpa.query;

import com.tvd12.ezydata.database.query.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EzyJpaQueryMethodConverter
    implements EzyQueryMethodConverter {

    @SuppressWarnings("rawtypes")
    @Override
    public String toQueryString(
        Class entityClass,
        EzyQueryMethod method
    ) {
        StringBuilder builder = new StringBuilder();
        if (method.getType() == EzyQueryMethodType.DELETE) {
            builder.append("DELETE");
        } else {
            builder.append("SELECT ");
        }

        if (method.getType() == EzyQueryMethodType.COUNT) {
            builder.append("COUNT(e)");
        } else if (method.getType() != EzyQueryMethodType.DELETE) {
            builder.append("e");
        }

        builder.append(" FROM ")
            .append(entityClass.getSimpleName()).append(" e");

        EzyQueryConditionChain conditionChain = method.getConditionChain();
        if (conditionChain.size() > 0) {
            builder.append(" ")
                .append("WHERE");
            convert(builder, conditionChain);
        }
        return builder.toString();
    }

    private void convert(
        StringBuilder builder,
        EzyQueryConditionChain conditionChain
    ) {
        List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
        AtomicInteger parameterCount = new AtomicInteger();
        for (int i = 0; i < conditionGroups.size(); ++i) {
            builder.append(" ");
            convert(builder, parameterCount, conditionGroups.get(i));
            if (i < conditionGroups.size() - 1) {
                builder.append(" OR");
            }
        }
    }

    private void convert(
        StringBuilder builder,
        AtomicInteger parameterCount,
        EzyQueryConditionGroup conditionGroup
    ) {
        List<EzyQueryCondition> conditions = conditionGroup.getConditions();
        if (conditions.size() > 1) {
            builder.append("(");
        }
        for (int i = 0; i < conditions.size(); ++i) {
            convert(builder, parameterCount, conditions.get(i));
            if (i < conditions.size() - 1) {
                builder.append(" AND ");
            }
        }
        if (conditions.size() > 1) {
            builder.append(")");
        }
    }

    private void convert(
        StringBuilder builder,
        AtomicInteger parameterCount,
        EzyQueryCondition condition
    ) {
        builder.append("e.")
            .append(condition.getField())
            .append(" ")
            .append(condition.getOperation().getSign())
            .append(" ")
            .append("?")
            .append(parameterCount.getAndIncrement());
    }
}
