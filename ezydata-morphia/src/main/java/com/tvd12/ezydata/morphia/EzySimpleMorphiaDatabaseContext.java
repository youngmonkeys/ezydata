package com.tvd12.ezydata.morphia;

import com.tvd12.ezydata.database.EzySimpleDatabaseContext;

import dev.morphia.Datastore;
import lombok.Getter;
import lombok.Setter;

@Setter
public class EzySimpleMorphiaDatabaseContext 
		extends EzySimpleDatabaseContext
		implements EzyMorphiaDatabaseContext {

	@Getter
	protected Datastore datastore;
	
}
