package com.tvd12.ezydata.mongodb.testing.loader;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.test.reflect.MethodUtil;

public class EzySimpleMongoClientLoaderTest {

    @Test
    public void preloadInputStreamNotNull() {
        // given
        EzySimpleMongoClientLoader loader = new EzySimpleMongoClientLoader();
        
        // when
        // then
        MethodUtil.invokeMethod("preload", loader);
    }
}
