package com.tvd12.ezydata.redis;

import com.tvd12.ezydata.redis.annotation.EzyRedisMessage;
import com.tvd12.ezydata.redis.loader.EzyJedisClientPoolLoader;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;
import com.tvd12.ezydata.redis.util.EzyRedisMessageAnnotations;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.ezyfox.binding.EzyBindingContextBuilder;
import com.tvd12.ezyfox.binding.codec.EzyBindingEntityCodec;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfox.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.data.annotation.EzyCachedKey;
import com.tvd12.ezyfox.data.annotation.EzyCachedValue;
import com.tvd12.ezyfox.data.util.EzyCachedValueAnnotations;
import com.tvd12.ezyfox.message.annotation.EzyMessage;
import com.tvd12.ezyfox.naming.EzyNameTranslator;
import com.tvd12.ezyfox.naming.EzyNamingCase;
import com.tvd12.ezyfox.naming.EzySimpleNameTranslator;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyField;
import com.tvd12.ezyfox.reflect.EzyReflection;
import com.tvd12.ezyfox.reflect.EzyReflectionProxy;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyThreads;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.tvd12.ezydata.redis.setting.EzyRedisSettings.MAX_CONNECTION_ATTEMPTS;

public class EzyRedisProxyFactory extends EzyLoggable {

    protected final EzyRedisSettings settings;
    protected final EzyEntityCodec entityCodec;
    protected final EzyRedisClientPool clientPool;
    protected final int maxConnectionAttempts;

    protected EzyRedisProxyFactory(Builder builder) {
        this.settings = builder.settings;
        this.entityCodec = builder.entityCodec;
        this.clientPool = builder.clientPool;
        this.maxConnectionAttempts = builder.maxConnectionAttempts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public EzyRedisProxy newRedisProxy() {
        return EzyRedisProxy.builder()
            .settings(settings)
            .redisClient(getRedisClient())
            .entityCodec(entityCodec)
            .build();
    }

    private EzyRedisClient getRedisClient() {
        int retryCount = 0;
        while (true) {
            try {
                return clientPool.getClient();
            } catch (Exception e) {
                if (retryCount >= maxConnectionAttempts) {
                    throw e;
                }
                logger.error(
                    "can not get redis client, retry count: {}",
                    (++retryCount),
                    e
                );
                EzyThreads.sleep(3000);
            }
        }
    }

    public static class Builder implements EzyBuilder<EzyRedisProxyFactory> {

        protected EzyReflection reflection;
        protected Set<String> packagesToScan;
        protected Properties properties;
        protected int maxConnectionAttempts;
        protected EzyRedisSettings settings;
        protected EzyEntityCodec entityCodec;
        protected EzyRedisClientPool clientPool;
        protected EzyRedisSettingsBuilder settingsBuilder;
        protected EzyNameTranslator mapNameTranslator;

        public Builder() {
            this.properties = new Properties();
            this.packagesToScan = new HashSet<>();
        }

        public Builder scan(String packageName) {
            packagesToScan.add(packageName);
            return this;
        }

        public Builder scan(String... packageNames) {
            return scan(Sets.newHashSet(packageNames));
        }

        public Builder scan(Iterable<String> packageNames) {
            for (String packageName : packageNames) {
                this.scan(packageName);
            }
            return this;
        }

        public Builder properties(Properties properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder settings(EzyRedisSettings settings) {
            this.settings = settings;
            return this;
        }

        public Builder settingsBuilder(EzyRedisSettingsBuilder settingsBuilder) {
            this.settingsBuilder = settingsBuilder;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        public Builder clientPool(EzyRedisClientPool clientPool) {
            this.clientPool = clientPool;
            return this;
        }

        public Builder mapNameTranslator(EzyNameTranslator mapNameTranslator) {
            this.mapNameTranslator = mapNameTranslator;
            return this;
        }

        public Builder mapNameTranslator(EzyNamingCase namingCase, String ignoredSuffix) {
            return mapNameTranslator(EzySimpleNameTranslator.builder()
                .namingCase(namingCase)
                .ignoredSuffix(ignoredSuffix)
                .build()
            );
        }

        @Override
        public EzyRedisProxyFactory build() {
            if (packagesToScan.size() > 0) {
                this.reflection = new EzyReflectionProxy(packagesToScan);
            }
            this.extractProperties();
            this.prepareMapNameTranslator();
            this.prepareSettings();
            this.prepareEntityCodec();
            this.prepareClientPool();
            return new EzyRedisProxyFactory(this);
        }

        private void extractProperties() {
            this.maxConnectionAttempts = Integer.parseInt(
                properties
                    .getOrDefault(MAX_CONNECTION_ATTEMPTS, maxConnectionAttempts)
                    .toString()
            );
        }

        private void prepareSettings() {
            if (settings != null) {
                return;
            }
            if (settingsBuilder == null) {
                settingsBuilder = new EzyRedisSettingsBuilder();
            }
            settingsBuilder.properties(properties);
            if (reflection != null) {
                Set<Class<?>> cachedClasses = reflection.getAnnotatedClasses(EzyCachedValue.class);
                for (Class<?> cachedClass : cachedClasses) {
                    EzyField keyField = getMapKeyFieldOf(cachedClass);
                    String mapName = mapNameTranslator
                        .translate(EzyCachedValueAnnotations.getMapName(cachedClass));
                    settingsBuilder.mapSettingBuilder(mapName)
                        .keyType(keyField.getType())
                        .valueType(cachedClass);
                }
                Set<Class<?>> messageClasses = new HashSet<>();
                messageClasses.addAll(reflection.getAnnotatedClasses(EzyMessage.class));
                messageClasses.addAll(reflection.getAnnotatedClasses(EzyRedisMessage.class));
                for (Class<?> messageClass : messageClasses) {
                    String channelName = EzyRedisMessageAnnotations.getChannelName(messageClass);
                    settingsBuilder.channelSettingBuilder(channelName)
                        .messageType(messageClass);
                }
            }
            this.settings = settingsBuilder.build();
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void prepareEntityCodec() {
            if (entityCodec != null) {
                return;
            }
            EzyBindingContextBuilder bindingContextBuilder = EzyBindingContext.builder();
            if (reflection != null) {
                Set<Class<?>> valueClasses = reflection.getAnnotatedClasses(EzyCachedValue.class);
                bindingContextBuilder
                    .addAllClasses(reflection)
                    .addClasses((Set) valueClasses)
                    .addClasses((Set) reflection.getAnnotatedClasses(EzyMessage.class))
                    .addClasses((Set) reflection.getAnnotatedClasses(EzyRedisMessage.class))
                    .addClasses((Set) reflection.getAnnotatedClasses(EzyCachedKey.class));
                for (Class<?> valueClass : valueClasses) {
                    EzyField keyField = getMapKeyFieldOf(valueClass);
                    EzyCachedKey cachedKeyAnno = keyField.getAnnotation(EzyCachedKey.class);
                    if (cachedKeyAnno != null && cachedKeyAnno.composite()) {
                        bindingContextBuilder.addClass(keyField.getType());
                    }
                }

            }
            EzyBindingContext bindingContext = bindingContextBuilder.build();
            entityCodec = EzyBindingEntityCodec.builder()
                .marshaller(bindingContext.newMarshaller())
                .unmarshaller(bindingContext.newUnmarshaller())
                .messageSerializer(new MsgPackSimpleSerializer())
                .messageDeserializer(new MsgPackSimpleDeserializer())
                .build();
        }

        private EzyField getMapKeyFieldOf(Class<?> mapValueClass) {
            EzyClass clazz = new EzyClass(mapValueClass);
            return clazz.getField(f ->
                    f.isAnnotated(EzyCachedKey.class)
                        || f.isAnnotated(EzyId.class)
                        || f.isAnnotated(Id.class)
                )
                .orElseThrow(() -> new IllegalArgumentException(
                    "unknown key type of cached value type: " + mapValueClass.getName() +
                        ", annotate key field with @EzyCachedKey or @EzyId or @Id"
                ));
        }

        private void prepareClientPool() {
            if (clientPool != null) {
                return;
            }
            clientPool = new EzyJedisClientPoolLoader()
                .properties(properties)
                .load();
        }

        protected void prepareMapNameTranslator() {
            if (mapNameTranslator == null) {
                EzyNamingCase namingCase =
                    EzyNamingCase.of(
                        properties.getProperty(EzyRedisSettings.MAP_NAMING_CASE)
                    );
                String ignoredSuffix =
                    properties.getProperty(EzyRedisSettings.MAP_NAMING_IGNORED_SUFFIX);
                mapNameTranslator(namingCase, ignoredSuffix);
            }
        }
    }
}
