package com.tvd12.ezydata.elasticsearch.util;

import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndex;
import com.tvd12.ezyfox.collect.Sets;

import java.util.Set;

public final class EzyDataIndexAnnotations {

    private EzyDataIndexAnnotations() {}

    public static String getIndex(EzyDataIndex anno) {
        return anno.value();
    }

    public static Set<String> getIndexes(EzyDataIndex anno) {
        return Sets.newHashSet(getIndex(anno));
    }

}
