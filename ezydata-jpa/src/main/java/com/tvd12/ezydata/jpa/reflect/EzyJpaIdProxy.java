package com.tvd12.ezydata.jpa.reflect;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.persistence.Id;

import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;
import com.tvd12.ezyfox.reflect.EzyGetterBuilder;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzySetterBuilder;

@SuppressWarnings("rawtypes")
public class EzyJpaIdProxy {

	protected final Function idGetter;
	protected final BiConsumer idSetter;
	
	public EzyJpaIdProxy(EzyClass entityClass) {
		EzyGetterBuilder getterBuilder = new EzyGetterBuilder();
		EzySetterBuilder setterBuilder = new EzySetterBuilder();
		Optional<EzyField> idField = entityClass.getAnnotationedField(Id.class);
		if(idField.isPresent()) {
			getterBuilder.field(idField.get());
			setterBuilder.field(idField.get());
		}
		else {
			Optional<EzyMethod> idMethod = entityClass.getAnnotationedMethod(Id.class);
			getterBuilder.method(idMethod.get());
			setterBuilder.method(idMethod.get());
		}
		this.idGetter = getterBuilder.build();
		this.idSetter = setterBuilder.build();
	}
	
	@SuppressWarnings("unchecked")
	public void setId(Object from, Object to) {
		this.idSetter.accept(to, idGetter.apply(from));
	}
	
}
