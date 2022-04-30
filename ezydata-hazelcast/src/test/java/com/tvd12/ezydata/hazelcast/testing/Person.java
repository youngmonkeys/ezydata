package com.tvd12.ezydata.hazelcast.testing;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Person implements Serializable {
    private static final long serialVersionUID = -6731210894904620373L;

    private String name;
    private int age;
}
