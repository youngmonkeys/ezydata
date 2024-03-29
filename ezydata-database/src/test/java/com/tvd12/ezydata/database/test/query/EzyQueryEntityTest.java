package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import org.testng.annotations.Test;

public class EzyQueryEntityTest {

    @Test
    public void test() {
        EzyQueryEntity queryA = EzyQueryEntity.builder()
            .type("find")
            .name("findOne")
            .value("select e from E e")
            .nativeQuery(false)
            .build();
        EzyQueryEntity queryB = EzyQueryEntity.builder()
            .type("find")
            .name("findOne")
            .value("select e from E e")
            .nativeQuery(false)
            .build();
        EzyQueryEntity queryC = EzyQueryEntity.builder()
            .type("find")
            .name("findTwo")
            .value("select e from E e")
            .nativeQuery(false)
            .build();
        assert queryA.getType().equals("find");
        assert !queryA.isNativeQuery();
        System.out.println(queryA);
        assert queryA.hashCode() == "findOne".hashCode();
        //noinspection ConstantConditions
        assert !queryA.equals(null);
        //noinspection EqualsWithItself
        assert queryA.equals(queryA);
        assert queryA.equals(queryB);
        assert !queryA.equals(queryC);
        //noinspection EqualsBetweenInconvertibleTypes
        assert !queryA.equals(getClass());

        try {
            EzyQueryEntity.builder()
                .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            EzyQueryEntity.builder()
                .name("hello")
                .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
