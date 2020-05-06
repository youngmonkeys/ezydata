package com.tvd12.ezydata.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezydata.redis.setting.EzyRedisMapSetting;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyRedisMap<K, V> implements Map<K, V> {

	protected final String mapName;
	protected final byte[] mapNameBytes;
	protected final Class<K> keyType;
	protected final Class<V> valueType;
	protected final EzyRedisClient redisClient;
	protected final EzyEntityCodec entityCodec;
	protected final EzyRedisMapSetting setting;
	
	protected EzyRedisMap(Builder builder) {
		this.setting = builder.setting;
		this.mapName = builder.mapName;
		this.keyType = setting.getKeyType();
		this.valueType = setting.getValueType();
		this.redisClient = builder.redisClient;
		this.entityCodec = builder.entityCodec;
		this.mapNameBytes = mapName.getBytes();
	}
	
	@Override
	public V put(K key, V value) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = entityCodec.serialize(value);
		redisClient.hset(mapNameBytes, keyBytes, valueBytes);
		return value;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		Map<byte[], byte[]> keyValueBytesMap = new HashMap<>();
		for(K key : m.keySet()) {
			V value = m.get(key);
			byte[] keyBytes = entityCodec.serialize(key);
			byte[] valueBytes = entityCodec.serialize(value);
			keyValueBytesMap.put(keyBytes, valueBytes);
		}
		redisClient.hmset(mapNameBytes, keyValueBytesMap);
	}
	
	@Override
	public V get(Object key) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = redisClient.hget(mapNameBytes, keyBytes);
		V value = entityCodec.deserialize(valueBytes, valueType);
		return value;
	}
	
	public Map<K, V> get(Set<K> keys) {
		byte[][] keyBytesArray = new byte[keys.size()][];
		int i = 0;
		for(K key : keys)
			keyBytesArray[i++] = entityCodec.serialize(key);
		List<byte[]> valueBytesList = redisClient.hmget(mapNameBytes, keyBytesArray);
		Map<K, V> answer = new HashMap<>();
		int k = 0;
		for(K key : keys) {
			byte[] valueBytes = valueBytesList.get(k ++);
			if(valueBytes == null)
				continue;
			V value = entityCodec.deserialize(valueBytes, valueType);
			answer.put(key, value);
		}
		return answer;
	}
	
	@Override
	public boolean containsKey(Object key) {
		byte[] keyBytes = entityCodec.serialize(key);
		byte[] valueBytes = redisClient.hget(mapNameBytes, keyBytes);
		return valueBytes != null;
	}

	@Override
	public boolean containsValue(Object value) {
		Map<byte[], byte[]> keyValueBytesMap = redisClient.hgetAll(mapNameBytes);
		for(byte[] valueBytes : keyValueBytesMap.values()) {
			V v = entityCodec.deserialize(valueBytes, valueType);
			if(value.equals(v))
				return true;
		}
		return false;
	}

	@Override
	public V remove(Object key) {
		byte[] keyBytes = entityCodec.serialize(key);
		redisClient.hdel(mapNameBytes, keyBytes);
		return null;
	}
	
	public int remove(Collection<K> keys) {
		byte[][] keyBytesArray = new byte[keys.size()][];
		int i = 0;
		for(K key : keys)
			keyBytesArray[i++] = entityCodec.serialize(key);
		Long count = redisClient.hdel(mapNameBytes, keyBytesArray);
		return count.intValue();
	}

	@Override
	public Set<K> keySet() {
		Set<byte[]> keyBytesSet = redisClient.hkeys(mapNameBytes);
		Set<K> answer = new HashSet<>();
		for(byte[] keyBytes : keyBytesSet) {
			K key = entityCodec.deserialize(keyBytes, keyType);
			answer.add(key);
		}
		return answer;
	}

	@Override
	public Collection<V> values() {
		Map<byte[], byte[]> keyValueBytesMap = redisClient.hgetAll(mapNameBytes);
		List<V> answer = new ArrayList<>();
		for(byte[] valueBytes : keyValueBytesMap.values()) {
			V value = entityCodec.deserialize(valueBytes, valueType);
			answer.add(value);
		}
		return answer;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		Map<byte[], byte[]> keyValueBytesMap = redisClient.hgetAll(mapNameBytes);
		Map<K, V> answer = new HashMap<>();
		for(byte[] keyBytes : keyValueBytesMap.keySet()) {
			byte[] valueBytes = keyValueBytesMap.get(keyBytes);
			K key = entityCodec.deserialize(keyBytes, keyType);
			V value = entityCodec.deserialize(valueBytes, valueType);
			answer.put(key, value);
		}
		return answer.entrySet();
	}
	
	@Override
	public int size() {
		return (int)sizeLong();
	}
	
	public long sizeLong() {
		long size = redisClient.hlen(mapNameBytes);
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public void clear() {
		redisClient.del(mapNameBytes);
	}
	
	public String getName() {
		return mapName;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisMap> {

		protected String mapName;
		protected EzyRedisClient redisClient;
		protected EzyEntityCodec entityCodec;
		protected EzyRedisMapSetting setting;
		
		public Builder mapName(String mapName) {
			this.mapName = mapName;
			return this;
		}
		
		public Builder setting(EzyRedisMapSetting setting) {
			this.setting = setting;
			return this;
		}
		
		public Builder redisClient(EzyRedisClient redisClient) {
			this.redisClient = redisClient;
			return this;
		}
		
		public Builder entityCodec(EzyEntityCodec entityCodec) {
			this.entityCodec = entityCodec;
			return this;
		}
		
		@Override
		public EzyRedisMap build() {
			return new EzyRedisMap<>(this);
		}
		
	}

}
