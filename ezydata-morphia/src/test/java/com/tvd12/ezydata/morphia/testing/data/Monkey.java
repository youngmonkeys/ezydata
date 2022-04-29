package com.tvd12.ezydata.morphia.testing.data;

import java.util.concurrent.ThreadLocalRandom;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity(value = "ezyfox.mongodb.testing.monkey", noClassnameStored = true)
public class Monkey {
    @Id
    private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
    private String name = "monkey#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

    public Monkey(Long id) {
        this.id = id;
    }
}
