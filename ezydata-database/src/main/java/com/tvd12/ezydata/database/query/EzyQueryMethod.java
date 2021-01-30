package com.tvd12.ezydata.database.query;

import static com.tvd12.ezydata.database.EzyDatabaseRepository.PREFIX_COUNT_BY;
import static com.tvd12.ezydata.database.EzyDatabaseRepository.PREFIX_FIND_BY;

import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.util.Next;

import lombok.Getter;

@Getter
public class EzyQueryMethod {
	protected final EzyMethod method;
	protected final EzyQueryMethodType type;
	protected final EzyQueryConditionChain conditionChain;
	
	protected final static String OR = "Or";
	protected final static String AND = "And";
	
	public EzyQueryMethod(EzyMethod method) {
		this.method = method;
		this.type = getType(method);
		this.conditionChain = getConditionChain(method);
		this.postConstruct();
	}
	
	protected void postConstruct() {
		int requiredCount = conditionChain.getParameterCount();
		int actualCount = getQueryParameterCount(method);
		if(requiredCount != actualCount) {
			throw new IllegalArgumentException(
					"invalid query method: " + method + 
					" not enough parameter (expected: " + requiredCount +
					", actual: " + actualCount + ")"
			);
		}
	}
	
	private static EzyQueryMethodType getType(EzyMethod method) {
		String methodName = method.getName();
		if(methodName.startsWith(PREFIX_COUNT_BY))
			return EzyQueryMethodType.COUNT;
		return EzyQueryMethodType.FIND;
	}
	
	private static EzyQueryConditionChain getConditionChain(EzyMethod method) {
		String methodName = method.getName();
		String chain = methodName;
		if(methodName.startsWith(PREFIX_COUNT_BY))
			chain = methodName.substring(PREFIX_COUNT_BY.length());
		else if(methodName.startsWith(PREFIX_FIND_BY))
			chain = methodName.substring(PREFIX_FIND_BY.length());
		
		EzyQueryConditionChain.Builder conditionChainBuilder = 
				EzyQueryConditionChain.builder();
		if(chain.isEmpty())
			return conditionChainBuilder.build();
		
		String[] conditionGroupStrings = chain.split(OR);
		
		for(String conditionGroupString : conditionGroupStrings) {
			String[] conditionStrings = conditionGroupString.split(AND);
			EzyQueryConditionGroup.Builder conditionGroupBuilder = EzyQueryConditionGroup.builder();
			
			for(String conditionString : conditionStrings)
				conditionGroupBuilder.addCondition(EzyQueryCondition.parse(conditionString));

			conditionChainBuilder.addConditionGroup(conditionGroupBuilder.build());
		}
		return conditionChainBuilder.build();
	}
	
	public static int getQueryParameterCount(EzyMethod method) {
		if(isPaginationMethod(method))
			return method.getParameterCount() - 1;
		return method.getParameterCount();
	}
	
	public static boolean isPaginationMethod(EzyMethod method) {
		int paramCount = method.getParameterCount();
		if(paramCount > 0) {
			Class<?> lastParamType = method.getParameterTypes()[paramCount - 1];
			if(Next.class.isAssignableFrom(lastParamType))
				return true;
		}
		return false;
	}
}
