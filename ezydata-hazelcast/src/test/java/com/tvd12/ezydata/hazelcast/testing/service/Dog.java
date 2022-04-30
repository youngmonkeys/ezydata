package com.tvd12.ezydata.hazelcast.testing.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Dog implements Serializable {
    private static final long serialVersionUID = -868330127693127237L;

    private String name;
    private int age;

    public void exception() {
        throw new RuntimeException();
    }

    public long exception2() {
        throw new RuntimeException();
    }
}
