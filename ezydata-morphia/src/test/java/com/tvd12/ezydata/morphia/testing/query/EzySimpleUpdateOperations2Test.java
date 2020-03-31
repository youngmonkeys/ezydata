package com.tvd12.ezydata.morphia.testing.query;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import com.tvd12.ezydata.morphia.query.impl.EzyMorphiaFindAndModifyOptions;
import com.tvd12.ezydata.morphia.query.impl.EzyMorphiaUpdateOperations;
import com.tvd12.ezydata.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezydata.morphia.testing.data.Cat;
import com.tvd12.ezydata.morphia.testing.data.Kitty;
import com.tvd12.ezyfox.collect.Lists;

import dev.morphia.FindAndModifyOptions;
import dev.morphia.query.UpdateOperations;

public class EzySimpleUpdateOperations2Test extends BaseMongoDBTest {

	@Test
	public void test() {
		UpdateOperations<Cat> realOperations = DATASTORE.createUpdateOperations(Cat.class);
		EzyMorphiaUpdateOperations<Cat> proxyOperations = new EzyMorphiaUpdateOperations<>(realOperations);
		
		proxyOperations.disableValidation();
		proxyOperations.enableValidation();
		proxyOperations.inc("age", 10);
		try {
			proxyOperations.isolated();
		}
		catch (Exception e) {
			assert e instanceof UnsupportedOperationException;
		}
		try {
			proxyOperations.isIsolated();
		}
		catch (Exception e) {
			assert e instanceof UnsupportedOperationException;
		}
		proxyOperations.push("valueSet", Lists.newArrayList("e", "f"));
		proxyOperations.push("kitties", new Kitty(100L), options -> options.sort("age", 1));
		proxyOperations.removeAll("valueList", Lists.newArrayList("a", "b", "c"));
		proxyOperations.setOnInsert("free", 100);
		proxyOperations.unset("unset");
		
		
		FindAndModifyOptions realOptions = new FindAndModifyOptions();
		EzyMorphiaFindAndModifyOptions proxyOptions = new EzyMorphiaFindAndModifyOptions(realOptions);
		proxyOptions.upsert(true)
			.maxTime(10, TimeUnit.SECONDS)
			.remove(false)
			.returnNew(true);
		assert !proxyOptions.isRemove();
		assert proxyOptions.isReturnNew();
		assert proxyOptions.isUpsert();
		assert proxyOptions.getMaxTime(TimeUnit.SECONDS) == 10;
		
		Cat cat = new Cat();
		DATASTORE.delete(DATASTORE.createQuery(Cat.class));
		DATASTORE.findAndModify(DATASTORE.createQuery(
				Cat.class).field("id").equal(cat.getId()), 
				realOperations,
				realOptions);
		
	}
	
}
