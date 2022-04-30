package com.tvd12.ezydata.elasticsearch;

import com.tvd12.ezydata.elasticsearch.util.EzyDataIndexesAnnotations;
import com.tvd12.ezyfox.data.EzyIndexedDataClassesFetcher;
import com.tvd12.ezyfox.data.EzySimpleIndexedDataClassesFetcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class EzySimpleIndexedDataClasses implements EzyIndexedDataClasses {

    protected final Map<Class, Set<String>> map = new ConcurrentHashMap<>();

    protected EzySimpleIndexedDataClasses(Builder builder) {
        this.map.putAll(builder.indexedClassMap);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Set<Class> getIndexedClasses() {
        return new HashSet<>(map.keySet());
    }

    @Override
    public Set<String> getIndexes(Class clazz) {
        if (map.containsKey(clazz)) {
            return map.get(clazz);
        }
        throw new IllegalArgumentException(clazz.getName() + " is not indexed data");
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public static class Builder implements EzyIndexedDataClassesBuilder {

        protected Map<Class, Set<String>> indexedClassMap
            = new ConcurrentHashMap<>();
        protected EzyIndexedDataClassesFetcher indexedDataClassFetcher
            = newIndexedDataClassesFetcher();

        @Override
        public Builder addIndexedDataClass(Class clazz) {
            this.indexedDataClassFetcher.addIndexedDataClass(clazz);
            return this;
        }

        public Builder addIndexedDataClass(Class clazz, Set<String> indexes) {
            indexedClassMap.computeIfAbsent(clazz, k -> new HashSet<>())
                .addAll(indexes);
            return this;
        }

        @Override
        public Builder addIndexedDataClasses(Class... classes) {
            return addIndexedDataClasses(Arrays.asList(classes));
        }

        @Override
        public Builder addIndexedDataClasses(Iterable<Class> classes) {
            for (Class clazz : classes) {
                this.addIndexedDataClass(clazz);
            }
            return this;
        }

        @Override
        public EzyIndexedDataClassesBuilder addIndexedDataClasses(Object reflection) {
            this.indexedDataClassFetcher.addIndexedDataClasses(reflection);
            return this;
        }

        @Override
        public Builder addIndexedDataClasses(Map<Class, Set<String>> map) {
            for (Class clazz : map.keySet()) {
                addIndexedDataClass(clazz, map.get(clazz));
            }
            return this;
        }

        @Override
        public EzyIndexedDataClasses build() {
            Set<Class> classes = indexedDataClassFetcher.getIndexedDataClasses();
            for (Class clazz : classes) {
                addIndexedDataClass(clazz, EzyDataIndexesAnnotations.getIndexes(clazz));
            }
            return new EzySimpleIndexedDataClasses(this);
        }

        protected EzyIndexedDataClassesFetcher newIndexedDataClassesFetcher() {
            return new EzySimpleIndexedDataClassesFetcher();
        }
    }
}
