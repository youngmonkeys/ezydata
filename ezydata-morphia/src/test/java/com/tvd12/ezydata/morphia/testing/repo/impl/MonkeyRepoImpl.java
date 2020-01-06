package com.tvd12.ezydata.morphia.testing.repo.impl;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezydata.morphia.repository.EzyDatastoreRepository;
import com.tvd12.ezydata.morphia.testing.data.Monkey;
import com.tvd12.ezydata.morphia.testing.repo.MonkeyRepo;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

@EzySingleton
public class MonkeyRepoImpl
		extends EzyDatastoreRepository<Long, Monkey>
		implements MonkeyRepo {

	@Override
	public void save2Monkey(Monkey monkey1, Monkey monkey2) {
		datastore.save(Lists.newArrayList(monkey1, monkey2));
	}
	
}
