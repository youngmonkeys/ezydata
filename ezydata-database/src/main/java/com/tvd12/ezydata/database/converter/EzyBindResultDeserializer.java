package com.tvd12.ezydata.database.converter;

import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@SuppressWarnings("rawtypes")
public class EzyBindResultDeserializer implements EzyResultDeserializer {

    protected final Class<?> resultType;
    protected final EzyUnmarshaller unmarshaller;

    @Override
    public Object deserialize(Object result) {
        EzyArray array = EzyEntityFactory.newArray();
        if (result instanceof Iterable) {
            for (Object item : (Iterable) result) {
                array.add(item);
            }
        } else if (result instanceof Object[]) {
            array.add((Object[]) result);
        } else {
            array.add(result);
        }
        return unmarshaller.unmarshal(array, resultType);
    }
}
