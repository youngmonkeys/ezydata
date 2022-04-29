package com.tvd12.ezydata.elasticsearch.util;

import java.util.Set;

import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndex;
import com.tvd12.ezyfox.collect.Sets;

public final class EzyDataIndexAnnotations {

    private EzyDataIndexAnnotations() {
    }

    public static String getIndex(Class<?> clazz, EzyDataIndex anno) {
        String index = anno.value();
        return index;
    }

    public static Set<String> getIndexes(Class<?> clazz, EzyDataIndex anno) {
        return Sets.newHashSet(getIndex(clazz, anno));
    }

}
