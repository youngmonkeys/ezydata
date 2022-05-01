package com.tvd12.ezydata.morphia.testing;

import com.tvd12.ezydata.morphia.testing.data.Cat;
import com.tvd12.ezydata.morphia.testing.repo.CatRepo;
import org.testng.annotations.Test;

public class CombineTest extends BaseMongoDBTest {

    @Test
    public void test() {
        CatRepo repo = (CatRepo) BEAN_CONTEXT.getBean(CatRepo.class);
        repo.save(new Cat());
    }
}
