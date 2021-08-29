package com.tvd12.ezydata.jpa.loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;

import com.tvd12.ezydata.database.util.EzyDatabasePropertiesKeeper;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;

import lombok.AllArgsConstructor;

public class EzyJpaEntityManagerFactoryLoader 
		extends EzyDatabasePropertiesKeeper<EzyJpaEntityManagerFactoryLoader> {

	protected String jpaVersion = "2.2";
	protected DataSource dataSource;
	protected final Set<String> entityPackages = new HashSet<>();
	protected final List<String> managedClassNames = new ArrayList<>();
	protected final List<String> mappingFileNames = new ArrayList<>();
	protected PersistenceUnitTransactionType transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
	
	
	public EzyJpaEntityManagerFactoryLoader jpaVersion(String jpaVersion) {
		this.jpaVersion = jpaVersion;
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader transactionType(PersistenceUnitTransactionType transactionType) {
		this.transactionType = transactionType;
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader mappingFileName(String mappingFileName) {
		this.mappingFileNames.add(mappingFileName);
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader mappingFileName(Iterable<String> mappingFileNames) {
		for(String mappingFileName : mappingFileNames)
			mappingFileName(mappingFileName);
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader entityClass(Class<?> entityClass) {
		this.managedClassNames.add(entityClass.getName());
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader entityClasses(Iterable<Class<?>> entityClasses) {
		for(Class<?> entityClass : entityClasses)
			entityClass(entityClass);
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader entityPackage(String entityPackage) {
		this.entityPackages.add(entityPackage);
		return this;
	}
	
	public EzyJpaEntityManagerFactoryLoader entityPackages(Iterable<String> entityPackages) {
		for(String entityPackage : entityPackages)
			entityPackage(entityPackage);
		return this;
	}

	public EntityManagerFactory load(String persistenceUnitName) {
		scanEntityPackages();
		return doLoad(persistenceUnitName);
	}
	
	private EntityManagerFactory doLoad(String persistenceUnitName) {
		EntityManagerFactory entityManagerFactory = null;
		try {
			entityManagerFactory = loadByHibernate(persistenceUnitName);
		}
		catch (Throwable e) {
			logger.warn("can't load EntityManagerFactory by hibernate (you can disable this warning by config log level to ERROR)", e);
		}
		if(entityManagerFactory == null)
			entityManagerFactory = loadByDefaultJpa(persistenceUnitName);
		return entityManagerFactory;
	}

	private EntityManagerFactory loadByHibernate(String persistenceUnitName) {
		PersistenceUnitInfo persistenceUnitInfo = 
				new PersistenceUnitInfoImpl(persistenceUnitName);
		HibernatePersistenceProvider persistenceProvider =
				new HibernatePersistenceProvider();
		return persistenceProvider
				.createContainerEntityManagerFactory(
						persistenceUnitInfo, 
						Collections.EMPTY_MAP
				);
	}
	
	private EntityManagerFactory loadByDefaultJpa(String persistenceUnitName) {
		return Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	private void scanEntityPackages() {
		if(entityPackages.isEmpty())
			return;
		EzyReflection reflection = new EzyReflectionProxy(entityPackages);
		entityClasses(reflection.getAnnotatedClasses(Entity.class));
	}

	@AllArgsConstructor
	private class PersistenceUnitInfoImpl implements PersistenceUnitInfo {

		private final String persistenceUnitName; 
		
		@Override
		public String getPersistenceUnitName() {
			return persistenceUnitName;
		}

		@Override
		public String getPersistenceProviderClassName() {
			return HibernatePersistenceProvider.class.getName();
		}

		@Override
		public PersistenceUnitTransactionType getTransactionType() {
			return transactionType;
		}

		@Override
		public DataSource getJtaDataSource() {
			return dataSource;
		}

		@Override
		public DataSource getNonJtaDataSource() {
			return dataSource;
		}

		@Override
		public List<String> getMappingFileNames() {
			return mappingFileNames;
		}

		@Override
		public List<URL> getJarFileUrls() {
			return Collections.emptyList();
		}

		@Override
		public URL getPersistenceUnitRootUrl() { return null; }

		@Override
		public List<String> getManagedClassNames() {
			return managedClassNames;
		}

		@Override
		public boolean excludeUnlistedClasses() { return false; }

		@Override
		public SharedCacheMode getSharedCacheMode() {
			return SharedCacheMode.UNSPECIFIED;
		}

		@Override
		public ValidationMode getValidationMode() {
			return ValidationMode.AUTO;
		}

		public Properties getProperties() {
			return properties;
		}

		@Override
		public String getPersistenceXMLSchemaVersion() {
			return jpaVersion;
		}

		@Override
		public ClassLoader getClassLoader() {
			return Thread.currentThread().getContextClassLoader();
		}

		@Override
		public void addTransformer(ClassTransformer transformer) {}

		@Override
		public ClassLoader getNewTempClassLoader() { return null; }
	}

}
