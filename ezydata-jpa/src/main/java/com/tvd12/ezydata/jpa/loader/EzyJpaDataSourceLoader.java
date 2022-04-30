package com.tvd12.ezydata.jpa.loader;

import com.tvd12.ezydata.database.util.EzyDatabasePropertiesKeeper;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.function.Function;

public class EzyJpaDataSourceLoader
    extends EzyDatabasePropertiesKeeper<EzyJpaDataSourceLoader> {

    public DataSource load() {
        return load(props ->
            new HikariDataSource(newConfig(props))
        );
    }

    public DataSource load(Function<Properties, DataSource> supplier) {
        return supplier.apply(properties);
    }

    private HikariConfig newConfig(Properties properties) {
        Properties newProps = new Properties();
        for (String name : properties.stringPropertyNames()) {
            String camelCaseName = EzyStrings.underscoreToCamelCase(name);
            newProps.put(camelCaseName, properties.get(name));
        }
        return new PropertiesMapper()
            .data(newProps)
            .map(HikariConfig.class);
    }
}
