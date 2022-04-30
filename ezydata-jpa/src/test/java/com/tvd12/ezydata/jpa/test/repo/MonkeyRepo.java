package com.tvd12.ezydata.jpa.test.repo;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.jpa.test.entity.Monkey;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface MonkeyRepo extends EzyDatabaseRepository<String, Monkey> {}
