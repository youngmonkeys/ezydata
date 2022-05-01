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
@Entity(value = "ezyfox.mongodb.testing.chicken")
public class Chicken {
    @Id
    private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String name = "cat#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

    public Chicken(Long id) {
        this.id = id;
    }
}
