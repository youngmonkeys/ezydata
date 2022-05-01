package com.tvd12.ezydata.morphia.testing;

import com.tvd12.ezyfox.database.repository.EzyEmptyRepository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class GenericsTest {

    public static void main(String[] args) {
        Type[] genericInterfaces = InterfaceB.class.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                System.out.println(genericInterface);
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                for (Type genericType : genericTypes) {
                    System.out.println("Generic type: " + genericType);
                }
                Class<?> clazz = (Class<?>) ((ParameterizedType) genericInterface).getRawType();
                System.out.println(Arrays.toString(clazz.getGenericInterfaces()));
            }
        }
    }

    public interface InterfaceA<I, E> extends EzyEmptyRepository<I, E> {}

    public interface InterfaceB extends
        InterfaceA<String, ClassA>,
        InterfaceC<Integer, Float> {}

    public interface InterfaceC<I, E> {}

    public static class ClassA {}
}
