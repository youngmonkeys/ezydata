package com.tvd12.ezydata.morphia.testing.repo.autoimpl;

import com.tvd12.ezydata.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezydata.morphia.testing.data.Monkey;
import com.tvd12.ezydata.morphia.testing.service.CatChickenService;

public class Try2 extends BaseMongoDBTest {

    public static void main(String[] args) {
        CatChickenService service =
            (CatChickenService) BEAN_CONTEXT.getBean(CatChickenService.class);
        service.printAllCatAndChicken();
        service.save2Monkey(new Monkey(), new Monkey());
    }
}
