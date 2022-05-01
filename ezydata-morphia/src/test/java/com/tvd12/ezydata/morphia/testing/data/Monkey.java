package com.tvd12.ezydata.morphia.testing.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity(value = "ezyfox.mongodb.testing.monkey")
public class Monkey {
    @Id
    private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String name = "monkey#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

    public Monkey(Long id) {
        this.id = id;
    }
}
