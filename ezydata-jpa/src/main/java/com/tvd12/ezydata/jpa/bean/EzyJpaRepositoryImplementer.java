package com.tvd12.ezydata.jpa.bean;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyTransactional;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.query.EzyQueryString;
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
		EzyQueryString queryString = getQueryString(method);
		EzyBody body = new EzyFunction(method).body();
		EzyInstruction createQueryInstruction = new EzyInstruction("\t", "\n")
				.variable(Query.class)
				.equal();
		if(queryString.isNativeQuery()) {
			createQueryInstruction
				.append("this.entityManager.createNativeQuery(");
		}
		else {
			createQueryInstruction
				.append("this.entityManager.createQuery(");
		}
		createQueryInstruction
			.string(queryString.getQueryString());
		
		Class<?> resultType = getResultType(method);
		
		if(queryString.isNativeQuery() && resultType.equals(entityType)) {
			createQueryInstruction.append(",")
				.clazz(resultType, true);
		}
		createQueryInstruction.append(")");
		body.append(createQueryInstruction);
		boolean isPagination = isPaginationMethod(method);
		int paramCount = method.getParameterCount();
		if(isPagination)
			-- paramCount;
		for(int i = 0 ; i < paramCount ; ++i) {
			body.append(new EzyInstruction("\t", "\n")
					.append("query.setParameter(")
						.append(i).append(",").append("arg").append(i)
					.append(")"));
		}

		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		
		EzyInstruction answerInstruction = new EzyInstruction("\t", "\n");
		if(methodName.startsWith(EzyDatabaseRepository.PREFIX_COUNT)) {
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
			if(Iterable.class.isAssignableFrom(returnType)) {
				if(isPagination) {
					String nextArg = "arg" + paramCount;
					body.append(new EzyInstruction("\t", "\n")
							.append("query.setFirstResult((int)" + nextArg + ".getSkip())"));
					body.append(new EzyInstruction("\t", "\n")
							.append("query.setMaxResults((int)" + nextArg + ".getLimit())"));
				}
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
			else {
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
