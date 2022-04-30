package com.tvd12.ezydata.hazelcast.map;

import com.hazelcast.map.EntryProcessor;

import java.util.Map.Entry;

public class EzyMaxIdEntryProcessor
    extends EzyAbstractMaxIdEntryProcessor
    implements EntryProcessor<String, Long, Long> {
    private static final long serialVersionUID = -3802285433756793878L;

    public EzyMaxIdEntryProcessor() {}

    public EzyMaxIdEntryProcessor(int delta) {
        super(delta);
    }

    @Override
    public Long process(Entry<String, Long> entry) {
        return processEntry(entry, delta);
    }
}
