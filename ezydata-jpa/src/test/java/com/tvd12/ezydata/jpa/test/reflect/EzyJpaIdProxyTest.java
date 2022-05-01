package com.tvd12.ezydata.jpa.test.reflect;

import com.tvd12.ezydata.jpa.reflect.EzyJpaIdProxy;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.persistence.Id;

@Test
public class EzyJpaIdProxyTest {

    @Test
    public void thereIsNoIdSetterMethod() {
        // given
        // when
        Throwable e = Asserts.assertThrows(() -> new EzyJpaIdProxy(
            new EzyClass(NoIdSetterMethod.class)
        ));

        // then
        Asserts.assertEqualsType(e, IllegalArgumentException.class);
        Asserts.assertEquals(
            e.getMessage(),
            "missing annotated @Id field or getter/setter methods"
        );
    }

    @Test
    public void thereIsNoIdGetterMethod() {
        // given
        // when
        Throwable e = Asserts.assertThrows(() -> new EzyJpaIdProxy(
            new EzyClass(NoIdGetterMethod.class)
        ));

        // then
        Asserts.assertEqualsType(e, IllegalArgumentException.class);
        Asserts.assertEquals(
            e.getMessage(),
            "missing annotated @Id field or getter/setter methods"
        );
    }

    private static class NoIdSetterMethod {
        @Id
        public long getId() {
            return 0L;
        }
    }

    private static class NoIdGetterMethod {
        @SuppressWarnings("unused")
        @Id
        public void setId(long id) {}
    }
}
