package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezyfox.data.annotation.EzyCachedKey;
import com.tvd12.ezyfox.data.annotation.EzyCachedValue;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCachedValue("ezydata_author3")
public class CompositeAuthor {

    @EzyCachedKey(composite = true)
    private CompositeId id;

    private String name;

    @Getter
    @Setter
    public static class CompositeId {
        private long id;
    }
}
