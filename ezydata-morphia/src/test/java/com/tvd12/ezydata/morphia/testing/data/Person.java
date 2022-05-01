package com.tvd12.ezydata.morphia.testing.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
@Entity(value = "ezyfox.mongodb.testing.person")
public class Person implements Serializable {
    private static final long serialVersionUID = 903502360552527690L;

    @Id
    private ObjectId id;
    private String name = "name#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String password = UUID.randomUUID().toString();

}
