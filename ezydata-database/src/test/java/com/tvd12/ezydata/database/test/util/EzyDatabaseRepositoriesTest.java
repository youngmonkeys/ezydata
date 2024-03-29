package com.tvd12.ezydata.database.test.util;

import com.tvd12.ezydata.database.util.EzyDatabaseRepositories;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyDatabaseRepositoriesTest {

    @Test
    public void autoImplAnnoWithName() {
        // given
        // when
        String repoName = EzyDatabaseRepositories.getRepoName(Repo1.class);

        // then
        Asserts.assertEquals(repoName, "repo1");
    }

    @Test
    public void repositoryWithName() {
        // given
        // when
        String repoName = EzyDatabaseRepositories.getRepoName(Repo2.class);

        // then
        Asserts.assertEquals(repoName, "repo2");
    }

    @EzyAutoImpl("repo1")
    private interface Repo1 {}

    @EzyRepository("repo2")
    private interface Repo2 {}
}
