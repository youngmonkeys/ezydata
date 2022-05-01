package com.tvd12.ezydata.morphia.testing;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.*;

import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity(value = "ezyfox.mongodb.testing.duck")
public class Duck {

    @Id
    private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String name = "cat#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String fixedValue = "fixedValue";

    public Duck(Long id) {
        this.id = id;
    }
}
