package com.tvd12.ezydata.hazelcast.testing.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class Monkey implements Serializable {
    private static final long serialVersionUID = 8171070638249273626L;

    private String name;
    private int age;
}
