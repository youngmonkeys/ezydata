package com.tvd12.ezydata.jpa.loader;

import java.util.Properties;
import java.util.function.Function;

import javax.sql.DataSource;

import com.tvd12.ezydata.database.util.EzyDatabasePropertiesKeeper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class EzyJpaDataSourceLoader 
		extends EzyDatabasePropertiesKeeper<EzyJpaDataSourceLoader> {
	
	public DataSource load() {
		return load(props ->
			new HikariDataSource(new HikariConfig(props))
		);
	}
	
	public DataSource load(Function<Properties, DataSource> supplier) {
		return supplier.apply(properties);
	}
	
}
