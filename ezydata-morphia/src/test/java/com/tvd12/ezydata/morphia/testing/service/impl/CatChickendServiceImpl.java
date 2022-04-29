package com.tvd12.ezydata.morphia.testing.service.impl;

import com.tvd12.ezydata.morphia.testing.data.Monkey;
import com.tvd12.ezydata.morphia.testing.repo.CatRepo;
import com.tvd12.ezydata.morphia.testing.repo.ChickendRepo;
import com.tvd12.ezydata.morphia.testing.repo.MonkeyRepo;
import com.tvd12.ezydata.morphia.testing.service.CatChickendService;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.util.EzyLoggable;

import lombok.Setter;

@Setter
@EzySingleton
public class CatChickendServiceImpl
        extends EzyLoggable
        implements CatChickendService {

    @EzyAutoBind
    private CatRepo catRepo;
    
    @EzyAutoBind
    private MonkeyRepo monkeyRepo;
    
    @EzyAutoBind
    private ChickendRepo chickendRepo;
    
    @Override
    public void printAllCatAndChickend() {
        System.out.println("all cat: " + catRepo.findAll());
        System.out.println("all chickend: " + chickendRepo.findAll());
        System.out.println("all monkey: " + monkeyRepo.findAll());
    }
    
    @Override
    public void save2Monkey(Monkey monkey1, Monkey monkey2) {
        monkeyRepo.save2Monkey(monkey1, monkey2);
    }
    
}
