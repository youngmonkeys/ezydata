package com.tvd12.ezydata.mongodb.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.mongodb.repository.EzySimpleMongoRepository;
import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyMethod;

public class EzyMongoRepositoryImplementer 
		extends EzyAbstractRepositoryImplementer  {
	
	public EzyMongoRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}
	
	@Override
	protected String makeAbstractMethodContent(EzyMethod method) {
		EzyQuery anno = method.getAnnotation(EzyQuery.class);
		String queryString = anno.value();
		if(EzyStrings.isNoContent(queryString)) {
			String queryName = anno.name();
			if(EzyStrings.isNoContent(queryName))
				throw new IllegalArgumentException("query name can not be null on method: " + method.getName());
			EzyQueryEntity query = queryManager.getQuery(queryName);
			if(query == null)
				throw new IllegalArgumentException("not found query with name: " + queryName + " on method: " + method.getName());
			queryString = query.getValue();
		}
		EzyBody body = new EzyFunction(method).body();
		EzyInstruction createQueryInstruction = new EzyInstruction("\t", "\n", false)
				.variable(EzyQLQuery.class, "query")
				.equal()
				.append("this.newQueryBuilder()")
				.append("\n\t\t.parameterCount(").append(method.getParameterCount()).append(")")
				.append("\n\t\t.query(").string(queryString).append(")");
		body.append(createQueryInstruction);
		for(int i = 0 ; i < method.getParameterCount() ; ++i) {
			body.append(new EzyInstruction("\t\t", "\n", false)
					.append(".parameter(")
						.append(i).append(",").append("arg").append(i)
					.append(")"));
		}
		body.append(new EzyInstruction("\t\t", "\n").append(".build()"));

		String methodName = method.getName();
		Class<?> resultType = anno.resultType();
		Class<?> returnType = method.getReturnType();
		EzyInstruction answerInstruction = new EzyInstruction("\t", "\n");
		if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND_ONE) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND_LIST)) {
			if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND_LIST))
				answerInstruction.answer().cast(returnType, "this.findListWithQuery(query)");
			else	
				answerInstruction.answer().cast(entityType, "this.findOneWithQuery(query)");
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_ONE) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_LIST)) {
			if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_LIST))
				answerInstruction.answer().cast(returnType, 
						"this.fetchListWithQuery(query," + resultType.getName() + ".class)");
			else	
				answerInstruction.answer().cast(resultType, 
						"this.fetchOneWithQuery(query," + resultType.getName() + ".class)");
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_UPDATE) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_DELETE)) {
			if(returnType != int.class && resultType != void.class)
				throw new IllegalArgumentException("update or delete method must return int or void, error method: " + method);
			body.append(new EzyInstruction("\t", "\n")
					.variable(int.class, "answer").equal().append("0"));
			if(methodName.startsWith(EzyDatabaseRepository.PREFIX_UPDATE)) {
				body.append(new EzyInstruction("\t", "\n")
						.append("answer = query.updateWithQuery()"));
			}
			else {
				body.append(new EzyInstruction("\t", "\n")
						.append("answer = query.deleteWithQuery()"));
			}
			answerInstruction.answer().append("answer");
		}
		else {
			throw new IllegalArgumentException("method name must start with: " + 
					EzyDatabaseRepository.PREFIX_FIND_ONE + ", " +
					EzyDatabaseRepository.PREFIX_FIND_LIST + ", " + 
					EzyDatabaseRepository.PREFIX_FETCH_ONE  + ", " +
					EzyDatabaseRepository.PREFIX_FETCH_LIST + ", " +
					EzyDatabaseRepository.PREFIX_UPDATE + " or " +
					EzyDatabaseRepository.PREFIX_DELETE);
		}
		if(returnType != void.class)
			body.append(answerInstruction);
		return body.function().toString();
	}

	@Override
	protected Class<?> getBaseRepositoryInterface() {
		return EzyMongoRepository.class;
	}

	@Override
	protected Class<?> getSuperClass() {
		return EzySimpleMongoRepository.class;
	}
	
}
