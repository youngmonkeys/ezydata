package com.tvd12.ezydata.jpa;

import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoriesImplementer;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyReflection;

public class EzyJpaDatabaseContextBuilder 
		extends EzyDatabaseContextBuilder<EzyJpaDatabaseContextBuilder> {

	protected EntityManagerFactory entityManagerFactory;
	
	public EzyJpaDatabaseContextBuilder 
			entityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		return this;
	}
	
	@Override
	protected EzySimpleDatabaseContext newDatabaseContext() {
		EzySimpleJpaDatabaseContext context = new EzySimpleJpaDatabaseContext();
		context.setEntityManagerFactory(entityManagerFactory);
		return context;
	}
	
	@Override
	protected EzyAbstractRepositoriesImplementer newRepositoriesImplementer() {
		return new EzyJpaRepositoriesImplementer();
	}
	
	@Override
	protected void scanAndAddQueries(EzyReflection reflection) {
		Set<Class<?>> resultClasses = reflection.getAnnotatedClasses(NamedQuery.class);
		for(Class<?> resultClass : resultClasses) {
			NamedQuery anno = resultClass.getAnnotation(NamedQuery.class);
			String queryName = anno.name();
			EzyQueryEntity queryEntity = getQuery(queryName, anno.query(), resultClass);
			queryEntity.setNativeQuery(false);
		}
		resultClasses = reflection.getAnnotatedClasses(NamedNativeQuery.class);
		for(Class<?> resultClass : resultClasses) {
			NamedNativeQuery anno = resultClass.getAnnotation(NamedNativeQuery.class);
			String queryName = anno.name();
			EzyQueryEntity queryEntity = getQuery(queryName, anno.query(), resultClass);
			queryEntity.setNativeQuery(true);
		}
	}
	
	protected EzyQueryEntity getQuery(String name, String value, Class<?> resultClass) {
		String queryName = name;
		String queryValue = value;
		if(EzyStrings.isNoContent(name))
			queryName = resultClass.getName();
		EzyQueryEntity queryEntity = queryManager.getQuery(queryName);
		if(queryEntity != null) {
			if(queryEntity.getResultType() != resultClass)
				throw new IllegalStateException("too many result type of query: " + queryName + "(" + queryEntity.getResultType().getName() + ", " + resultClass.getName() + ")");
		}
		else {
			if(EzyStrings.isNoContent(queryValue))
				throw new IllegalStateException("has no query with name: " + queryName);
			queryEntity = EzyQueryEntity.builder()
					.name(queryName)
					.value(queryValue)
					.resultType(resultClass)
					.build();
			queryManager.addQuery(queryEntity);
		}
		return queryEntity;
	}
	
	@Override
	protected void bindResultType(EzyBindingContextBuilder builder, Class<?> resultType) {
		builder.addArrayBindingClass(resultType);
	}
	
}
