package com.tvd12.ezydata.database.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepositoryWrapper;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzyQueryRegister;
import com.tvd12.ezyfox.reflect.EzyReflection;

import java.util.Map;

public interface EzyRepositoriesImplementer {

    EzyRepositoriesImplementer scan(String packageName);

    EzyRepositoriesImplementer scan(String... packageNames);

    EzyRepositoriesImplementer scan(Iterable<String> packageNames);

    EzyRepositoriesImplementer repositoryInterface(Class<?> itf);

    EzyRepositoriesImplementer repositoryInterfaces(Class<?>... interfaces);

    EzyRepositoriesImplementer repositoryInterfaces(Iterable<Class<?>> interfaces);

    EzyRepositoriesImplementer repositoryInterfaces(EzyReflection reflection);

    EzyRepositoriesImplementer queryManager(EzyQueryRegister queryManager);

    EzyRepositoriesImplementer queryMethodConverter(EzyQueryMethodConverter queryMethodConverter);

    EzyRepositoriesImplementer repositoryWrapper(EzyDatabaseRepositoryWrapper repositoryWrapper);

    Map<Class<?>, Object> implement(Object template);
}
