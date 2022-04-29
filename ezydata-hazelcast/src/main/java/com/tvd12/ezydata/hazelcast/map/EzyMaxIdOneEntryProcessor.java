package com.tvd12.ezydata.hazelcast.map;

import static com.tvd12.ezydata.hazelcast.map.EzyAbstractMaxIdEntryProcessor.*;

import java.io.IOException;
import java.util.Map.Entry;

import com.hazelcast.map.EntryProcessor;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class EzyMaxIdOneEntryProcessor 
        implements EntryProcessor<String, Long, Long>, DataSerializable {
    private static final long serialVersionUID = -3802285433756793878L;

    @Override
    public Long process(Entry<String, Long> entry) {
        return processEntry(entry, 1);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {}

    @Override
    public void readData(ObjectDataInput in) throws IOException {}

}
