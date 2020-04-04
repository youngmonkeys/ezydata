package com.tvd12.ezydata.jpa.bean;

import java.util.List;

import javax.persistence.Query;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.jpa.EzyJpaRepository;
import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyMethod;

public class EzyJpaRepositoryImplementer extends EzyAbstractRepositoryImplementer {

	public EzyJpaRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}
	
	@Override
	protected String makeAbstractMethodContent(EzyMethod method) {
		EzyQuery anno = method.getAnnotation(EzyQuery.class);
		String queryString = anno.value();
		EzyBody body = new EzyFunction(method).body();
		EzyInstruction createQueryInstruction = new EzyInstruction("\t", "\n")
				.variable(Query.class)
				.equal()
				.append("this.entityManager.createQuery(").string(queryString)
				.append(")");
		body.append(createQueryInstruction);
		for(int i = 0 ; i < method.getParameterCount() ; ++i) {
			body.append(new EzyInstruction("\t", "\n")
					.append("query.setParameter(")
						.append(i).append(",").append("arg").append(i)
					.append(")"));
		}

		String methodName = method.getName();
		Class<?> resultType = anno.resultType();
		Class<?> returnType = method.getReturnType();
		EzyInstruction answerInstruction = new EzyInstruction("\t", "\n");
		if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND_LIST) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_LIST)) {
			if(resultType == Object.class || resultType == entityType) {
				answerInstruction.answer().cast(returnType, "query.getResultList()");
			}
			else {	
				body.append(new EzyInstruction("\t", "\n")
						.variable(List.class, "result")
							.equal()
						.append("query.getResultList()"));
				answerInstruction.answer()
					.append("this.databaseContext.deserializeResultList(result,")
					.clazz(resultType, true).append(")");
			}
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND_ONE) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_ONE)) {
			if(resultType == Object.class || resultType == entityType) {
				answerInstruction.answer().cast(returnType, "query.getSingleResult()");
			}
			else {
				body.append(new EzyInstruction("\t", "\n")
						.variable(List.class, "result")
							.equal()
						.append("query.getResultList()"));
				answerInstruction
					.variable(Object.class, "answer")
						.equal()
					.append("this.databaseContext.deserializeResult(result,")
						.clazz(resultType, true).append(")");
				body.append(answerInstruction);
				answerInstruction = new EzyInstruction("\t", "\n")
						.answer().cast(returnType, "answer");
			}
		}
		body.append(answerInstruction);
		return body.function().toString();
	}
	
	@Override
	protected void setRepoComponent(Object repo, Object template) {
		EzyDatabaseContext databaseContext = (EzyDatabaseContext)template;
		EzyDatabaseContextAware databaseContextAware = (EzyDatabaseContextAware)repo; 
		databaseContextAware.setDatabaseContext(databaseContext);
	}
	
	@Override
	protected Class<?> getSuperClass() {
		return EzyJpaRepository.class;
	}
	
}
