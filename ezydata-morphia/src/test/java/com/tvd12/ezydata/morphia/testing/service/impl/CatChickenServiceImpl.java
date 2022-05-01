package com.tvd12.ezydata.morphia.testing.service.impl;

import com.tvd12.ezydata.morphia.testing.data.Monkey;
import com.tvd12.ezydata.morphia.testing.repo.CatRepo;
import com.tvd12.ezydata.morphia.testing.repo.ChickenRepo;
import com.tvd12.ezydata.morphia.testing.repo.MonkeyRepo;
import com.tvd12.ezydata.morphia.testing.service.CatChickenService;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Setter;

@Setter
@EzySingleton
public class CatChickenServiceImpl
    extends EzyLoggable
    implements CatChickenService {

    @EzyAutoBind
    private CatRepo catRepo;

    @EzyAutoBind
    private MonkeyRepo monkeyRepo;

    @EzyAutoBind
    private ChickenRepo chickenRepo;

    @Override
    public void printAllCatAndChicken() {
        System.out.println("all cat: " + catRepo.findAll());
        System.out.println("all chicken: " + chickenRepo.findAll());
        System.out.println("all monkey: " + monkeyRepo.findAll());
    }

    @Override
    public void save2Monkey(Monkey monkey1, Monkey monkey2) {
        monkeyRepo.save2Monkey(monkey1, monkey2);
    }
}
