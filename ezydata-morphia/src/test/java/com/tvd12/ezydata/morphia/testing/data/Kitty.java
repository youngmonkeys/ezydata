package com.tvd12.ezydata.morphia.testing.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity(value = "ezyfox.mongodb.testing.kitty")
public class Kitty {
    public int age = 10;
    @Id
    private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String name = "cat#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String fixedValue = "fixedValue";
    private Set<String> set = new HashSet<>();

    public Kitty(Long id) {
        this.id = id;
    }
}
