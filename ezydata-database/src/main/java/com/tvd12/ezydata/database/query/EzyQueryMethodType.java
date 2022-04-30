package com.tvd12.ezydata.database.query;

import lombok.Getter;

public enum EzyQueryMethodType {

    FIND("findBy"),
    COUNT("countBy"),
    DELETE("deleteBy");

    @Getter
    private final String prefix;

    EzyQueryMethodType(String prefix) {
        this.prefix = prefix;
    }
}
