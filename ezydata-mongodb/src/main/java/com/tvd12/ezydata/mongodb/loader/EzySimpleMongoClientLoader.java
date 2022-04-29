/**
 * 
 */
package com.tvd12.ezydata.mongodb.loader;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.properties.file.reader.BaseFileReader;


/**
 * 
 * @author tavandung12
 *
 */
public class EzySimpleMongoClientLoader extends EzyPropertiesMongoClientLoader {
    
    protected InputStream inputStream;

    public EzySimpleMongoClientLoader configFile(String filePath) {
        return inputStream(
            EzyAnywayInputStreamLoader.builder()
                .build()
                .load(filePath)
        );
    }
    
    public EzySimpleMongoClientLoader inputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public EzySimpleMongoClientLoader properties(Map map) {
        return (EzySimpleMongoClientLoader) super.properties(map);
    }
    
    @Override
    public EzySimpleMongoClientLoader property(String name, Object value) {
        return (EzySimpleMongoClientLoader) super.property(name, value);
    }
    
    @Override
    protected void preload() {
        if(inputStream != null)
            this.properties.putAll(loadInputStream());
    }
    
    private Properties loadInputStream() {
        return new BaseFileReader().loadInputStream(inputStream);
    }
    
}
