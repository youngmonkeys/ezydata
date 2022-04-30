package com.tvd12.ezydata.hazelcast.transaction;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyEnums;
import lombok.Getter;

public enum EzyTransactionType implements EzyConstant {

    TWO_PHASE(1),
    ONE_PHASE(2);

    @Getter
    private int id;

    EzyTransactionType(int id) {
        this.id = id;
    }

    public static EzyTransactionType valueOf(int id) {
        return EzyEnums.valueOf(values(), id);
    }

    @Override
    public String getName() {
        return toString();
    }
}
