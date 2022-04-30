package com.tvd12.ezydata.hazelcast.testing.mapstore;

import com.tvd12.ezydata.hazelcast.mapstore.EzyBeanMapstoreCreator;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.Properties;

public class EzyBeanMapstoreCreatorTest extends BaseTest {

    @Test
    public void test() {
        EzyBeanContext beanContext = EzyBeanContext.builder()
            .addSingletonClass(ExampleUserMapstore.class)
            .build();
        EzyBeanMapstoreCreator creator = new EzyBeanMapstoreCreator();
        creator.setContext(beanContext);
        assert creator.create("example_users", new Properties()) != null;
    }
}
