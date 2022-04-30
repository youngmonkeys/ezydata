package com.tvd12.ezydata.jpa;

import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.jpa.bean.EzyJpaRepositoriesImplementer;
import com.tvd12.ezydata.jpa.query.EzyJpaQueryMethodConverter;
import com.tvd12.ezyfox.reflect.EzyReflection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import java.util.Set;

public class EzyJpaDatabaseContextBuilder
    extends EzyDatabaseContextBuilder<EzyJpaDatabaseContextBuilder> {

    protected EntityManagerFactory entityManagerFactory;

    public EzyJpaDatabaseContextBuilder entityManagerFactory(
        EntityManagerFactory entityManagerFactory
    ) {
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
    protected EzyQueryMethodConverter newQueryMethodConverter() {
        return new EzyJpaQueryMethodConverter();
    }

    @Override
    protected EzyAbstractRepositoriesImplementer newRepositoriesImplementer() {
        return new EzyJpaRepositoriesImplementer();
    }

    @Override
    protected void scanAndAddQueries(EzyReflection reflection) {
        Set<Class<?>> resultClasses = reflection.getAnnotatedClasses(NamedQuery.class);
        for (Class<?> resultClass : resultClasses) {
            NamedQuery anno = resultClass.getAnnotation(NamedQuery.class);
            addQuery(anno.name(), anno.query(), resultClass, false);
        }
        resultClasses = reflection.getAnnotatedClasses(NamedNativeQuery.class);
        for (Class<?> resultClass : resultClasses) {
            NamedNativeQuery anno = resultClass.getAnnotation(NamedNativeQuery.class);
            addQuery(anno.name(), anno.query(), resultClass, true);
        }
    }

    protected void addQuery(
        String name, String value, Class<?> resultClass, boolean nativeQuery) {
        super.doAddQuery(name, "", value, resultClass, nativeQuery);
    }

    @Override
    protected void bindResultType(Class<?> resultType) {
        bindingContextBuilder.addArrayBindingClass(resultType);
    }
}
