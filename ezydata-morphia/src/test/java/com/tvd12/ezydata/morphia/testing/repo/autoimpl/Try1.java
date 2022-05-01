package com.tvd12.ezydata.morphia.testing.repo.autoimpl;

import com.tvd12.ezydata.morphia.bean.EzyMorphiaRepositoryImplementer;
import com.tvd12.ezydata.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezydata.morphia.testing.data.Cat;
import com.tvd12.ezydata.morphia.testing.repo.CatRepo;

public class Try1 extends BaseMongoDBTest {

    public static void main(String[] args) {
        EzyMorphiaRepositoryImplementer implementer =
            new EzyMorphiaRepositoryImplementer(CatRepo.class);
        CatRepo repo = (CatRepo) implementer.implement(DATASTORE);
        Cat cat = repo.findById(2019072087L);
        System.out.println(cat);
    }
}
