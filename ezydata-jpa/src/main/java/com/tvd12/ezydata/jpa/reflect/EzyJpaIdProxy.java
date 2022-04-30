package com.tvd12.ezydata.jpa.reflect;

import com.tvd12.ezyfox.reflect.*;

import javax.persistence.Id;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class EzyJpaIdProxy {

    protected final Function idGetter;
    protected final BiConsumer idSetter;

    public EzyJpaIdProxy(EzyClass entityClass) {
        EzyGetterBuilder getterBuilder = new EzyGetterBuilder();
        EzySetterBuilder setterBuilder = new EzySetterBuilder();
        Optional<EzyField> idField = entityClass.getAnnotatedField(Id.class);
        if (idField.isPresent()) {
            getterBuilder.field(idField.get());
            setterBuilder.field(idField.get());
        } else {
            Optional<EzyMethod> idGetterMethod = entityClass.getAnnotatedGetterMethod(Id.class);
            Optional<EzyMethod> idSetterMethod = entityClass.getAnnotatedSetterMethod(Id.class);
            getterBuilder.method(
                idGetterMethod.orElseThrow(() ->
                    new IllegalArgumentException(
                        "missing annotated @Id field or getter/setter methods"
                    )
                )
            );
            setterBuilder.method(idSetterMethod.orElseThrow(() ->
                    new IllegalArgumentException(
                        "missing annotated @Id field or getter/setter methods"
                    )
                )
            );
        }
        this.idGetter = getterBuilder.build();
        this.idSetter = setterBuilder.build();
    }

    @SuppressWarnings("unchecked")
    public void setId(Object from, Object to) {
        this.idSetter.accept(to, idGetter.apply(from));
    }
}
