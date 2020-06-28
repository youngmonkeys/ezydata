package com.tvd12.ezydata.jpa.bean;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.annotation.EzyTransactional;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.jpa.repository.EzyJpaRepository;
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
		String queryString = getQueryString(method);
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
			body.append(new EzyInstruction("\t", "\n")
					.variable(List.class, "result")
						.equal()
					.append("query.getResultList()"));
			if(resultType == Object.class || resultType == entityType) {
				body.append(new EzyInstruction("\t", "\n")
					.variable(Object.class, "answer")
					.append(" = null"));
				body.append(new EzyInstruction("\t", "\n", false)
					.append("if(result.size() > 0)"));
				body.append(new EzyInstruction("\t\t", "\n")
						.append("answer = result.get(0)"));
				answerInstruction = new EzyInstruction("\t", "\n")
						.answer().cast(returnType, "answer");
			}
			else {
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
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_COUNT)) {
			if(returnType != int.class && returnType != long.class)
				throw new IllegalArgumentException("count method must return int or long, error method: " + method);
			body.append(new EzyInstruction("\t", "\n")
					.variable(Object.class, "answer")
						.equal()
					.append("query.getSingleResult()"));
			answerInstruction.answer()
				.cast(Number.class, "answer").dot()
				.append(returnType == long.class ? "longValue()" : "intValue()");
			
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_UPDATE) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_DELETE)) {
			if(returnType != int.class && returnType != void.class)
				throw new IllegalArgumentException("update or delete method must return int or void, error method: " + method);
			body.append(new EzyInstruction("\t", "\n")
					.variable(int.class, "answer").equal().append("0"));
			Transactional transAnno = method.getAnnotation(Transactional.class);
			EzyTransactional etransAnno = method.getAnnotation(EzyTransactional.class);
			if(transAnno != null || etransAnno != null) {
				transactionAppend(body, () -> {
					body.append(new EzyInstruction("\t\t", "\n")
							.append("answer = query.executeUpdate()"));
				});
			}
			else {
				body.append(new EzyInstruction("\t", "\n")
						.append("answer = query.executeUpdate()"));
			}
			answerInstruction.answer().append("answer");
		}
		else {
			throw newInvalidMethodException(method);
		}
		if(returnType != void.class)
			body.append(answerInstruction);
		return body.function().toString();
	}
	
	protected void transactionAppend(EzyBody body, Runnable content) {
		body.append(new EzyInstruction("\t", "\n")
				.variable(EntityTransaction.class, "transaction")
				.equal()
				.append("this.entityManager.getTransaction()"));
		body.append(new EzyInstruction("\t", "\n")
				.append("transaction.begin()"));
		body.append(new EzyInstruction("\t", "\n", false)
				.append("try {"));
		content.run();
		body.append(new EzyInstruction("\t\t", "\n")
				.append("transaction.commit()"));
		body.append(new EzyInstruction("\t", "\n", false)
				.append("}"));
		body.append(new EzyInstruction("\t", "\n", false)
				.append("catch(Exception e) {"));
		body.append(new EzyInstruction("\t\t", "\n")
				.append("transaction.rollback()"));
		body.append(new EzyInstruction("\t\t", "\n")
				.append("throw e"));
		body.append(new EzyInstruction("\t", "\n", false)
				.append("}"));
	}
	
	@Override
	protected Class<?> getSuperClass() {
		return EzyJpaRepository.class;
	}
	
}
