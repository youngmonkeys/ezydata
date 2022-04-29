package com.tvd12.ezydata.database.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.EzyDatabaseRepositoryWrapper;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzyQueryRegister;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.ezyfox.io.EzySets;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.ezyfox.util.EzyLoggable;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EzyAbstractRepositoriesImplementer
    extends EzyLoggable
    implements EzyRepositoriesImplementer {

    protected Set<String> packagesToScan;
    protected Set<Class<?>> autoImplInterfaces;
    protected List<EzyReflection> reflections;
    protected EzyQueryRegister queryManager;
    protected EzyQueryMethodConverter queryMethodConverter;
    protected EzyDatabaseRepositoryWrapper repositoryWrapper;

    public EzyAbstractRepositoriesImplementer() {
        this.reflections = new ArrayList<>();
        this.packagesToScan = new HashSet<>();
        this.autoImplInterfaces = new HashSet<>();
        this.repositoryWrapper = EzyDatabaseRepositoryWrapper.DEFAULT;
    }

    public EzyRepositoriesImplementer scan(String packageName) {
        packagesToScan.add(packageName);
        return this;
    }

    public EzyRepositoriesImplementer scan(String... packageNames) {
        return scan(Sets.newHashSet(packageNames));
    }

    public EzyRepositoriesImplementer scan(Iterable<String> packageNames) {
        for (String packageName : packageNames) {
            this.scan(packageName);
        }
        return this;
    }

    @Override
    public EzyRepositoriesImplementer repositoryInterface(Class<?> itf) {
        if (!Modifier.isInterface(itf.getModifiers())) {
            logger.warn("class {} is not an interface, ignore its", itf.getSimpleName());
        } else if (!getBaseRepositoryInterface().isAssignableFrom(itf)) {
            logger.warn("interface {} doestn't extends {}, ignore its",
                itf.getSimpleName(), getBaseRepositoryInterface().getSimpleName());
        } else {
            autoImplInterfaces.add(itf);
        }
        return this;
    }

    @Override
    public EzyRepositoriesImplementer repositoryInterfaces(Class<?>... itfs) {
        return repositoryInterfaces(Sets.newHashSet(itfs));
    }

    @Override
    public EzyRepositoriesImplementer repositoryInterfaces(Iterable<Class<?>> itfs) {
        for (Class<?> itf : itfs) {
            this.repositoryInterface(itf);
        }
        return this;
    }

    @Override
    public EzyRepositoriesImplementer repositoryInterfaces(EzyReflection reflection) {
        if (reflection != null) {
            this.reflections.add(reflection);
        }
        return this;
    }

    @Override
    public EzyRepositoriesImplementer queryManager(EzyQueryRegister queryManager) {
        this.queryManager = queryManager;
        return this;
    }

    @Override
    public EzyRepositoriesImplementer queryMethodConverter(EzyQueryMethodConverter queryMethodConverter) {
        this.queryMethodConverter = queryMethodConverter;
        return this;
    }

    @Override
    public EzyRepositoriesImplementer repositoryWrapper(EzyDatabaseRepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        return this;
    }

    @Override
    public Map<Class<?>, Object> implement(Object template) {
        Collection<Class<?>> scannedInterfaces = getAutoImplRepoInterfaces();
        autoImplInterfaces.addAll(scannedInterfaces);
        Map<Class<?>, Object> repositories = new ConcurrentHashMap<>();
        for (Class<?> itf : autoImplInterfaces) {
            Object repo = implementRepoInterface(itf, template);
            repositories.put(itf, repo);
        }
        return repositories;
    }

    private Object implementRepoInterface(Class<?> itf, Object template) {
        EzyAbstractRepositoryImplementer implementer = newRepoImplementer(itf);
        implementer.setQueryManager(queryManager);
        implementer.setQueryMethodConverter(queryMethodConverter);
        implementer.setRepositoryWrapper(repositoryWrapper);
        return implementer.implement(template);
    }

    protected abstract EzyAbstractRepositoryImplementer newRepoImplementer(Class<?> itf);

    private Collection<Class<?>> getAutoImplRepoInterfaces() {
        if (packagesToScan.size() > 0) {
            reflections.add(new EzyReflectionProxy(packagesToScan));
        }
        Set<Class<?>> classes = new HashSet<>();
        Class<?> baseInterface = getBaseRepositoryInterface();
        for (Object item : reflections) {
            EzyReflection reflection = (EzyReflection) item;
            classes.addAll(reflection.getExtendsClasses(baseInterface));
        }
        return EzySets.filter(classes, clazz -> this.isAutoImplRepoInterface(clazz));
    }

    protected Class<?> getBaseRepositoryInterface() {
        return EzyDatabaseRepository.class;
    }

    private boolean isAutoImplRepoInterface(Class<?> clazz) {
        return (clazz.isAnnotationPresent(EzyAutoImpl.class) ||
            clazz.isAnnotationPresent(EzyRepository.class)) &&
            Modifier.isPublic(clazz.getModifiers()) &&
            Modifier.isInterface(clazz.getModifiers());
    }
}
