package com.tvd12.ezydata.database.util;

import java.util.Properties;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.util.PropertiesUtil;

@SuppressWarnings("unchecked")
public abstract class EzyDatabasePropertiesKeeper<P extends EzyDatabasePropertiesKeeper<P>>
		extends EzyLoggable {

	protected final Properties properties = new Properties();
	
	public P properties(Properties properties) {
		this.properties.putAll(properties);
		return (P)this;
	}
	
	public P properties(Properties properties, String prefix) {
		this.properties.putAll(PropertiesUtil.getPropertiesByPrefix(properties, prefix));
		return (P)this;
	}

	public P property(String name, Object value) {
		this.properties.put(name, value);
		return (P)this;
	}

	public P propertiesFile(String filePath) {
		this.properties.putAll(loadProperties(filePath));
		return (P)this;
	}
	
	private Properties loadProperties(String filePath) {
		return new BaseFileReader().read(filePath);
	}
	
}
