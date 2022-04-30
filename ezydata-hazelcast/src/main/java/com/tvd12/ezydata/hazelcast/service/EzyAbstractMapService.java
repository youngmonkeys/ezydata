package com.tvd12.ezydata.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.transaction.TransactionalMap;
import com.tvd12.ezydata.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezydata.hazelcast.factory.EzyMapTransactionFactoryAware;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezyfox.function.EzyExceptionApply;
import com.tvd12.ezyfox.function.EzyExceptionFunction;
import com.tvd12.ezyfox.function.EzyExceptionVoid;
import com.tvd12.ezyfox.function.EzySupplier;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class EzyAbstractMapService<K, V>
    extends EzyAbstractHazelcastService
    implements EzyMapTransactionFactoryAware {

    protected IMap<K, V> map;

    @Setter
    protected EzyMapTransactionFactory mapTransactionFactory;

    public EzyAbstractMapService() {}

    public EzyAbstractMapService(HazelcastInstance hazelcastInstance) {
        super(hazelcastInstance);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        super.setHazelcastInstance(hazelcastInstance);
        this.map = hazelcastInstance.getMap(getMapName());
    }

    protected abstract String getMapName();

    protected void mapLockUpdate(K key, EzyExceptionVoid applier) {
        map.lock(key);
        try {
            applier.apply();
        } catch (Exception e) {
            throw new IllegalStateException("lock to update with key " + key + " error", e);
        } finally {
            map.unlock(key);
        }
    }

    protected <T> T mapLockUpdateAndGet(K key, EzySupplier<T> supplier) {
        map.lock(key);
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new IllegalStateException("lock to update and get with key " + key + " error", e);
        } finally {
            map.unlock(key);
        }
    }

    protected void mapLockUpdateWithException(
        K key, EzyExceptionVoid applier) throws Exception {
        map.lock(key);
        try {
            applier.apply();
        } finally {
            map.unlock(key);
        }
    }

    protected <T> T mapLockUpdateAndGetWithException(
        K key, EzySupplier<T> supplier) throws Exception {
        map.lock(key);
        try {
            return supplier.get();
        } finally {
            map.unlock(key);
        }
    }

    protected EzyMapApplyTransaction<K, V> newApplyTransaction() {
        return mapTransactionFactory.newApplyTransaction(getMapName());
    }

    protected <R> EzyMapReturnTransaction<K, V, R> newReturnTransaction() {
        return mapTransactionFactory.newReturnTransaction(getMapName());
    }

    protected final void transactionUpdate(
        EzyExceptionApply<TransactionalMap<K, V>> applier) throws Exception {
        EzyMapApplyTransaction<K, V> transaction = newApplyTransaction();
        transaction.begin();
        try {
            transaction.apply(applier);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    protected final V transactionGet(K key) throws Exception {
        return transactionGet(key, null);
    }

    protected final V transactionGet(K key, V defValue) throws Exception {
        EzyMapReturnTransaction<K, V, V> transaction = newReturnTransaction();
        transaction.begin();
        try {
            V value = transaction.apply(m -> m.getForUpdate(key));
            transaction.commit();
            return value == null ? defValue : value;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    protected final Map<K, V> transactionGet(Set<K> keys) throws Exception {
        EzyMapReturnTransaction<K, V, Map<K, V>> transaction = newReturnTransaction();
        transaction.begin();
        try {
            Map<K, V> answer = transaction.apply(m -> {
                Map<K, V> result = new HashMap<>();
                for (K key : keys) {
                    result.put(key, m.getForUpdate(key));
                }
                return result;
            });
            transaction.commit();
            return answer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    protected final void transactionUpdate(
        K key, EzyExceptionApply<V> applier) throws Exception {
        transactionUpdate(key, applier, null);
    }

    protected final void transactionUpdate(
        K key, EzyExceptionApply<V> applier, V defValue) throws Exception {
        transactionUpdate(m -> transactionUpdate(m, key, applier, defValue));
    }

    protected final <R> R transactionUpdateAndGet(
        EzyExceptionFunction<TransactionalMap<K, V>, R> applier) throws Exception {
        EzyMapReturnTransaction<K, V, R> transaction = newReturnTransaction();
        transaction.begin();
        try {
            R answer =
                transaction.apply(applier);
            transaction.commit();
            return answer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    protected final <R> R transactionUpdateAndGet(
        K key, EzyExceptionFunction<V, R> applier) throws Exception {
        return transactionUpdateAndGet(m -> transactionUpdateAndGet(m, key, applier, null));
    }

    protected final <R> R transactionUpdateAndGet(
        K key, EzyExceptionFunction<V, R> applier, V defValue) throws Exception {
        return transactionUpdateAndGet(m -> transactionUpdateAndGet(m, key, applier, defValue));
    }

    private void transactionUpdate(
        TransactionalMap<K, V> m, K key, EzyExceptionApply<V> applier, V defValue) throws Exception {
        V value = m.getForUpdate(key);
        if (value == null) {
            value = defValue;
        }
        if (value != null) {
            applier.apply(value);
            m.set(key, value);
        }
    }

    private <R> R transactionUpdateAndGet(
        TransactionalMap<K, V> m, K key, EzyExceptionFunction<V, R> applier, V defValue) throws Exception {
        V value = m.getForUpdate(key);
        if (value == null) {
            value = defValue;
        }
        R result = null;
        if (value != null) {
            result = applier.apply(value);
            m.set(key, value);
        }
        return result;
    }
}
