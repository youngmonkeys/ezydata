package com.tvd12.ezydata.database.bean;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.EzyDatabaseRepositoryWrapper;
import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezydata.database.query.EzyQueryMethod;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzyQueryRegister;
import com.tvd12.ezydata.database.query.EzyQueryString;
import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyMethods;
import com.tvd12.ezyfox.util.EzyLoggable;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractRepositoryImplementer extends EzyLoggable {

	protected final EzyClass clazz;
	protected Class<?> idType;
	protected Class<?> entityType;
	@Setter
	protected EzyQueryRegister queryManager;
	@Setter
	protected EzyQueryMethodConverter queryMethodConverter;
	@Setter
	protected EzyDatabaseRepositoryWrapper repositoryWrapper;
	@Setter
	protected static boolean debug; 
	protected static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzyAbstractRepositoryImplementer(Class<?> clazz) {
		this(new EzyClass(clazz));
	}
	
	public EzyAbstractRepositoryImplementer(EzyClass clazz) {
		this.clazz = clazz;
	}
	
	public Object implement(Object template) {
		try {
			this.preimplement(template);
			return doimplement(template);
		}
		catch(Exception e) {
			throw new IllegalStateException("error on repo interface: " + clazz.getName(), e);
		}
	}
	
	protected void preimplement(Object template) {}

	protected Object doimplement(Object template) throws Exception {
		Class[] idAndEntityTypes = getIdAndEntityTypes();
		idType = idAndEntityTypes[0];
		entityType = idAndEntityTypes[1];
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyClass superClass = new EzyClass(getSuperClass());
		implClass.setSuperclass(pool.get(superClass.getName()));
		for(EzyMethod method : getAbstractMethods()) {
			registerQuery(method);
			String methodContent = makeAbstractMethodContent(method);
			printMethodContent(methodContent);
			implClass.addMethod(CtNewMethod.make(methodContent, implClass));
		}
		String getEntityTypeMethodContent = makeGetEntityTypeMethodContent(entityType);
		printMethodContent(getEntityTypeMethodContent);
		implClass.addMethod(CtNewMethod.make(getEntityTypeMethodContent, implClass));
		implClass.setInterfaces(new CtClass[] { pool.get(clazz.getName()) });
		Class answerClass = implClass.toClass();
		implClass.detach();
		Object repo = answerClass.newInstance();
		if(template instanceof EzyDatabaseContext 
				&& repo instanceof EzyDatabaseContextAware) {
			((EzyDatabaseContextAware)repo).setDatabaseContext((EzyDatabaseContext) template);
		}
		setRepoComponent(repo, template);
		return repositoryWrapper.wrap(repo);
	}
	
	protected void setRepoComponent(Object repo, Object template) {}
	
	protected Collection<EzyMethod> getAbstractMethods() {
		return clazz.getMethods(m -> isAbstractMethod(m));
	}
	
	protected boolean isAbstractMethod(EzyMethod method) {
		if(method.isAnnotated(EzyQuery.class))
			return true;
		if(isAutoImplementMethod(method))
			return true;
		return false;
	}
	
	protected boolean isAutoImplementMethod(EzyMethod method) {
		for(EzyMethod defMethod : EzyDatabaseRepository.CLASS.getMethods()) {
			if(method.equals(defMethod))
				return false;
			if(EzyMethods.isOverriddenMethod(method, defMethod))
				return false;
		}
		return true;
	}
	
	protected void registerQuery(EzyMethod method) {
		if(queryManager == null)
			return;
		EzyQuery queryAnno = method.getAnnotation(EzyQuery.class);
		if(queryAnno == null)
			return;
		String queryValue = queryAnno.value();
		if(EzyStrings.isNoContent(queryValue))
			return;
		String queryName = queryAnno.name();
		if(EzyStrings.isNoContent(queryName))
			queryName = method.toString();
		EzyQueryEntity queryEntity = EzyQueryEntity.builder()
				.name(queryName)
				.type(queryAnno.type())
				.value(queryValue)
				.nativeQuery(queryAnno.nativeQuery())
				.resultType(queryAnno.resultType())
				.build();
		queryManager.addQuery(queryEntity);
	}
	
	protected String makeAbstractMethodContent(EzyMethod method) {
		EzyBody body = new EzyFunction(method).body();
		return body.function().toString();
	}
	
	protected EzyQueryString getQueryString(EzyMethod method) {
		EzyQuery anno = method.getAnnotation(EzyQuery.class);
		if(anno != null)
			return getQueryString(method, anno);
		return convertQueryMethodToQueryString(method);
	}
	
	protected EzyQueryString getQueryString(EzyMethod method, EzyQuery queryAnnotation) {
		String queryString = queryAnnotation.value();
		boolean nativeQuery = queryAnnotation.nativeQuery();
		if(EzyStrings.isNoContent(queryString)) {
			String queryName = queryAnnotation.name();
			if(EzyStrings.isNoContent(queryName))
				throw new IllegalArgumentException("query name can not be null on method: " + method.getName());
			EzyQueryEntity query = queryManager.getQuery(queryName);
			if(query == null)
				throw new IllegalArgumentException("not found query with name: " + queryName + " on method: " + method.getName());
			queryString = query.getValue();
			nativeQuery = query.isNativeQuery();
		}
		return new EzyQueryString(queryString, nativeQuery);
	}
	
	protected EzyQueryString convertQueryMethodToQueryString(EzyMethod method) {
		EzyQueryMethod queryMethod = new EzyQueryMethod(method);
		return new EzyQueryString(queryMethodConverter.toQueryString(entityType, queryMethod));
	}
	
	protected boolean isPaginationMethod(EzyMethod method) {
		return EzyQueryMethod.isPaginationMethod(method);
	}
	
	protected Class<?> getResultType(EzyMethod method) {
		Class<?> resultType = Object.class;
		EzyQuery anno = method.getAnnotation(EzyQuery.class);
		if(anno != null)
			resultType = anno.resultType();
		if(resultType == Object.class) {
			resultType = method.getReturnType();
			if(Iterable.class.isAssignableFrom(resultType)) {
				try {
					resultType = EzyGenerics.getOneGenericClassArgument(
						method.getGenericReturnType()
					);
				}
				catch (Exception e) {}
			}
		}
		return resultType;
	}
	
	protected String makeGetEntityTypeMethodContent(Class entityType) {
		return new EzyFunction(getEntityTypeMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.answer()
							.clazz(entityType, true))
					.function()
				.toString();
	}
	
	protected EzyMethod getEntityTypeMethod() {
		Method method = EzyMethods.getMethod(getSuperClass(), "getEntityType");
		return new EzyMethod(method);
	}
	
	protected abstract Class<?> getSuperClass();
	
	protected String getImplClassName() {
		return clazz.getName() + "$EzyDatabaseRepository$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
	
	protected Class[] getIdAndEntityTypes() {
		return EzyGenerics.getGenericInterfacesArguments(clazz.getClazz(), getBaseRepositoryInterface(), 2);
	}
	
	protected Class<?> getBaseRepositoryInterface() {
		return EzyDatabaseRepository.class;
	}
	
	protected void printMethodContent(String methodContent) {
		if(debug) 
			logger.info("method content \n{}", methodContent);
	}
	
}
