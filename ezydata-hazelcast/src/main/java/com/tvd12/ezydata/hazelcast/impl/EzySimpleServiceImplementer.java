package com.tvd12.ezydata.hazelcast.impl;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezydata.hazelcast.service.EzySimpleHazelcastMapService;
import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyMethods;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtNewMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;

public class EzySimpleServiceImplementer {

    protected static final AtomicInteger COUNT = new AtomicInteger(0);
    protected final EzyClass clazz;

    public EzySimpleServiceImplementer(EzyClass clazz) {
        this.clazz = clazz;
        this.checkInterface(clazz);
    }

    public Object implement(HazelcastInstance hzInstance) {
        return implement(hzInstance, getMapName());
    }

    public Object implement(HazelcastInstance hzInstance, String mapName) {
        try {
            return doImplement(hzInstance, mapName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void checkInterface(EzyClass clazz) {
        if (!Modifier.isInterface(clazz.getModifiers())) {
            throw new IllegalArgumentException(clazz + " is not an interface");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object doImplement(HazelcastInstance hzInstance, String mapName) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        String implClassName = getImplClassName();
        CtClass implClass = pool.makeClass(implClassName);
        EzyClass superClass = getSuperClass();
        String getMapNameMethodContent = makeGetMapNameMethodContent(mapName);
        implClass.setInterfaces(new CtClass[]{pool.get(clazz.getName())});
        implClass.setSuperclass(pool.get(superClass.getName()));
        CtConstructor constructorMethod = makeConstructorMethod(pool, implClass);
        implClass.addConstructor(constructorMethod);
        implClass.addMethod(CtNewMethod.make(getMapNameMethodContent, implClass));
        Class answerClass = implClass.toClass();
        implClass.detach();
        Constructor constructor = answerClass.getConstructor(HazelcastInstance.class);
        return constructor.newInstance(hzInstance);
    }

    protected CtConstructor makeConstructorMethod(
        ClassPool pool, CtClass implClass) throws Exception {
        CtConstructor constructor = new CtConstructor(
            new CtClass[]{
                pool.get(HazelcastInstance.class.getName())
            },
            implClass
        );
        constructor.setModifiers(Modifier.PUBLIC);
        constructor.setBody("super($1);");
        return constructor;
    }

    protected String makeGetMapNameMethodContent(String mapName) {
        return new EzyFunction(getMapNameMethod())
            .body()
            .append(new EzyInstruction("\t", "\n")
                .answer()
                .string(mapName))
            .function()
            .toString();
    }

    private String getMapName() {
        return new EzySimpleMapNameFetcher().getMapName(clazz.getClazz());
    }

    protected EzyMethod getMapNameMethod() {
        Method method = EzyMethods.getMethod(EzySimpleHazelcastMapService.class, "getMapName");
        return new EzyMethod(method);
    }

    protected EzyClass getSuperClass() {
        return new EzyClass(EzySimpleHazelcastMapService.class);
    }

    protected String getImplClassName() {
        return clazz.getName() + "$EzyHazelcastService$EzyAutoImpl$" + COUNT.incrementAndGet();
    }
}
