package com.tvd12.ezydata.jpa.test.repo;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.test.entity.Monkey;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface MonkeyRepoBase extends EzyDatabaseRepository<String, Monkey> {

    Monkey findByMonkeyId(String id);
}
