package com.tvd12.ezydata.jpa.bean;

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
				.append("this.entityManager.createQuery(").string(queryString);
		Class<?> resultType = anno.resultType();
		if(resultType != Object.class)
			createQueryInstruction.append(",").clazz(resultType, true);
		createQueryInstruction.append(")");
		body.append(createQueryInstruction);
		for(int i = 0 ; i < method.getParameterCount() ; ++i) {
			body.append(new EzyInstruction("\t", "\n")
					.append("query.setParameter(")
						.append(i).append(",").append("arg").append(i)
					.append(")"));
		}

		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		EzyInstruction resultInstruction = new EzyInstruction("\t", "\n")
				.append("java.lang.Object answer = ");
		if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND)) {
			if(Iterable.class.isAssignableFrom(returnType))
				resultInstruction.append("query.getResultList()");
			else
				resultInstruction.append("query.getSingleResult()");
		}
		body.append(resultInstruction);
		body.append(new EzyInstruction("\t", "\n")
				.answer().cast(returnType, "answer"));
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
