package com.tvd12.ezydata.mongodb.query;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezydata.database.query.EzyQueryCondition;
import com.tvd12.ezydata.database.query.EzyQueryConditionChain;
import com.tvd12.ezydata.database.query.EzyQueryConditionGroup;
import com.tvd12.ezydata.database.query.EzyQueryMethod;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzyQueryOperation;

public class EzyMongoQueryMethodConverter 
		implements EzyQueryMethodConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public String toQueryString(
			Class entityClass, EzyQueryMethod method) {
		StringBuilder builder = new StringBuilder();
		EzyQueryConditionChain conditionChain = method.getConditionChain();
		if(conditionChain.size() > 0) {
			convert(builder, conditionChain);
		}
		return builder.toString();
	}
	
	private void convert(
			StringBuilder builder, 
			EzyQueryConditionChain conditionChain) {
		List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
		AtomicInteger parameterCount = new AtomicInteger();
		if(conditionGroups.size() > 1)
			builder.append("{$or:[");
		for(int i = 0 ; i < conditionGroups.size() ; ++i) {
			convert(builder, parameterCount, conditionGroups.get(i));
			if(i < conditionGroups.size() - 1)
				builder.append(",");
		}
		if(conditionGroups.size() > 1)
			builder.append("]}");
	}
	
	private void convert(
			StringBuilder builder, 
			AtomicInteger parameterCount,
			EzyQueryConditionGroup conditionGroup) {
		List<EzyQueryCondition> conditions = conditionGroup.getConditions();
		if(conditions.size() > 1)
			builder.append("{$and:[");
		for(int i = 0 ; i < conditions.size() ; ++i) {
			convert(builder, parameterCount, conditions.get(i));
			if(i < conditions.size() - 1)
				builder.append(",");
		}
		if(conditions.size() > 1)
			builder.append("]}");
	}
	
	private void convert(
			StringBuilder builder, 
			AtomicInteger parameterCount,
			EzyQueryCondition condition) {
		builder
			.append("{")
			.append(condition.getField());
		for (EzyQueryOperation it : EzyQueryOperation.notIncludeEqualValues()) {
		    if(condition.getOperation() == it) {
	            builder.append(":{$" + it.getSignName());
	            break;
	        }
		}
		builder.append(":?")
			.append(parameterCount.getAndIncrement())
			.append("}");
		for (EzyQueryOperation it : EzyQueryOperation.notIncludeEqualValues()) {
            if(condition.getOperation() == it) {
                builder.append("}");
                break;
            }
        }
	}
}
