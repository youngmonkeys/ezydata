package com.tvd12.ezydata.morphia.testing;

import com.tvd12.ezydata.morphia.bean.EzyMorphiaRepositoryImplementer;
import org.testng.annotations.Test;

public class EzyRepositoryImplementerTest extends BaseMongoDBTest {

    @Test(expectedExceptions = {IllegalStateException.class})
    public void test() {
        new EzyMorphiaRepositoryImplementer(ClassA.class).implement(DATASTORE);
    }

    public static class ClassA {}
}
