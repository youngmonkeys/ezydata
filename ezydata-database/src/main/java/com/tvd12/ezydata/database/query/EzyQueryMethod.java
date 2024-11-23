package com.tvd12.ezydata.database.query;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.util.Next;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.tvd12.ezydata.database.EzyDatabaseRepository.*;

@Getter
public class EzyQueryMethod {

    protected final EzyMethod method;
    protected final EzyQueryMethodType type;
    protected final EzyQueryConditionChain conditionChain;

    protected static final String OR = "Or";
    protected static final String AND = "And";

    public EzyQueryMethod(EzyMethod method) {
        this.method = method;
        this.type = getType(method);
        this.conditionChain = getConditionChain(method);
        this.postConstruct();
    }

    private static EzyQueryMethodType getType(EzyMethod method) {
        EzyQueryMethodType answer = EzyQueryMethodType.FIND;
        String methodName = method.getName();
        for (EzyQueryMethodType it : EzyQueryMethodType.values()) {
            if (methodName.startsWith(it.getPrefix())) {
                answer = it;
            }
        }
        return answer;
    }

    private static EzyQueryConditionChain getConditionChain(EzyMethod method) {
        String methodName = method.getName();
        String chain = EzyStrings.EMPTY_STRING;
        if (methodName.startsWith(PREFIX_COUNT_BY)) {
            chain = methodName.substring(PREFIX_COUNT_BY.length());
        } else if (methodName.startsWith(PREFIX_FIND_BY)) {
            chain = methodName.substring(PREFIX_FIND_BY.length());
        } else if (methodName.startsWith(PREFIX_DELETE_BY)) {
            chain = methodName.substring(PREFIX_DELETE_BY.length());
        }

        EzyQueryConditionChain.Builder conditionChainBuilder =
            EzyQueryConditionChain.builder();
        if (chain.isEmpty()) {
            return conditionChainBuilder.build();
        }

        List<String> conditionGroupStrings = splitConditions(chain, OR);

        for (String conditionGroupString : conditionGroupStrings) {
            List<String> conditionStrings = splitConditions(conditionGroupString, AND);
            EzyQueryConditionGroup.Builder conditionGroupBuilder = EzyQueryConditionGroup.builder();

            for (String conditionString : conditionStrings) {
                conditionGroupBuilder.addCondition(EzyQueryCondition.parse(conditionString));
            }

            conditionChainBuilder.addConditionGroup(conditionGroupBuilder.build());
        }
        return conditionChainBuilder.build();
    }

    public static List<String> splitConditions(
        String chain,
        String operator
    ) {
        int fromIndex = 0;
        String remaining = chain;
        List<String> conditions = new ArrayList<>();
        while (true) {
            int index = remaining.indexOf(operator, fromIndex);
            if (index <= 0) {
                break;
            }
            int afterIndex = index + operator.length();
            if (afterIndex >= remaining.length()) {
                break;
            }
            char afterOperatorChar = remaining.charAt(afterIndex);
            if (Character.isUpperCase(afterOperatorChar)) {
                conditions.add(remaining.substring(0, index));
                fromIndex = 0;
                remaining = remaining.substring(afterIndex);
            } else {
                fromIndex = afterIndex;
            }
        }
        conditions.add(remaining);
        return conditions;
    }

    public static int getQueryParameterCount(EzyMethod method) {
        if (isPaginationMethod(method)) {
            return method.getParameterCount() - 1;
        }
        return method.getParameterCount();
    }

    public static boolean isPaginationMethod(EzyMethod method) {
        int paramCount = method.getParameterCount();
        if (paramCount > 0) {
            Class<?> lastParamType = method.getParameterTypes()[paramCount - 1];
            return Next.class.isAssignableFrom(lastParamType);
        }
        return false;
    }

    protected void postConstruct() {
        int requiredCount = conditionChain.getParameterCount();
        int actualCount = getQueryParameterCount(method);
        if (requiredCount != actualCount) {
            throw new IllegalArgumentException(
                "invalid query method: " + method +
                    " not enough parameter (expected: " + requiredCount +
                    ", actual: " + actualCount + ")"
            );
        }
    }
}
