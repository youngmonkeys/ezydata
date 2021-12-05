package com.tvd12.ezydata.jpa.test.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ezyfox_jpa_monkey")
public class Monkey {

    private String monkeyId;
    
    @Id
    public void setId(String id) {
        this.monkeyId = id;
    }
    
    @Id
    public String getId() {
        return monkeyId;
    }
    
}
