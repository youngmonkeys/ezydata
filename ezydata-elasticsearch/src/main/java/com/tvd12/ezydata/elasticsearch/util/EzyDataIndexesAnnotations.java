package com.tvd12.ezydata.elasticsearch.util;

import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndex;
import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndexes;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyNameStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public final class EzyDataIndexesAnnotations {

    private static final Logger LOGGER
        = LoggerFactory.getLogger(EzyDataIndexesAnnotations.class);

    private EzyDataIndexesAnnotations() {
    }

    public static Set<String> getIndexes(Class<?> clazz) {
        EzyDataIndexes indexesAnno = clazz.getAnnotation(EzyDataIndexes.class);
        if (indexesAnno != null) {
            return EzyDataIndexesAnnotations.getIndexTypes(indexesAnno);
        }

        EzyDataIndex indexAnno = clazz.getAnnotation(EzyDataIndex.class);
        if (indexAnno != null) {
            return EzyDataIndexAnnotations.getIndexes(indexAnno);
        }

        warningAnnotationNotFound(clazz);

        String className = clazz.getSimpleName();
        String hyphenClassName = EzyNameStyles.toLowerHyphen(className);
        return Sets.newHashSet(hyphenClassName);
    }

    private static Set<String> getIndexTypes(EzyDataIndexes anno) {
        return Sets.newHashSet(anno.value());
    }

    private static void warningAnnotationNotFound(Class<?> clazz) {
        LOGGER.warn(
            "{} was not annotated by @EzyDataIndexes or @EzyDataIndex, use class name by default",
            clazz.getName()
        );
    }
}
