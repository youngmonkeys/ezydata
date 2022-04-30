package com.tvd12.ezydata.database;

import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.database.converter.EzyBindResultDeserializer;
import com.tvd12.ezydata.database.converter.EzyResultDeserializer;
import com.tvd12.ezydata.database.converter.EzySimpleResultDeserializers;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzySimpleQueryManager;
import com.tvd12.ezydata.database.util.EzyDatabasePropertiesKeeper;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.database.annotation.EzyNamedQuery;
import com.tvd12.ezyfox.database.annotation.EzyQueryResult;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.ezyfox.database.annotation.EzyResultDeserialized;
import com.tvd12.ezyfox.io.EzyLists;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class EzyDatabaseContextBuilder<B extends EzyDatabaseContextBuilder<B>>
    extends EzyDatabasePropertiesKeeper<B>
    implements EzyBuilder<EzyDatabaseContext> {

    protected Set<String> packagesToScan;
    protected Set<Class<?>> repositoryClasses;
    protected Set<Class<?>> autoImplInterfaces;
    protected Set<Class<?>> queryResultClasses;
    protected List<EzyReflection> reflections;
    protected Map<Class<?>, Object> repositories;
    protected EzySimpleQueryManager queryManager;
    protected EzyQueryMethodConverter queryMethodConverter;
    protected EzyBindingContextBuilder bindingContextBuilder;
    protected EzySimpleResultDeserializers resultDeserializers;
    protected EzyDatabaseRepositoryWrapper repositoryWrapper;

    public EzyDatabaseContextBuilder() {
        this.reflections = new ArrayList<>();
        this.packagesToScan = new HashSet<>();
        this.repositoryClasses = new HashSet<>();
        this.autoImplInterfaces = new HashSet<>();
        this.queryResultClasses = new HashSet<>();
        this.repositories = new HashMap<>();
        this.queryManager = new EzySimpleQueryManager();
        this.queryMethodConverter = newQueryMethodConverter();
        this.resultDeserializers = new EzySimpleResultDeserializers();
        this.repositoryWrapper = EzyDatabaseRepositoryWrapper.DEFAULT;
    }

    protected EzyQueryMethodConverter newQueryMethodConverter() {
        return null;
    }

    public B addQuery(EzyQueryEntity query) {
        this.queryManager.addQuery(query);
        return (B) this;
    }

    public B addQueries(Iterable<EzyQueryEntity> queries) {
        for (EzyQueryEntity query : queries) {
            addQuery(query);
        }
        return (B) this;
    }

    public B scan(String packageName) {
        this.packagesToScan.add(packageName);
        return (B) this;
    }

    public B scan(String... packageNames) {
        return scan(Sets.newHashSet(packageNames));
    }

    public B scan(Iterable<String> packageNames) {
        for (String packageName : packageNames) {
            this.scan(packageName);
        }
        return (B) this;
    }

    public B scan(EzyReflection reflection) {
        this.reflections.add(reflection);
        return (B) this;
    }

    public B repositoryInterface(Class<?> itf) {
        this.autoImplInterfaces.add(itf);
        return (B) this;
    }

    public B repositoryInterfaces(Class<?>... interfaces) {
        return repositoryInterfaces(Sets.newHashSet(interfaces));
    }

    public B repositoryInterfaces(Iterable<Class<?>> interfaces) {
        for (Class<?> itf : interfaces) {
            this.repositoryInterface(itf);
        }
        return (B) this;
    }

    public B repositoryClass(Class<?> repoClass) {
        this.repositoryClasses.add(repoClass);
        return (B) this;
    }

    public B repositoryClasses(Class<?>... repoClasses) {
        return repositoryClasses(Sets.newHashSet(repoClasses));
    }

    public B repositoryClasses(Iterable<Class<?>> repoClasses) {
        for (Class<?> repoClass : repoClasses) {
            repositoryClass(repoClass);
        }
        return (B) this;
    }

    public B queryResultClass(Class<?> resultClass) {
        this.queryResultClasses.add(resultClass);
        return (B) this;
    }

    public B queryResultClasses(Class<?>... resultClasses) {
        return queryResultClasses(Sets.newHashSet(resultClasses));
    }

    public B queryResultClasses(Iterable<Class<?>> resultClasses) {
        for (Class<?> resultClass : resultClasses) {
            this.queryResultClasses.add(resultClass);
        }
        return (B) this;
    }

    public B bindingContextBuilder(
        EzyBindingContextBuilder bindingContextBuilder
    ) {
        this.bindingContextBuilder = bindingContextBuilder;
        return (B) this;
    }

    public B addResultDeserializer(
        Class<?> resultType,
        EzyResultDeserializer deserializer
    ) {
        this.queryResultClasses.add(resultType);
        this.resultDeserializers.addDeserializer(resultType, deserializer);
        return (B) this;
    }

    public B addResultDeserializers(
        Map<Class<?>, EzyResultDeserializer> deserializers
    ) {
        for (Class<?> resultType : deserializers.keySet()) {
            EzyResultDeserializer deserializer = deserializers.get(resultType);
            addResultDeserializer(resultType, deserializer);
        }
        return (B) this;
    }

    @Override
    public EzyDatabaseContext build() {
        if (packagesToScan.size() > 0) {
            reflections.add(new EzyReflectionProxy(packagesToScan));
        }
        if (bindingContextBuilder == null) {
            bindingContextBuilder = EzyBindingContext.builder();
        }
        for (EzyReflection reflection : reflections) {
            bindingContextBuilder.addAllClasses(reflection);
        }
        preBuild();
        scanAndAddQueries();
        scanAndAddResultTypes();
        scanAndAddRepoClasses();
        EzySimpleDatabaseContext context = newDatabaseContext();
        context.setQueryManager(queryManager);
        context.setDeserializers(resultDeserializers);
        addRepositoriesFromClasses(context);
        implementAutoImplRepositories(context);
        scanAndAddResultDeserializers();
        addQueryResultClassesFromQueryManager();
        Set<Class<?>> unknownDeserializerResultTypes =
            getUnknownDeserializerResultClasses();
        bindUnknownDeserializerResultClasses(unknownDeserializerResultTypes);
        EzyBindingContext bindingContext = bindingContextBuilder.build();
        createUnknownResultDeserializers(
            bindingContext,
            unknownDeserializerResultTypes
        );
        context.setRepositories((Map) repositories);
        printDatabaseContextInformation(context);
        postBuild(context, bindingContext);
        return context;
    }

    protected abstract EzySimpleDatabaseContext newDatabaseContext();

    protected void preBuild() {}

    protected void postBuild(
        EzySimpleDatabaseContext context,
        EzyBindingContext bindingContext
    ) {}

    protected void scanAndAddQueries() {
        for (EzyReflection reflection : reflections) {
            scanAndAddNamedQueries(reflection);
            scanAndAddQueries(reflection);
        }
    }

    protected void scanAndAddQueries(EzyReflection reflection) {}

    protected void scanAndAddNamedQueries(EzyReflection reflection) {
        Set<Class<?>> resultClasses = reflection
            .getAnnotatedClasses(EzyNamedQuery.class);
        for (Class<?> resultClass : resultClasses) {
            EzyNamedQuery anno = resultClass
                .getAnnotation(EzyNamedQuery.class);
            doAddQuery(
                anno.name(),
                anno.type(),
                anno.value(),
                resultClass,
                anno.nativeQuery()
            );
        }
    }

    protected void doAddQuery(
        String name,
        String type,
        String value,
        Class<?> resultClass,
        boolean nativeQuery
    ) {
        String queryName = name;
        if (EzyStrings.isNoContent(name)) {
            queryName = resultClass.getName();
        }
        EzyQueryEntity queryEntity = queryManager.getQuery(queryName);
        if (queryEntity != null) {
            if (queryEntity.getResultType() != resultClass) {
                throw new IllegalStateException(
                    "too many result type of query: " + queryName +
                        "(" + queryEntity.getResultType().getName() +
                        ", " + resultClass.getName() + ")"
                );
            }
        } else {
            if (EzyStrings.isNoContent(value)) {
                throw new IllegalStateException(
                    "has no query with name: " + queryName
                );
            }
            queryEntity = EzyQueryEntity.builder()
                .name(queryName)
                .type(type)
                .value(value)
                .resultType(resultClass)
                .nativeQuery(nativeQuery)
                .build();
            queryManager.addQuery(queryEntity);
        }
    }

    protected void scanAndAddRepoClasses() {
        for (EzyReflection reflection : reflections) {
            repositoryClasses.addAll(
                EzyLists.filter(
                    reflection.getAnnotatedClasses(EzyRepository.class),
                    it -> !it.isInterface()
                )
            );
        }
    }

    protected void scanAndAddResultTypes() {
        for (EzyReflection reflection : reflections) {
            queryResultClasses.addAll(
                reflection.getAnnotatedClasses(EzyQueryResult.class)
            );
        }
    }

    protected void scanAndAddResultDeserializers() {
        for (EzyReflection reflection : reflections) {
            scanAndAddResultDeserializers(reflection);
        }
    }

    protected void scanAndAddResultDeserializers(EzyReflection reflection) {
        Set<Class> classes = (Set) reflection.getAnnotatedClasses(
            EzyResultDeserialized.class
        );
        for (Class<EzyResultDeserializer> clazz : classes) {
            EzyResultDeserialized anno = clazz.getAnnotation(
                EzyResultDeserialized.class
            );
            Class<?> resultType = anno.value();
            if (EzyResultDeserializer.class.isAssignableFrom(clazz)) {
                EzyResultDeserializer deserializer = EzyClasses.newInstance(clazz);
                resultDeserializers.addDeserializer(resultType, deserializer);
            } else {
                throw new IllegalStateException(
                    clazz + " must implement interface "
                        + EzyResultDeserializer.class.getName()
                );
            }
        }
    }

    private void addQueryResultClassesFromQueryManager() {
        Map<String, EzyQueryEntity> queries = queryManager.getQueries();
        for (EzyQueryEntity query : queries.values()) {
            Class<?> resultType = query.getResultType();
            if (resultType == Object.class) {
                continue;
            }
            queryResultClasses.add(resultType);
        }
    }

    private Set<Class<?>> getUnknownDeserializerResultClasses() {
        Set<Class<?>> unknownDeserializerResultTypes = new HashSet<>();
        for (Class<?> resultType : queryResultClasses) {
            EzyResultDeserializer ds = resultDeserializers.getDeserializer(resultType);
            if (ds == null) {
                unknownDeserializerResultTypes.add(resultType);
            }
        }
        return unknownDeserializerResultTypes;
    }

    private void bindUnknownDeserializerResultClasses(
        Set<Class<?>> unknownDeserializerResultTypes
    ) {
        for (Class<?> resultType : unknownDeserializerResultTypes) {
            bindResultType(resultType);
        }
    }

    private void createUnknownResultDeserializers(
        EzyBindingContext bindingContext,
        Set<Class<?>> unknownDeserializerResultTypes
    ) {
        EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
        for (Class<?> resultType : unknownDeserializerResultTypes) {
            EzyResultDeserializer deserializer =
                newUnknownResultDeserializer(resultType, unmarshaller);
            resultDeserializers.addDeserializer(resultType, deserializer);
        }
    }

    protected EzyResultDeserializer newUnknownResultDeserializer(
        Class<?> resultType,
        EzyUnmarshaller unmarshaller
    ) {
        return new EzyBindResultDeserializer(resultType, unmarshaller);
    }

    protected void bindResultType(Class<?> resultType) {
        bindingContextBuilder.addClass(resultType);
    }

    protected void addRepositoriesFromClasses(EzyDatabaseContext context) {
        for (Class<?> repoClass : repositoryClasses) {
            addRepositoryFromClass(context, repoClass);
        }
    }

    protected void addRepositoryFromClass(
        EzyDatabaseContext context,
        Class<?> repoClass
    ) {
        try {
            Object repo = EzyClasses.newInstance(repoClass);
            if (repo instanceof EzyDatabaseContextAware) {
                ((EzyDatabaseContextAware) repo).setDatabaseContext(context);
            }
            postCreateRepositoryFromClass(context, repo);
            repositories.put(repoClass, repo);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "create repo of class: " + repoClass.getName() + " error",
                e
            );
        }
    }

    protected void postCreateRepositoryFromClass(
        EzyDatabaseContext context,
        Object repo
    ) {}

    private void implementAutoImplRepositories(
        EzySimpleDatabaseContext context
    ) {
        EzyAbstractRepositoriesImplementer implementer =
            createRepositoriesImplementer();
        repositories.putAll(implementer.implement(context));
    }

    private EzyAbstractRepositoriesImplementer createRepositoriesImplementer() {
        EzyAbstractRepositoriesImplementer answer = newRepositoriesImplementer();
        answer.queryManager(queryManager);
        answer.queryMethodConverter(queryMethodConverter);
        answer.repositoryWrapper(repositoryWrapper);
        for (EzyReflection reflection : reflections) {
            answer.repositoryInterfaces(reflection);
        }
        answer.repositoryInterfaces(autoImplInterfaces);
        for (EzyReflection reflection : reflections) {
            answer.repositoryInterfaces(reflection);
        }
        return answer;
    }

    protected abstract EzyAbstractRepositoriesImplementer newRepositoriesImplementer();

    protected void printDatabaseContextInformation(EzyDatabaseContext context) {
        logger.debug("\n{}\n{}\n{}",
            "====================== DATABASE CONTEXT ===============",
            context,
            "=======================================================");
    }
}
