package com.tvd12.ezydata.jpa;

import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoriesImplementer;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
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
			addQuery(anno.name(), anno.query(), resultClass, false);
		}
		resultClasses = reflection.getAnnotatedClasses(NamedNativeQuery.class);
		for(Class<?> resultClass : resultClasses) {
			NamedNativeQuery anno = resultClass.getAnnotation(NamedNativeQuery.class);
			addQuery(anno.name(), anno.query(), resultClass, true);
		}
	}
	
	protected void addQuery(
			String name, String value, Class<?> resultClass, boolean nativeQuery) {
		super.addQuery(name, "", value, resultClass, nativeQuery);
	}
	
	@Override
	protected void bindResultType(EzyBindingContextBuilder builder, Class<?> resultType) {
		builder.addArrayBindingClass(resultType);
	}
	
}
