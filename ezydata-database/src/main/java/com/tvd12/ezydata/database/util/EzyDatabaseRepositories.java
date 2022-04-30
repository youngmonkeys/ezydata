package com.tvd12.ezydata.database.util;

import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyClasses;

public final class EzyDatabaseRepositories {

    private EzyDatabaseRepositories() {}

    public static String getRepoName(Class<?> repoType) {
        String repoName = "";
        EzyAutoImpl autoImplAnno = repoType.getAnnotation(EzyAutoImpl.class);
        if (autoImplAnno != null) {
            repoName = autoImplAnno.value();
        }
        if (EzyStrings.isNoContent(repoName)) {
            EzyRepository repositoryAnno = repoType.getAnnotation(EzyRepository.class);
            if (repositoryAnno != null) {
                repoName = repositoryAnno.value();
            }
        }
        if (EzyStrings.isNoContent(repoName)) {
            repoName = EzyClasses.getVariableName(repoType);
        }
        return repoName;
    }
}
