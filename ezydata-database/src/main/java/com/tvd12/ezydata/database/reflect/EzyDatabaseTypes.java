package com.tvd12.ezydata.database.reflect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.tvd12.ezyfox.reflect.EzyTypes;

@SuppressWarnings("rawtypes")
public final class EzyDatabaseTypes {

	public static final Set<Class> DEFAULT_TYPES = defaultTypes();
	
	private EzyDatabaseTypes() {}
	
	private static Set<Class> defaultTypes() {
		Set<Class> set = new HashSet<>();
		set.addAll(EzyTypes.ALL_TYPES);
		set.add(Date.class);
		set.add(LocalDate.class);
		set.add(LocalDateTime.class);
		set.add(BigInteger.class);
		set.add(BigDecimal.class);
		set.add(UUID.class);
		set.add(Class.class);
		return Collections.unmodifiableSet(set);
	}
	
}
