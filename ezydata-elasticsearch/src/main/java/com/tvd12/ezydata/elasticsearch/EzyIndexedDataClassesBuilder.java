package com.tvd12.ezydata.elasticsearch;

import com.tvd12.ezyfox.builder.EzyBuilder;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public interface EzyIndexedDataClassesBuilder
    extends EzyBuilder<EzyIndexedDataClasses> {

    EzyIndexedDataClassesBuilder addIndexedDataClass(Class clazz);

    EzyIndexedDataClassesBuilder addIndexedDataClass(Class clazz, Set<String> indexes);

    EzyIndexedDataClassesBuilder addIndexedDataClasses(Class... classes);

    EzyIndexedDataClassesBuilder addIndexedDataClasses(Iterable<Class> classes);

    EzyIndexedDataClassesBuilder addIndexedDataClasses(Object reflection);

    EzyIndexedDataClassesBuilder addIndexedDataClasses(Map<Class, Set<String>> map);
}
